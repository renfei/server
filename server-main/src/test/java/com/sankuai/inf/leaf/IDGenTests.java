package com.sankuai.inf.leaf;

import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author renfei
 */
@Slf4j
public class IDGenTests extends ApplicationTest {
    @Autowired
    private SegmentIDGenImpl segmentIDGen;
    @Autowired
    private SnowflakeIDGenImpl snowflakeIDGen;

    @Test
    public void test() {
        Long id = segmentIDGen.get("leaf-segment-test").getId();
        log.info("segmentIDGen: {}", id);
        id = snowflakeIDGen.get("").getId();
        log.info("snowflakeIDGen: {}", id);
    }
}
