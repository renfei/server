package net.renfei.server.main;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * 单元测试
 *
 * @author renfei
 */
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApplicationTest {
}
