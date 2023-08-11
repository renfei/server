package com.sankuai.inf.leaf.snowflake;

import com.google.common.base.Preconditions;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.common.Status;
import com.sankuai.inf.leaf.common.Utils;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Snowflake模式：基于 Zookeeper 实现
 * 算法取自twitter开源的snowflake算法。
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "application"
        , name = "defaultIDGen"
        , havingValue = "SnowflakeIDGenImpl")
public class SnowflakeIDGenImpl implements IDGen {
    private final long twepoch;
    private final long workerIdBits = 10L;
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private static final Random RANDOM = new Random();

    public SnowflakeIDGenImpl(ServerProperties properties) {
        this.twepoch = System.currentTimeMillis() - 10000;
        Preconditions.checkArgument(timeGen() > twepoch, "Snowflake not support twepoch gt currentTime");
        final String ip = Utils.getIp();
        SnowflakeZookeeperHolder holder = new SnowflakeZookeeperHolder(properties, ip, String.valueOf(properties.getZookeeper().getPort()), properties.getZookeeper().getAddress());
        log.info("twepoch:{} ,ip:{} ,zkAddress:{} port:{}", twepoch, ip, properties.getZookeeper().getAddress(), properties.getZookeeper().getPort());
        boolean initFlag = holder.init();
        if (initFlag) {
            workerId = holder.getWorkerID();
            log.info("START SUCCESS USE ZK WORKERID-{}", workerId);
        } else {
            Preconditions.checkArgument(initFlag, "Snowflake Id Gen is not init ok");
        }
        long maxWorkerId = ~(-1L << workerIdBits);
        Preconditions.checkArgument(workerId >= 0 && workerId <= maxWorkerId, "workerID must gte 0 and lte 1023");
    }

    @Override
    public Result get(String key) {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        return new Result(-1L, Status.EXCEPTION);
                    }
                } catch (InterruptedException e) {
                    log.error("wait interrupted");
                    return new Result(-2L, Status.EXCEPTION);
                }
            } else {
                return new Result(-3L, Status.EXCEPTION);
            }
        }
        // 最大能够分配的 workerid =1023
        long sequenceBits = 12L;
        if (lastTimestamp == timestamp) {
            long sequenceMask = ~(-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //seq 为0的时候表示是下一毫秒时间开始对seq做随机
                sequence = RANDOM.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果是新的ms开始
            sequence = RANDOM.nextInt(100);
        }
        lastTimestamp = timestamp;
        long timestampLeftShift = sequenceBits + workerIdBits;
        long id = ((timestamp - twepoch) << timestampLeftShift) | (workerId << sequenceBits) | sequence;
        return new Result(id, Status.SUCCESS);
    }

    @Override
    public boolean init() {
        return true;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
