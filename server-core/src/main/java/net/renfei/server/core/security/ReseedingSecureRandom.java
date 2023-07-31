package net.renfei.server.core.security;

import net.renfei.server.core.exception.GoogleAuthenticatorException;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author renfei
 */
public class ReseedingSecureRandom {
    private static final int MAX_OPERATIONS = 1_000_000;
    private final String provider;
    private final String algorithm;
    private final AtomicInteger count = new AtomicInteger(0);
    private volatile SecureRandom secureRandom;

    public ReseedingSecureRandom() {
        this.algorithm = null;
        this.provider = null;

        buildSecureRandom();
    }

    public ReseedingSecureRandom(String algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null.");
        }

        this.algorithm = algorithm;
        this.provider = null;

        buildSecureRandom();
    }

    public ReseedingSecureRandom(String algorithm, String provider) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null.");
        }

        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null.");
        }

        this.algorithm = algorithm;
        this.provider = provider;

        buildSecureRandom();
    }

    private void buildSecureRandom() {
        try {
            if (this.algorithm == null && this.provider == null) {
                this.secureRandom = new SecureRandom();
            } else if (this.provider == null) {
                this.secureRandom = SecureRandom.getInstance(this.algorithm);
            } else {
                assert this.algorithm != null;
                this.secureRandom = SecureRandom.getInstance(this.algorithm, this.provider);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new GoogleAuthenticatorException(
                    String.format(
                            "Could not initialise SecureRandom with the specified algorithm: %s. " +
                                    "Another provider can be chosen setting the -- system property.",
                            this.algorithm
                    ), e
            );
        } catch (NoSuchProviderException e) {
            throw new GoogleAuthenticatorException(
                    String.format(
                            "Could not initialise SecureRandom with the specified provider: %s. " +
                                    "Another provider can be chosen setting the -- system property.",
                            this.provider
                    ), e
            );
        }
    }

    public void nextBytes(byte[] bytes) {
        if (count.incrementAndGet() > MAX_OPERATIONS) {
            synchronized (this) {
                if (count.get() > MAX_OPERATIONS) {
                    buildSecureRandom();
                    count.set(0);
                }
            }
        }
        this.secureRandom.nextBytes(bytes);
    }
}
