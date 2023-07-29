package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统服务测试
 *
 * @author renfei
 */
@Slf4j
public class SystemServiceTests extends ApplicationTest {
    @Autowired
    private SystemService systemService;

    @Test
    public void querySystemVersionInfoTest() {
        log.info(systemService.querySystemVersionInfo().toString());
    }
}
