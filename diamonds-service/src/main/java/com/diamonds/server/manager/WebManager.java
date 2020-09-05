package com.diamonds.server.manager;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.AuthConstant;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.server.domin.UserInfoCache;
import com.framework.common.exception.CommonException;
import com.framework.service.redis.RedisCache;
import com.framework.web.auth.JwtToken;
import com.framework.web.auth.JwtTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;


@Component
public class WebManager {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private HttpServletRequest request;

    @Resource
    @Qualifier("userTokenSecurityKey")
    private String userTokenSecurityKey;

    public UserInfoCache getCurrentUserInfo() {
        Cookie[] cookies = request.getCookies();
        UserInfoCache userInfoCache = null;
        Optional<Cookie> optional = Arrays.stream(cookies).filter(cookie -> AuthConstant.TOKEN.equals(cookie.getName())).findFirst();
        if (!optional.isPresent()) {
            throw CommonException.INVALID_PARAM_ERROR;
        }
        String token = optional.get().getValue();
        JwtToken jwtToken = JwtTokenManager.getToken(userTokenSecurityKey, token);
        if (StringUtils.isNotEmpty(jwtToken.getUserId())) {
            String cacheKey = StrUtil.format(CacheKeyConstant.LOGIN_USER_INFO_KEY, jwtToken.getUserId());
            userInfoCache = redisCache.get(cacheKey);
        }
        return userInfoCache;
    }
}
