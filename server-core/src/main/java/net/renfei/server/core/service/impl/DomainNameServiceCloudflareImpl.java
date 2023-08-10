package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.DnsRecordTypeEnum;
import net.renfei.server.core.entity.CloudflareDnsRecord;
import net.renfei.server.core.entity.CloudflareResponses;
import net.renfei.server.core.entity.DnsRecord;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.CloudflareApiV4;
import net.renfei.server.core.service.DomainNameService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权威DNS服务Cloudflare实现
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application"
        , name = "defaultDomainNameService"
        , havingValue = "DomainNameServiceCloudflareImpl")
public class DomainNameServiceCloudflareImpl extends BaseService implements DomainNameService {
    private final CloudflareApiV4 cloudflareApiV4;

    @Override
    public ListData<DnsRecord> queryDomainRecords(String domainName, String name, DnsRecordTypeEnum recordType) {
        CloudflareResponses<CloudflareDnsRecord> responses =
                cloudflareApiV4.listDnsRecords(domainName, name, recordType.toString(), null, 1, 50000);
        ListData<DnsRecord> dnsRecordListData = new ListData<>();
        if (responses.getSuccess()) {
            List<CloudflareDnsRecord> results = responses.getResult();
            List<DnsRecord> dnsRecords = new ArrayList<>(results.size());
            results.forEach(result -> dnsRecords.add(DnsRecord.builder()
                    .id(result.getId())
                    .domainName(domainName)
                    .name(result.getName())
                    .type(DnsRecordTypeEnum.valueOf(result.getType()))
                    .content(result.getContent())
                    .alidnsLine(null)
                    .comment(result.getComment())
                    .ttl(result.getTtl())
                    .proxied(result.getProxied())
                    .build()));
            dnsRecordListData.setData(dnsRecords);
            dnsRecordListData.setTotal(results.size());
        } else {
            log.warn("cloudflareApiV4 queryDomainRecords 查询失败");
            dnsRecordListData.setData(new ArrayList<>());
            dnsRecordListData.setTotal(0);
        }
        dnsRecordListData.setPageNum(1);
        dnsRecordListData.setPages(1);
        dnsRecordListData.setPageSize(50000);
        return dnsRecordListData;
    }

    @Override
    public void createDomainRecord(DnsRecord dnsRecord) {
        cloudflareApiV4.createDnsRecord(dnsRecord.getDomainName(), CloudflareDnsRecord.builder()
                .name(dnsRecord.getName())
                .type(dnsRecord.getType().toString())
                .content(dnsRecord.getContent())
                .proxied(dnsRecord.getProxied())
                .comment(dnsRecord.getComment())
                .ttl(dnsRecord.getTtl())
                .build());
    }

    @Override
    public void updateDomainRecord(DnsRecord dnsRecord) {
        cloudflareApiV4.updateDnsRecord(dnsRecord.getDomainName(), dnsRecord.getId(),
                CloudflareDnsRecord.builder()
                        .name(dnsRecord.getName())
                        .type(dnsRecord.getType().toString())
                        .content(dnsRecord.getContent())
                        .proxied(dnsRecord.getProxied())
                        .comment(dnsRecord.getComment())
                        .ttl(dnsRecord.getTtl())
                        .build());
    }

    @Override
    public void deleteDomainRecord(String zoneId, String recordId) {
        cloudflareApiV4.deleteDnsRecord(zoneId, recordId);
    }
}
