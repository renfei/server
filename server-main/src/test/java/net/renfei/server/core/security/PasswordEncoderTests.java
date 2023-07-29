package net.renfei.server.core.security;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加解密单元测试
 *
 * @author renfei
 */
@Slf4j
public class PasswordEncoderTests extends ApplicationTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ServerProperties serverProperties;

    @Test
    public void test() {
        log.info("-----< 密码加解密单元测试 >-----");
        serverProperties.setEnableSM3(true);
        log.info("-----< 国密SM3测试 >-----");
        String password = "password";
        String encode = passwordEncoder.encode(password);
        log.info("密码加密前：{}", password);
        log.info("密码加密后：{}", encode);
        boolean matches = passwordEncoder.matches(password, encode);
        log.info("密码认证：{}", matches);
        assert matches;
        log.info("-----< 国际SHA测试 >-----");
        serverProperties.setEnableSM3(false);
        encode = passwordEncoder.encode(password);
        log.info("密码加密前：{}", password);
        log.info("密码加密后：{}", encode);
        matches = passwordEncoder.matches(password, encode);
        log.info("密码认证：{}", matches);
        assert matches;
    }
}
