package net.renfei.server.core.service;

import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 对象存储服务测试
 *
 * @author renfei
 */
public class ObjectStorageServiceTests extends ApplicationTest {
    @Autowired
    private ObjectStorageService objectStorageService;

    @Test
    public void putObjectTest() {
        byte[] bytes = "Test".getBytes();
        objectStorageService.putObject("/Users/renfei/Downloads/test.txt", bytes);
    }
}
