package net.renfei.server.core.service;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

/**
 * Redis 服务
 *
 * @author renfei
 */
public interface RedisService {
    /**
     * Redis Key 跟前缀
     */
    String REDIS_ROOT_KEY = "renfeid:";
    /**
     * Token认证的Redis缓存Key前缀
     */
    String AUTH_TOKEN_KEY = REDIS_ROOT_KEY + "auth:token:";
    /**
     * 两步验证TOTP的Redis缓存Key前缀
     */
    String AUTH_TOTP_KEY = REDIS_ROOT_KEY + "auth:totp:";

    /**
     * 判断缓存中是否存在
     *
     * @param key Key
     * @return
     */
    Boolean contain(String key);

    /**
     * 从缓存中获取单个值
     *
     * @param key Key
     * @return
     */
    Serializable getValue(String key);

    /**
     * 设置单个值缓存
     *
     * @param key     Key
     * @param value   值
     * @param timeout 缓存时间
     */
    void setValue(String key, Serializable value, Duration timeout);

    /**
     * 删除缓存
     *
     * @param key Key
     * @return
     */
    Boolean delete(String key);

    /**
     * 批量删除缓存
     *
     * @param keys Keys
     * @return
     */
    Long delete(Collection<String> keys);
}
