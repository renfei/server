package net.renfei.server.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加支持 application/dns-json 的转换器声明
 *
 * @author renfei
 */
@Slf4j
@Configuration
public class FeignDnsJsonConverter extends MappingJackson2HttpMessageConverter {
    public FeignDnsJsonConverter() {
        // 先将原先支持的MediaType列表拷出
        List<MediaType> mediaTypeList = new ArrayList<>(this.getSupportedMediaTypes());
        //加入对text/plain的支持
        mediaTypeList.add(new MediaType("application", "dns-json"));
        //将已经加入了text/json的MediaType支持列表设置为其支持的媒体类型列表
        this.setSupportedMediaTypes(mediaTypeList);
    }
}
