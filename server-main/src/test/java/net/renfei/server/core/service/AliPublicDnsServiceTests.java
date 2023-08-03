package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.AliPublicDns;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 阿里DNS公共解析服务测试
 *
 * @author renfei
 */
@Slf4j
public class AliPublicDnsServiceTests extends ApplicationTest {
    @Autowired
    private AliPublicDnsService aliPublicDnsService;

    @Test
    public void resolveTest() {
        AliPublicDns resolve = aliPublicDnsService.resolve("www.renfei.net", null, null, null, null);
        log.info(resolve.toString());
    }
}
