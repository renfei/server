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
    private Boolean enableSM3;
    private Boolean allowConcurrentLogin;
    private Boolean enableCustomEncryption;
    private Boolean enableThreeMembers;
    private SecretLevelEnum systemMaxSecretLevel;
    private Jwt jwt;
    private Leaf leaf;
    private Zookeeper zookeeper;
    private Aliyun aliyun;

    @Data
    public static class Jwt {
        private String secret;
        private Long tokenExpiration;
    }

    @Data
    public static class Leaf {
        private String name;
    }

    @Data
    public static class Zookeeper {
        private String address;
        private Integer port;
    }

    @Data
    public static class Aliyun {
        private String accessKeyId;
        private String accessKeySecret;
        private Alidns alidns;

        @Data
        public static class Alidns {
            private String endpoint;
        }
    }
}
