package net.renfei.server.core.service;

import jakarta.validation.constraints.NotNull;
import net.renfei.server.core.entity.CloudflareDnsAnswer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Cloudflare公共DNS解析服务
 *
 * @author renfei
 */
@FeignClient(name = "cloudflarePublicDnsService", url = "https://cloudflare-dns.com")
public interface CloudflarePublicDnsService {
    @GetMapping(value = "/dns-query", headers = {"Accept=application/dns-json"})
    @ResponseBody
    CloudflareDnsAnswer dnsQuery(@RequestParam("name") @NotNull String name,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "do", required = false) Boolean dnssec,
                                 @RequestParam(value = "cd", required = false) Boolean disableValidation);
}
