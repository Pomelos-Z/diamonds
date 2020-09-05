package com.diamonds.server.impl;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.server.UserService;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.manager.WebManager;
import com.diamonds.server.request.UserLoginRequest;
import com.diamonds.server.response.UserLoginResponse;
import com.diamonds.server.response.UserLogoutResponse;
import com.framework.service.redis.RedisCache;
import com.framework.web.auth.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WebManager webManager;

    @Autowired
    private RedisCache redisCache;

    @Resource
    @Qualifier("userTokenExpirationTime")
    private int userTokenExpirationTime;

    @Resource
    @Qualifier("userTokenSecurityKey")
    private String userTokenSecurityKey;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        // get user info from database by userName
        // 查不到userinfo -> 抛异常

        // 前端传递的request中的pwd为MD5之后的pwd -> MD5(pwd)
        // database中的pwd为MD5之后加盐再次MD5 -> MD5(MD5(pwd) + salt)
        // request.getPassword()加盐MD5之后和数据库对比 salt存在数据库
        // 密码错误 -> 抛异常

        // 验证通过 允许多点登录 将当前登录设备id

        String token = JwtTokenManager.generateToken("userId", userTokenSecurityKey, userTokenExpirationTime * 24 * 60);

        UserInfoCache cache = new UserInfoCache();
        // cache.set
        redisCache.put(StrUtil.format(CacheKeyConstant.LOGIN_USER_INFO_KEY, "userId"), cache, userTokenExpirationTime * 24 * 60 * 60);

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setAvatar("avatarUrl");
        userLoginResponse.setNickName("nickName");
        userLoginResponse.setToken(token);
        userLoginResponse.setUserName(request.getUserName());
        return userLoginResponse;
    }

    @Override
    public UserLogoutResponse logout() {
        UserInfoCache currentUser = webManager.getCurrentUserInfo();
        redisCache.remove(StrUtil.format(CacheKeyConstant.LOGIN_USER_INFO_KEY, currentUser.getUserId()));
        return new UserLogoutResponse();
    }
}
