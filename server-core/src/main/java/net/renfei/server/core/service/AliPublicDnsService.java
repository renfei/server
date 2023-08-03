package net.renfei.server.core.service;

import jakarta.validation.constraints.NotNull;
import net.renfei.server.core.entity.AliPublicDns;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 阿里DNS公共解析服务
 *
 * @author renfei
 */
@FeignClient(name = "aliPublicDnsService", url = "https://dns.alidns.com")
public interface AliPublicDnsService {
    @GetMapping("/resolve")
    AliPublicDns resolve(@RequestParam("name") @NotNull String name,
                         @RequestParam(value = "type", required = false) Integer type,
                         @RequestParam(value = "edns_client_subnet", required = false) String clientSubnet,
                         @RequestParam(value = "short", required = false) Boolean isShort,
                         @RequestParam(value = "uid", required = false) String uid);
}
