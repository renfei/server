package net.renfei.server.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author renfei
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "net.renfei.server.search.repository")
public class ElasticConfiguration {
}
