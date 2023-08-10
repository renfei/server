package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.GoogleAuthenticatorConfig;
import net.renfei.server.core.constant.HmacHashFunction;
import net.renfei.server.core.entity.GoogleAuthenticatorKey;
import net.renfei.server.core.exception.GoogleAuthenticatorException;
import net.renfei.server.core.security.ReseedingSecureRandom;
import net.renfei.server.core.service.GoogleAuthenticator;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 谷歌两步认证
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthenticatorImpl implements GoogleAuthenticator {
    /**
     * Number of digits of a scratch code represented as a decimal integer.
     */
    private static final int SCRATCH_CODE_LENGTH = 8;

    /**
     * Modulus used to truncate the scratch code.
     */
    public static final int SCRATCH_CODE_MODULUS = (int) Math.pow(10, SCRATCH_CODE_LENGTH);

    /**
     * Magic number representing an invalid scratch code.
     */
    private static final int SCRATCH_CODE_INVALID = -1;

    /**
     * Length in bytes of each scratch code. We're using Google's default of
     * using 4 bytes per scratch code.
     */
    private static final int BYTES_PER_SCRATCH_CODE = 4;

    /**
     * The default SecureRandom algorithm to use if none is specified.
     *
     * @see java.security.SecureRandom#getInstance(String)
     * @since 0.5.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static final String DEFAULT_RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    /**
     * The default random number algorithm provider to use if none is specified.
     *
     * @see java.security.SecureRandom#getInstance(String)
     * @since 0.5.0
     */
    private static final String DEFAULT_RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";

    /**
     * The configuration used by the current instance.
     */
    private final GoogleAuthenticatorConfig config;

    /**
     * The internal SecureRandom instance used by this class.  Since Java 7
     * {@link Random} instances are required to be thread-safe, no synchronisation is
     * required in the methods of this class using this instance.  Thread-safety
     * of this class was a de-facto standard in previous versions of Java so
     * that it is expected to work correctly in previous versions of the Java
     * platform as well.
     */
    private final ReseedingSecureRandom secureRandom = new ReseedingSecureRandom(
            getRandomNumberAlgorithm(),
            getRandomNumberAlgorithmProvider()
    );

    /**
     * @return the default random number generator algorithm.
     * @since 0.5.0
     */
    private String getRandomNumberAlgorithm() {
        return DEFAULT_RANDOM_NUMBER_ALGORITHM;
    }

    /**
     * @return the default random number generator algorithm provider.
     * @since 0.5.0
     */
    private String getRandomNumberAlgorithmProvider() {
        return DEFAULT_RANDOM_NUMBER_ALGORITHM_PROVIDER;
    }

    /**
     * Calculates the verification code of the provided key at the specified
     * instant of time using the algorithm specified in RFC 6238.
     *
     * @param key the secret key in binary format.
     * @param tm  the instant of time.
     * @return the validation code for the provided key at the specified instant
     * of time.
     */
    int calculateCode(byte[] key, long tm) {
        // Allocating an array of bytes to represent the specified instant
        // of time.
        byte[] data = new byte[8];
        long value = tm;

        // Converting the instant of time from the long representation to a
        // big-endian array of bytes (RFC4226, 5.2. Description).
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        // Building the secret key specification for the HmacSHA1 algorithm.
        SecretKeySpec signKey = new SecretKeySpec(key, config.getHmacHashFunction().toString());

        try {
            // Getting an HmacSHA1/HmacSHA256 algorithm implementation from the JCE.
            Mac mac = Mac.getInstance(config.getHmacHashFunction().toString());

            // Initializing the MAC algorithm.
            mac.init(signKey);

            // Processing the instant of time and getting the encrypted data.
            byte[] hash = mac.doFinal(data);

            // Building the validation code performing dynamic truncation
            // (RFC4226, 5.3. Generating an HOTP value)
            int offset = hash[hash.length - 1] & 0xF;

            // We are using a long because Java hasn't got an unsigned integer type
            // and we need 32 unsigned bits).
            long truncatedHash = 0;

            for (int i = 0; i < 4; ++i) {
                truncatedHash <<= 8;

                // Java bytes are signed but we need an unsigned integer:
                // cleaning off all but the LSB.
                truncatedHash |= (hash[offset + i] & 0xFF);
            }

            // Clean bits higher than the 32nd (inclusive) and calculate the
            // module with the maximum validation code value.
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= config.getKeyModulus();

            // Returning the validation code to the caller.
            return (int) truncatedHash;
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.warn(ex.getMessage(), ex);
            // We're not disclosing internal error details to our clients.
            throw new GoogleAuthenticatorException("The operation cannot be performed now.");
        }
    }

    private long getTimeWindowFromTime(long time) {
        return time / this.config.getTimeStepSizeInMillis();
    }

    /**
     * This method implements the algorithm specified in RFC 6238 to check if a
     * validation code is valid in a given instant of time for the given secret
     * key.
     *
     * @param secret    the Base32 encoded secret key.
     * @param code      the code to validate.
     * @param timestamp the instant of time to use during the validation process.
     * @param window    the window size to use during the validation process.
     * @return <code>true</code> if the validation code is valid,
     * <code>false</code> otherwise.
     */
    private boolean checkCode(
            String secret,
            long code,
            long timestamp,
            int window) {
        byte[] decodedKey = decodeSecret(secret);

        // convert unix time into a 30 second "window" as specified by the
        // TOTP specification. Using Google's default interval of 30 seconds.
        final long timeWindow = getTimeWindowFromTime(timestamp);

        // Calculating the verification code of the given key in each of the
        // time intervals and returning true if the provided code is equal to
        // one of them.
        for (int i = -((window - 1) / 2); i <= window / 2; ++i) {
            // Calculating the verification code for the current time interval.
            long hash = calculateCode(decodedKey, timeWindow + i);

            // Checking if the provided code is equal to the calculated one.
            if (hash == code) {
                // The verification code is valid.
                return true;
            }
        }

        // The verification code is invalid.
        return false;
    }

    private byte[] decodeSecret(String secret) {
        // Decoding the secret key to get its raw byte representation.
        switch (config.getKeyRepresentation()) {
            case BASE32 -> {
                Base32 codec32 = new Base32();
                // See: https://issues.apache.org/jira/browse/CODEC-234
                // Commons Codec Base32::decode does not support lowercase letters.
                return codec32.decode(secret.toUpperCase());
            }
            case BASE64 -> {
                Base64 codec64 = new Base64();
                return codec64.decode(secret);
            }
            default -> throw new IllegalArgumentException("Unknown key representation type.");
        }
    }

    @Override
    public GoogleAuthenticatorKey createCredentials() {
        // Allocating a buffer sufficiently large to hold the bytes required by
        // the secret key.
        int bufferSize = config.getSecretBits() / 8;
        byte[] buffer = new byte[bufferSize];
        secureRandom.nextBytes(buffer);
        // Extracting the bytes making up the secret key.
        byte[] secretKey = Arrays.copyOf(buffer, bufferSize);
        String generatedKey = calculateSecretKey(secretKey);
        // Generating the verification code at time = 0.
        int validationCode = calculateValidationCode(secretKey);
        // Calculate scratch codes
        List<Integer> scratchCodes = calculateScratchCodes();
        return GoogleAuthenticatorKey.builder()
                .key(generatedKey)
                .config(config)
                .verificationCode(validationCode)
                .scratchCodes(scratchCodes)
                .build();
    }

    private List<Integer> calculateScratchCodes() {
        final List<Integer> scratchCodes = new ArrayList<>();

        for (int i = 0; i < config.getNumberOfScratchCodes(); ++i) {
            scratchCodes.add(generateScratchCode());
        }

        return scratchCodes;
    }

    /**
     * This method calculates a scratch code from a random byte buffer of
     * suitable size <code>#BYTES_PER_SCRATCH_CODE</code>.
     *
     * @param scratchCodeBuffer a random byte buffer whose minimum size is
     *                          <code>#BYTES_PER_SCRATCH_CODE</code>.
     * @return the scratch code.
     */
    private int calculateScratchCode(byte[] scratchCodeBuffer) {
        if (scratchCodeBuffer.length < BYTES_PER_SCRATCH_CODE) {
            throw new IllegalArgumentException(
                    String.format(
                            "The provided random byte buffer is too small: %d.",
                            scratchCodeBuffer.length));
        }

        int scratchCode = 0;

        for (int i = 0; i < BYTES_PER_SCRATCH_CODE; ++i) {
            scratchCode = (scratchCode << 8) + (scratchCodeBuffer[i] & 0xff);
        }

        scratchCode = (scratchCode & 0x7FFFFFFF) % SCRATCH_CODE_MODULUS;

        // Accept the scratch code only if it has exactly
        // SCRATCH_CODE_LENGTH digits.
        if (validateScratchCode(scratchCode)) {
            return scratchCode;
        } else {
            return SCRATCH_CODE_INVALID;
        }
    }

    /* package */ boolean validateScratchCode(int scratchCode) {
        return (scratchCode >= SCRATCH_CODE_MODULUS / 10);
    }

    /**
     * This method creates a new random byte buffer from which a new scratch
     * code is generated. This function is invoked if a scratch code generated
     * from the main buffer is invalid because it does not satisfy the scratch
     * code restrictions.
     *
     * @return A valid scratch code.
     */
    private int generateScratchCode() {
        while (true) {
            byte[] scratchCodeBuffer = new byte[BYTES_PER_SCRATCH_CODE];
            secureRandom.nextBytes(scratchCodeBuffer);

            int scratchCode = calculateScratchCode(scratchCodeBuffer);

            if (scratchCode != SCRATCH_CODE_INVALID) {
                return scratchCode;
            }
        }
    }

    /**
     * This method calculates the validation code at time 0.
     *
     * @param secretKey The secret key to use.
     * @return the validation code at time 0.
     */
    private int calculateValidationCode(byte[] secretKey) {
        return calculateCode(secretKey, 0);
    }


    public int getTotpPassword(String secret) {
        return getTotpPassword(secret, System.currentTimeMillis());
    }

    public int getTotpPassword(String secret, long time) {
        return calculateCode(decodeSecret(secret), getTimeWindowFromTime(time));
    }

    /**
     * This method calculates the secret key given a random byte buffer.
     *
     * @param secretKey a random byte buffer.
     * @return the secret key.
     */
    private String calculateSecretKey(byte[] secretKey) {
        switch (config.getKeyRepresentation()) {
            case BASE32:
                return new Base32().encodeToString(secretKey);
            case BASE64:
                return new Base64().encodeToString(secretKey);
            default:
                throw new IllegalArgumentException("Unknown key representation type.");
        }
    }

    @Override
    public boolean authorize(String secret, int verificationCode) {
        return authorize(secret, verificationCode, System.currentTimeMillis());
    }

    @Override
    public boolean authorize(String secret, int verificationCode, long time) {
        // Checking user input and failing if the secret key was not provided.
        if (secret == null) {
            throw new IllegalArgumentException("Secret cannot be null.");
        }

        // Checking if the verification code is between the legal bounds.
        if (verificationCode <= 0 || verificationCode >= this.config.getKeyModulus()) {
            return false;
        }

        // Checking the validation code using the current UNIX time.
        return checkCode(
                secret,
                verificationCode,
                time,
                this.config.getWindowSize());
    }

    /**
     * Returns the basic otpauth TOTP URI. This URI might be sent to the user via email, QR code or some other method.
     * Use a secure transport since this URI contains the secret.
     * <p>
     * The current implementation supports the following features:
     * <ul>
     * <li>Label, made up of an optional issuer and an account name.</li>
     * <li>Secret parameter.</li>
     * <li>Issuer parameter.</li>
     * </ul>
     *
     * @param issuer      The issuer name. This parameter cannot contain the colon
     *                    (:) character. This parameter can be null.
     * @param accountName The account name. This parameter shall not be null.
     * @param credentials The generated credentials.  This parameter shall not be null.
     * @return an otpauth scheme URI for loading into a client application.
     * @see <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">Google Authenticator - KeyUriFormat</a>
     */
    public String getOtpAuthTotpURL(String issuer,
                                           String accountName,
                                           GoogleAuthenticatorKey credentials) {
        URIBuilder uri = new URIBuilder()
                .setScheme("otpauth")
                .setHost("totp")
                .setPath("/" + formatLabel(issuer, accountName))
                .setParameter("secret", credentials.getKey());
        if (issuer != null) {
            if (issuer.contains(":")) {
                throw new IllegalArgumentException("Issuer cannot contain the ':' character.");
            }

            uri.setParameter("issuer", issuer);
        }
        final GoogleAuthenticatorConfig config = credentials.getConfig();
        uri.setParameter("algorithm", getAlgorithmName(config.getHmacHashFunction()));
        uri.setParameter("digits", String.valueOf(config.getCodeDigits()));
        uri.setParameter("period", String.valueOf((int) (config.getTimeStepSizeInMillis() / 1000)));
        return uri.toString();
    }

    private String getAlgorithmName(HmacHashFunction hashFunction) {
        return switch (hashFunction) {
            case HmacSHA1 -> "SHA1";
            case HmacSHA256 -> "SHA256";
            case HmacSHA512 -> "SHA512";
        };
    }

    /**
     * The label is used to identify which account a key is associated with.
     * It contains an account name, which is a URI-encoded string, optionally
     * prefixed by an issuer string identifying the provider or service managing
     * that account.  This issuer prefix can be used to prevent collisions
     * between different accounts with different providers that might be
     * identified using the same account name, e.g. the user's email address.
     * The issuer prefix and account name should be separated by a literal or
     * url-encoded colon, and optional spaces may precede the account name.
     * Neither issuer nor account name may themselves contain a colon.
     * Represented in ABNF according to RFC 5234:
     * <p>
     * label = accountname / issuer (“:” / “%3A”) *”%20” accountname
     *
     * @see <a href="https://code.google.com/p/google-authenticator/wiki/KeyUriFormat">Google Authenticator - KeyUriFormat</a>
     */
    private String formatLabel(String issuer, String accountName) {
        if (accountName == null || accountName.trim().length() == 0) {
            throw new IllegalArgumentException("Account name must not be empty.");
        }

        StringBuilder sb = new StringBuilder();

        if (issuer != null) {
            if (issuer.contains(":")) {
                throw new IllegalArgumentException("Issuer cannot contain the ':' character.");
            }

            sb.append(issuer);
            sb.append(":");
        }

        sb.append(accountName);

        return sb.toString();
    }
}
