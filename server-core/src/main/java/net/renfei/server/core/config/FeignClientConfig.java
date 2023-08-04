package net.renfei.server.core.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
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

    @Bean
    @ConditionalOnBean(FeignDnsJsonConverter.class)
    public Decoder feignDecoder(FeignDnsJsonConverter converter) {
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(converter);
        return new SpringDecoder(objectFactory);
    }
}