package net.renfei.server.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger3 配置
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
public class Swagger3Config {
}
