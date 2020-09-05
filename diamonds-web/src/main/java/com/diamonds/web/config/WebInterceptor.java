package com.diamonds.web.config;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.AuthConstant;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.common.exception.UnauthorizedException;
import com.diamonds.server.domin.UserInfoCache;
import com.framework.service.redis.RedisCache;
import com.framework.web.auth.JwtToken;
import com.framework.web.auth.JwtTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class WebInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysConfig sysConfig;

    private static final List<String> UN_CHECK_COOKIE_URI = new ArrayList<>();

    static {
        // config读取
        UN_CHECK_COOKIE_URI.add("/user/login");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UN_CHECK_COOKIE_URI.contains(request.getRequestURI())) return true;

        String token = request.getHeader(AuthConstant.TOKEN);
        if (StringUtils.isBlank(token)) {
            throw new UnauthorizedException("cookie不存在,请重新登录");
        }
        String userId;
        try {
            JwtToken jwtToken = JwtTokenManager.getToken(sysConfig.getUserTokenSecurityKey(), token);
            userId = jwtToken.getUserId();
        } catch (Exception e) {
            throw new UnauthorizedException("无效请求,token解析失败");
        }
        if (userId == null) {
            return false;
        }
        UserInfoCache userInfoCache = redisCache.get(StrUtil.format(CacheKeyConstant.LOGIN_USER_INFO_KEY, userId));
        // 刷新token时间
        redisCache.put(StrUtil.format(CacheKeyConstant.LOGIN_USER_INFO_KEY, userId), userInfoCache, sysConfig.getUserTokenExpirationTime() * 24 * 60 * 60);
        return true;
    }

}
