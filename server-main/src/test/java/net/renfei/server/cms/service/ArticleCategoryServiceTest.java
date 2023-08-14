package net.renfei.server.cms.service;

import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author renfei
 */
public class ArticleCategoryServiceTest extends ApplicationTest {
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Test
    public void queryAllChildCategoryId() {
        List<Long> longs = articleCategoryService.queryAllChildCategoryId(1L);
        System.out.println(longs);
    }
}
