package com.diamonds.web.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SysConfig {

    // 考虑分布式配置框架 such as apollo

    // redis 配置
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.connect.timeout}")
    private Integer redisConnectTimeout;

    // token相关
    @Value("${user.token.security.key}")
    private String userTokenSecurityKey;
    @Value("${user.token.expiration.time}")
    private int userTokenExpirationTime;

    public String getUserTokenSecurityKey() {
        return userTokenSecurityKey;
    }

    public void setUserTokenSecurityKey(String userTokenSecurityKey) {
        this.userTokenSecurityKey = userTokenSecurityKey;
    }

    public int getUserTokenExpirationTime() {
        return userTokenExpirationTime;
    }

    public void setUserTokenExpirationTime(int userTokenExpirationTime) {
        this.userTokenExpirationTime = userTokenExpirationTime;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisConnectTimeout() {
        return redisConnectTimeout;
    }

    public void setRedisConnectTimeout(Integer redisConnectTimeout) {
        this.redisConnectTimeout = redisConnectTimeout;
    }
}
