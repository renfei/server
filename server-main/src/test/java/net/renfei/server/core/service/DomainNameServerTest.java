package net.renfei.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.DnsRecordTypeEnum;
import net.renfei.server.core.entity.DnsRecord;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 权威DNS服务
 *
 * @author renfei
 */
@Slf4j
public class DomainNameServerTest extends ApplicationTest {
    @Autowired
    @Qualifier("domainNameServiceAliyunImpl")
    private DomainNameService domainNameServiceAliyunImpl;

    @Test
    public void testAliyunImpl() {
        domainNameServiceAliyunImpl.createDomainRecord(DnsRecord.builder()
                .domainName("renfei.net")
                .name("unit-test")
                .type(DnsRecordTypeEnum.A)
                .content("127.0.0.1")
                .ttl(600L)
                .build());
        ListData<DnsRecord> listData = domainNameServiceAliyunImpl.queryDomainRecords("renfei.net", "unit-test", DnsRecordTypeEnum.A);
        DnsRecord dnsRecord = listData.getData().get(0);
        domainNameServiceAliyunImpl.deleteDomainRecord(dnsRecord.getId());
    }
}
