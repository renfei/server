package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.Duration;

/**
 * Redis服务测试
 *
 * @author renfei
 */
@Slf4j
public class RedisServiceTest extends ApplicationTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void test() {
        UserDetail userDetail = UserDetail.builder()
                .username("renfei")
                .password("test")
                .build();
        redisService.setValue("test", userDetail, Duration.ofMinutes(10));
        Serializable test = redisService.getValue("test");
        if (test instanceof UserDetail) {
            UserDetail testUser = (UserDetail) test;
            log.info("testUser: {}", testUser);
            redisService.delete("test");
        }
    }
}
