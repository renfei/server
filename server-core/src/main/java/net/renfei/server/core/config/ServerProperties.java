package net.renfei.server.core.config;

import lombok.Data;
import net.renfei.server.core.constant.SecretLevelEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置
 *
 * @author renfei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class ServerProperties {
    private String siteName;
    private String emailReplyTo;
    private Boolean enableCustomEncryption;
    private Boolean enableThreeMembers;
    private SecretLevelEnum systemMaxSecretLevel;
    private Jwt jwt;

    @Data
    public static class Jwt {
        private String secret;
        private Long tokenExpiration;
    }
}
