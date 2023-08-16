package net.renfei.server.search.api.service;

import net.renfei.server.core.entity.ListData;
import net.renfei.server.search.api.entity.SearchItem;
import net.renfei.server.search.api.entity.TypeEnum;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * 搜索服务
 *
 * @author renfei
 */
public interface SearchService {
    void createIndex(List<SearchItem> searchItemAll);

    void deleteIndex();

    ListData<SearchItem> search(String word, String pages, String rows);

    ListData<SearchItem> search(TypeEnum type, String word, String pages, String rows);

    ListData<SearchItem> search(TypeEnum type, Date startDate, Date endDate, String word, String pages, String rows);

    ListData<SearchItem> search(TypeEnum type, Long originalId, String pages, String rows);

    ListData<SearchItem> search(NativeQueryBuilder queryBuilder, String pages, String rows);
}
