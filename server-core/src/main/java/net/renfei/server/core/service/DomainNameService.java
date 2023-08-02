package net.renfei.server.core.service;

import net.renfei.server.core.constant.DnsRecordTypeEnum;
import net.renfei.server.core.entity.DnsRecord;
import net.renfei.server.core.entity.ListData;

/**
 * 权威DNS服务
 *
 * @author renfei
 */
public interface DomainNameService {
    /**
     * 查询域名记录列表
     *
     * @param domainName 域名名称
     * @param name       名称
     * @param recordType 记录类型
     * @return
     */
    ListData<DnsRecord> queryDomainRecords(String domainName, String name, DnsRecordTypeEnum recordType);

    /**
     * 创建DNS记录
     *
     * @param dnsRecord DNS记录
     */
    void createDomainRecord(DnsRecord dnsRecord);

    /**
     * 更新DNS记录
     *
     * @param dnsRecord DNS记录
     */
    void updateDomainRecord(DnsRecord dnsRecord);

    /**
     * 删除DNS记录
     *
     * @param recordId DNS记录ID
     */
    void deleteDomainRecord(String recordId);
}
