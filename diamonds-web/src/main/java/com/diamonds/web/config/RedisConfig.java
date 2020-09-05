package com.diamonds.web.config;

import com.framework.service.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Autowired
    private SysConfig sysConfig;

    @Bean
    public RedisCache redisCache() {
        RedisCache redisCache = new RedisCache();
        redisCache.setHost("redis://" + sysConfig.getRedisHost() + ":" + sysConfig.getRedisPort());
        redisCache.setPassword(sysConfig.getRedisPassword());
        redisCache.setConnectTimeout(sysConfig.getRedisConnectTimeout());
        return redisCache;
    }
}
