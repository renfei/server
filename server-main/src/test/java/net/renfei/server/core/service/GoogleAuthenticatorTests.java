package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.GoogleAuthenticatorKey;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 谷歌两步认证
 *
 * @author renfei
 */
@Slf4j
public class GoogleAuthenticatorTests extends ApplicationTest {
    @Autowired
    private GoogleAuthenticator googleAuthenticator;

    @Test
    public void test() {
        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secretKey = key.getKey();
        log.info("secretKey: {}", secretKey);
        String otpAuthTotpURL = googleAuthenticator.getOtpAuthTotpURL("RENFEI.NET", "tester", key);
        log.info("otpAuthTotpURL: {}", otpAuthTotpURL);
        int code = googleAuthenticator.getTotpPassword(secretKey);
        log.info("TOTP code: {}", code);
        boolean isCodeValid = googleAuthenticator.authorize(secretKey, code);
        log.info("isCodeValid: {}", isCodeValid);
        assert isCodeValid;
    }
}
