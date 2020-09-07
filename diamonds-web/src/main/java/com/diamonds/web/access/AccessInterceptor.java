package com.diamonds.web.access;

import com.alibaba.fastjson.JSON;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.manager.WebManager;
import com.diamonds.server.response.Result;
import com.framework.service.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WebManager webManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            UserInfoCache userInfo = webManager.getCurrentUserInfo();
            Limit limit = handlerMethod.getMethodAnnotation(Limit.class);
            if (limit == null) {
                return true;
            }
            int seconds = limit.seconds();
            int maxCount = limit.maxCount();
            boolean needLogin = limit.needLogin();
            // 登录拦截器先于限流执行 一般不会到这里
            // 配置的不检查登录的接口会走到这里执行
            // 所以有个问题 如果一个不需要登录的页面限流此处就有一定问题
            // 因为不登录的限流需要采用其他的参数来限制而不是userInfo
            if (needLogin) {
                if (userInfo == null) {
                    render(response, HttpStatus.UNAUTHORIZED);
                    return false;
                }
            }
            // 放置于ThreadLocal
            UserContext.setUser(userInfo);

            // 使用user的phone来组成key
            String cacheKey = request.getRequestURI() + ":" + userInfo.getPhone();
            // 调用framework的方法 计数器自增+1 并返回计算前的原值
            // 如果key不存在则按当前值为0计算
            long increment = redisCache.getAndIncrement(cacheKey, seconds);
            if (increment > maxCount) {
                render(response, HttpStatus.TOO_MANY_REQUESTS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // remove
        UserContext.removeUser();
    }

    private void render(HttpServletResponse response, HttpStatus status) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(status.value(), status.getReasonPhrase()));
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

}
