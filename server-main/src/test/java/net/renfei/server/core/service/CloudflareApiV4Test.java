package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.CloudflareDnsRecord;
import net.renfei.server.core.entity.CloudflareIpAccessRule;
import net.renfei.server.core.entity.CloudflareResponse;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Cloudflare API V4 测试
 *
 * @author renfei
 */
@Slf4j
public class CloudflareApiV4Test extends ApplicationTest {
    @Autowired
    private CloudflareApiV4 cloudflareApiV4;
    private final static String accountIdentifier = "16710c1a4cc36fdeec565057481140b7";
    private final static String zoneIdentifier = "772f42050bffce53e446ee33d1b996cf";

    @Test
    public void test() {
        List<CloudflareDnsRecord> result = cloudflareApiV4.listDnsRecords(zoneIdentifier,
                null, null, null, null, null).getResult();
        log.info(result.toString());
        CloudflareResponse<CloudflareDnsRecord> createResponse = cloudflareApiV4.createDnsRecord(zoneIdentifier
                , CloudflareDnsRecord.builder()
                        .name("unit-test")
                        .type("A")
                        .content("127.0.0.1")
                        .ttl(600L)
                        .build());
        CloudflareDnsRecord record = createResponse.getResult();
        log.info(record.toString());
        record.setContent("127.0.0.2");
        CloudflareResponse<CloudflareDnsRecord> updateResponse =
                cloudflareApiV4.updateDnsRecord(zoneIdentifier, record.getId(), record);
        log.info(updateResponse.getResult().toString());
        cloudflareApiV4.deleteDnsRecord(zoneIdentifier, record.getId());
    }

    @Test
    public void testIpAccessRoles() {
        String ip = "198.51.100.4";
        CloudflareResponse<CloudflareIpAccessRule> ipAccessRules =
                cloudflareApiV4.createIpAccessRules(accountIdentifier, CloudflareIpAccessRule.builder()
                        .configuration(CloudflareIpAccessRule.Configuration.builder()
                                .target("ip")
                                .value(ip)
                                .build())
                        .mode("challenge")
                        .notes("Unit Test")
                        .build());
        log.info(ipAccessRules.getResult().toString());
        List<CloudflareIpAccessRule> result = cloudflareApiV4.listIpAccessRules(accountIdentifier).getResult();
        for (CloudflareIpAccessRule rule : result) {
            log.info(rule.toString());
            if (rule.getConfiguration().getValue().equals(ip)) {
                Map<String, String> map = cloudflareApiV4.deleteIpAccessRules(accountIdentifier, rule.getId()).getResult();
                log.info(map.toString());
            }
        }
    }
}
