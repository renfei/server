package net.renfei.server.core.service;

import net.renfei.server.core.entity.GoogleAuthenticatorKey;
import net.renfei.server.core.exception.GoogleAuthenticatorException;

/**
 * 谷歌两步认证
 *
 * @author renfei
 */
public interface GoogleAuthenticator {
    /**
     * This method generates a new set of credentials including:
     * <ol>
     * <li>Secret key.</li>
     * <li>Validation code.</li>
     * <li>A list of scratch codes.</li>
     * </ol>
     * <p>
     * The user must register this secret on their device.
     *
     * @return secret key
     */
    GoogleAuthenticatorKey createCredentials();

    /**
     * This method generates the current TOTP password.
     *
     * @param secret the encoded secret key.
     * @return the current TOTP password.
     * @since 1.1.0
     */
    int getTotpPassword(String secret);

    /**
     * This method generates the TOTP password at the specified time.
     *
     * @param secret The encoded secret key.
     * @param time   The time to use to calculate the password.
     * @return the TOTP password at the specified time.
     * @since 1.1.0
     */
    int getTotpPassword(String secret, long time);

    /**
     * Checks a verification code against a secret key using the current time.
     *
     * @param secret           the encoded secret key.
     * @param verificationCode the verification code.
     * @return <code>true</code> if the validation code is valid,
     * <code>false</code> otherwise.
     * @throws GoogleAuthenticatorException if a failure occurs during the
     *                                      calculation of the validation code.
     *                                      The only failures that should occur
     *                                      are related with the cryptographic
     *                                      functions provided by the JCE.
     * @see #authorize(String, int, long)
     */
    boolean authorize(String secret, int verificationCode);

    /**
     * Checks a verification code against a secret key using the specified time.
     * The algorithm also checks in a time window whose size determined by the
     * {@code windowSize} property of this class.
     * <p>
     * The default value of 30 seconds recommended by RFC 6238 is used for the
     * interval size.
     *
     * @param secret           The encoded secret key.
     * @param verificationCode The verification code.
     * @param time             The time to use to calculate the TOTP password..
     * @return {@code true} if the validation code is valid, {@code false}
     * otherwise.
     * @throws GoogleAuthenticatorException if a failure occurs during the
     *                                      calculation of the validation code.
     *                                      The only failures that should occur
     *                                      are related with the cryptographic
     *                                      functions provided by the JCE.
     * @since 0.6.0
     */
    boolean authorize(String secret, int verificationCode, long time);

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
    String getOtpAuthTotpURL(String issuer, String accountName, GoogleAuthenticatorKey credentials);
}
