package net.renfei.server.core.service;

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
}
