package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.AliDnsAnswer;
import net.renfei.server.core.entity.CloudflareDnsAnswer;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 阿里DNS公共解析服务测试
 *
 * @author renfei
 */
@Slf4j
public class PublicDnsAnswerServiceTests extends ApplicationTest {
    @Autowired
    private AliPublicDnsService aliPublicDnsService;
    @Autowired
    private CloudflarePublicDnsService cloudflarePublicDnsService;

    @Test
    public void resolveTest() {
        AliDnsAnswer resolve = aliPublicDnsService.resolve("www.renfei.net", null, null, null, null);
        log.info("阿里 DNS:");
        for (AliDnsAnswer.Answer answer : resolve.getAnswer()) {
            log.info(answer.getData());
        }
        CloudflareDnsAnswer publicDnsAnswer = cloudflarePublicDnsService.dnsQuery("www.renfei.net", null, null, null);
        log.info("Cloudflare DNS:");
        for (CloudflareDnsAnswer.Answer answer : publicDnsAnswer.getAnswer()) {
            log.info(answer.getData());
        }
    }
}
