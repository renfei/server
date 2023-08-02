package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.CloudflareDnsRecord;
import net.renfei.server.core.entity.CloudflareResponse;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Cloudflare API V4 测试
 *
 * @author renfei
 */
@Slf4j
public class CloudflareApiV4Test extends ApplicationTest {
    @Autowired
    private CloudflareApiV4 cloudflareApiV4;

    @Test
    public void test() {
        String zoneIdentifier = "772f42050bffce53e446ee33d1b996cf";
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
}
