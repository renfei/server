package net.renfei.server.core.service;

import jakarta.validation.constraints.NotNull;
import net.renfei.server.core.entity.CloudflareDnsRecord;
import net.renfei.server.core.entity.CloudflareIpAccessRule;
import net.renfei.server.core.entity.CloudflareResponse;
import net.renfei.server.core.entity.CloudflareResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Cloudflare API V4
 *
 * @author renfei
 */
@FeignClient(name = "cloudflareApiV4", url = "https://api.cloudflare.com/client/v4")
public interface CloudflareApiV4 {
    /**
     * List, search, sort, and filter a zones' DNS records.
     *
     * @param zoneIdentifier 域名区域ID
     * @param name           名称
     * @param proxied        是否代理
     * @param type           记录类型
     * @param page           页码
     * @param perPage        每页容量
     * @return
     */
    @GetMapping("/zones/{zone_identifier}/dns_records")
    CloudflareResponses<CloudflareDnsRecord> listDnsRecords(@PathVariable("zone_identifier") @NotNull String zoneIdentifier,
                                                            @RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "type", required = false) String type,
                                                            @RequestParam(value = "proxied", required = false) Boolean proxied,
                                                            @RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "per_page", required = false) Integer perPage);

    @PostMapping("/zones/{zone_identifier}/dns_records")
    CloudflareResponse<CloudflareDnsRecord> createDnsRecord(@PathVariable("zone_identifier") @NotNull String zoneIdentifier,
                                                            @RequestBody CloudflareDnsRecord cloudflareDnsRecord);

    @PutMapping("/zones/{zone_identifier}/dns_records/{identifier}")
    CloudflareResponse<CloudflareDnsRecord> updateDnsRecord(@PathVariable("zone_identifier") @NotNull String zoneIdentifier,
                                                            @PathVariable("identifier") @NotNull String identifier,
                                                            @RequestBody CloudflareDnsRecord cloudflareDnsRecord);

    @DeleteMapping("/zones/{zone_identifier}/dns_records/{identifier}")
    CloudflareResponse<?> deleteDnsRecord(@PathVariable("zone_identifier") @NotNull String zoneIdentifier,
                                          @PathVariable("identifier") @NotNull String identifier);

    @GetMapping("/accounts/{account_identifier}/firewall/access_rules/rules")
    CloudflareResponses<CloudflareIpAccessRule> listIpAccessRules(@PathVariable("account_identifier") @NotNull String accountIdentifier);

    @PostMapping("/accounts/{account_identifier}/firewall/access_rules/rules")
    CloudflareResponse<CloudflareIpAccessRule> createIpAccessRules(@PathVariable("account_identifier") @NotNull String accountIdentifier,
                                                                   @RequestBody CloudflareIpAccessRule cloudflareIpAccessRule);

    @DeleteMapping("/accounts/{account_identifier}/firewall/access_rules/rules/{identifier}")
    CloudflareResponse<Map<String, String>> deleteIpAccessRules(@PathVariable("account_identifier") @NotNull String accountIdentifier,
                                                                @PathVariable("identifier") @NotNull String identifier);
}
