package net.renfei.server.core.security.cipher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 国产密码算法-商用密码SM3-单元测试
 *
 * @author renfei
 */
@Slf4j
public class Sm3Tests {
    @Test
    public void test() {
        String text = "password";
        String encrypt = Sm3.encrypt(text);
        log.info("{} encrypt: {}", text, encrypt);
        boolean verify = Sm3.verify(text, encrypt);
        log.info("verify: {}", verify);
        assert verify;
    }
}
