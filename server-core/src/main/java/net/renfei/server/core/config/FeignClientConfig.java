package net.renfei.server.core.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 客户端配置
 *
 * @author renfei
 */
@Slf4j
@Data
@Configuration
@RequiredArgsConstructor
public class FeignClientConfig implements RequestInterceptor {
    private final static String CLOUDFLARE_API = "https://api.cloudflare.com";
    private final ServerProperties serverProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (requestTemplate.feignTarget().url().startsWith(CLOUDFLARE_API)) {
            // cloudflare api
            String authorization = "Bearer " + serverProperties.getCloudflare().getToken();
            requestTemplate.header("Authorization", authorization);
        }
    }
}