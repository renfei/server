package net.renfei.server.main.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Swagger配置
 *
 * @author renfei
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "任霏博客与网站",
                version = "4.0",
                description = "任霏博客与网站",
                contact = @Contact(
                        name = "renfei",
                        email = "i@renfei.net",
                        url = "https://www.renfei.net"
                )
        ),
        security = @SecurityRequirement(name = "JWT")
)
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi coreGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Core")
                .pathsToMatch("/api/core/**")
                .build();
    }

    @Bean
    public GroupedOpenApi cmsGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("CMS")
                .pathsToMatch("/api/cms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Member")
                .pathsToMatch("/api/member/**")
                .build();
    }
}
