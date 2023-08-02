package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

/**
 * Redis 服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl extends BaseService implements RedisService {
    private final RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public Boolean contain(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Serializable getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setValue(String key, Serializable value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }
}
