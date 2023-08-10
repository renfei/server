package net.renfei.server.core.service.impl;

import com.aliyun.alidns20150109.models.*;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.constant.AlidnsLineEnum;
import net.renfei.server.core.constant.DnsRecordTypeEnum;
import net.renfei.server.core.entity.DnsRecord;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.service.DomainNameService;
import net.renfei.server.core.service.BaseService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.aliyun.alidns20150109.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权威DNS服务阿里云实现
 *
 * @author renfei
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "application"
        , name = "defaultDomainNameService"
        , havingValue = "DomainNameServiceAliyunImpl"
        , matchIfMissing = true)
public class DomainNameServiceAliyunImpl extends BaseService implements DomainNameService {
    private final Client client;

    public DomainNameServiceAliyunImpl(ServerProperties serverProperties) throws Exception {
        final Config config = new Config()
                .setAccessKeyId(serverProperties.getAliyun().getAccessKeyId())
                .setAccessKeySecret(serverProperties.getAliyun().getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Alidns
        config.endpoint = serverProperties.getAliyun().getAlidns().getEndpoint();
        this.client = new Client(config);
    }

    /**
     * 查询域名记录列表
     *
     * @param domainName 域名名称
     * @param name       名称
     * @param recordType 记录类型
     * @return
     */
    @Override
    public ListData<DnsRecord> queryDomainRecords(String domainName, String name, DnsRecordTypeEnum recordType) {
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        if (StringUtils.hasLength(domainName)) {
            request.domainName = domainName;
        }
        if (StringUtils.hasLength(name)) {
            request.RRKeyWord = name;
        }
        if (recordType != null) {
            request.type = recordType.toString();
        }
        ListData<DnsRecord> listData = new ListData<>();
        try {
            DescribeDomainRecordsResponse response = client.describeDomainRecords(request);
            DescribeDomainRecordsResponseBody body = response.getBody();
            DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecords domainRecords = body.getDomainRecords();
            List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> records = domainRecords.getRecord();
            List<DnsRecord> dnsRecords = new ArrayList<>(records.size());
            records.forEach(record -> dnsRecords.add(DnsRecord.builder()
                    .id(record.getRecordId())
                    .name(record.getRR())
                    .type(DnsRecordTypeEnum.valueOf(record.getType().toUpperCase()))
                    .content(record.getValue())
                    .alidnsLine(AlidnsLineEnum.valueOf(record.getLine().toUpperCase()))
                    .comment(record.getRemark())
                    .ttl(record.getTTL())
                    .proxied(false)
                    .build()));
            listData.setData(dnsRecords);
            listData.setPages(Math.toIntExact(response.getBody().getPageNumber()));
            listData.setTotal(response.getBody().getTotalCount());
            listData.setPageSize(Math.toIntExact(response.getBody().getPageSize()));
            return listData;
        } catch (Exception error) {
            log.error("error", error);
            listData.setData(new ArrayList<>());
            listData.setPages(0);
            listData.setTotal(0);
            listData.setPageSize(0);
            return listData;
        }
    }

    /**
     * 创建DNS记录
     *
     * @param dnsRecord DNS记录
     */
    @Override
    public void createDomainRecord(DnsRecord dnsRecord) {
        AddDomainRecordRequest request = new AddDomainRecordRequest();
        request.domainName = dnsRecord.getDomainName();
        request.RR = dnsRecord.getName();
        request.value = dnsRecord.getContent();
        request.type = dnsRecord.getType().toString();
        request.line = dnsRecord.getAlidnsLine() == null ? AlidnsLineEnum.DEFAULT.toString().toLowerCase()
                : dnsRecord.getAlidnsLine().toString().toLowerCase();
        request.TTL = dnsRecord.getTtl();
        try {
            client.addDomainRecord(request);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 更新DNS记录
     *
     * @param dnsRecord DNS记录
     */
    @Override
    public void updateDomainRecord(DnsRecord dnsRecord) {
        assert StringUtils.hasLength(dnsRecord.getId()) : "DNS记录ID不能为空";
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
        request.RR = dnsRecord.getName();
        request.recordId = dnsRecord.getId();
        request.value = dnsRecord.getContent();
        request.type = dnsRecord.getType().toString();
        request.line = dnsRecord.getAlidnsLine() == null ? AlidnsLineEnum.DEFAULT.toString().toLowerCase()
                : dnsRecord.getAlidnsLine().toString().toLowerCase();
        request.TTL = dnsRecord.getTtl();
        try {
            client.updateDomainRecord(request);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 删除DNS记录
     *
     * @param recordId DNS记录ID
     */
    @Override
    public void deleteDomainRecord(String zoneId, String recordId) {
        DeleteDomainRecordRequest request = new DeleteDomainRecordRequest();
        request.recordId = recordId;
        try {
            client.deleteDomainRecord(request);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
