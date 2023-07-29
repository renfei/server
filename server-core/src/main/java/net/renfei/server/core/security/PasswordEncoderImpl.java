package net.renfei.server.core.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.security.cipher.Sm3;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

/**
 * 密码加密实现
 *
 * @author renfei
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordEncoderImpl implements PasswordEncoder {
    private static final int SALT_BYTE_SIZE = 24;
    private static final int HASH_BYTE_SIZE = 18;
    private static final int PBKDF2_ITERATIONS = 18;
    private final ServerProperties properties;

    @Override
    public String encode(CharSequence rawPassword) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        if (properties.getEnableSM3()) {
            // 使用SM3加密密码
            String encrypt = Sm3.encrypt(rawPassword.toString() + toBase64(salt));
            int hashSize = encrypt.length();
            return "sm3:" + PBKDF2_ITERATIONS + ":" + hashSize + ":" + toBase64(salt) + ":" + encrypt;
        } else {
            // 使用SHA512加密密码
            byte[] hash = pbkdf2(rawPassword.toString().toCharArray(), salt,
                    PBKDF2_ITERATIONS, "PBKDF2WithHmacSHA512", HASH_BYTE_SIZE);
            int hashSize = hash.length;
            return "sha512:" + PBKDF2_ITERATIONS + ":" + hashSize + ":" + toBase64(salt) + ":" + toBase64(hash);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] params = encodedPassword.split(":");
        if (params.length != 5) {
            log.warn("Fields are missing from the password hash.");
            return false;
        } else {
            if ("sm3".equals(params[0])) {
                return Sm3.verify(rawPassword.toString() + params[3], params[4]);
            } else {
                int iterations;
                try {
                    iterations = Integer.parseInt(params[1]);
                } catch (NumberFormatException var11) {
                    log.error("Could not parse the iteration count as an integer.", var11);
                    return false;
                }
                if (iterations < 1) {
                    log.error("Invalid number of iterations. Must be >= 1.");
                    return false;
                } else {
                    byte[] salt;
                    try {
                        salt = fromBase64(params[3]);
                    } catch (IllegalArgumentException var10) {
                        log.error("Base64 decoding of salt failed.", var10);
                        return false;
                    }
                    byte[] hash;
                    try {
                        hash = fromBase64(params[4]);
                    } catch (IllegalArgumentException var9) {
                        log.error("Base64 decoding of pbkdf2 output failed.", var9);
                        return false;
                    }
                    int storedHashSize;
                    try {
                        storedHashSize = Integer.parseInt(params[2]);
                    } catch (NumberFormatException var8) {
                        log.error("Could not parse the hash size as an integer.", var8);
                        return false;
                    }
                    if (storedHashSize != hash.length) {
                        log.warn("Hash length doesn't match stored hash length.");
                        return false;
                    } else {
                        byte[] testHash;
                        if ("sha1".equals(params[0])) {
                            testHash = pbkdf2(rawPassword.toString().toCharArray(), salt, iterations, "PBKDF2WithHmacSHA1", hash.length);
                        } else {
                            testHash = pbkdf2(rawPassword.toString().toCharArray(), salt, iterations, "PBKDF2WithHmacSHA512", hash.length);
                        }
                        return slowEquals(hash, testHash);
                    }
                }
            }
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, String algorithm, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("Hash algorithm not supported.", var7);
        } catch (InvalidKeySpecException var8) {
            throw new RuntimeException("Invalid key spec.", var8);
        }
    }

    private static String toBase64(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }

    private static byte[] fromBase64(String hex) throws IllegalArgumentException {
        return Base64.getDecoder().decode(hex);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;

        for (int i = 0; i < a.length && i < b.length; ++i) {
            diff |= a[i] ^ b[i];
        }

        return diff == 0;
    }
}
