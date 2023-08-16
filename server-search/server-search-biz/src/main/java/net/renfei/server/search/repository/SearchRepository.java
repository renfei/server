package net.renfei.server.search.repository;

import net.renfei.server.search.api.entity.SearchItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author renfei
 */
public interface SearchRepository extends ElasticsearchRepository<SearchItem, String> {
}
