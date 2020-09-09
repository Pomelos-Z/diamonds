package com.diamonds.server.impl;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.server.SecKillingService;
import com.diamonds.server.response.Goods;
import com.diamonds.server.response.SecKillingPathResponse;
import com.diamonds.server.response.SecKillingResponse;
import com.diamonds.server.response.SecKillingResultResponse;
import com.framework.service.redis.RedisCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SecKillingServiceImpl implements SecKillingService, InitializingBean {

    @Autowired
    private RedisCache redisCache;

    // 存放商品的是否售卖完毕的状态 卖完了为true
    private final ConcurrentHashMap<String, Boolean> localOverMap = new ConcurrentHashMap<>();

    @Override
    public SecKillingResponse secKilling(String path, String goodsId) {
        SecKillingResponse response = new SecKillingResponse();
        // check path
        // order gen -> redis lock
        // redis getAndDecrement
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            response.setSuccess(false);
            response.setCode(HttpStatus.GONE.value());
            response.setMessage("Sold out.");
            return response;
        }
        long decrement = redisCache.getAndDecrement(StrUtil.format(CacheKeyConstant.SEC_KILLING_GOODS_KEY, goodsId));
        if (decrement < 1) {
            localOverMap.put(goodsId, true);
            response.setSuccess(false);
            response.setCode(HttpStatus.GONE.value());
            response.setMessage("Sold out.");
            return response;
        }
        // mq sender

        response.setSuccess(true);
        return response;
    }

    @Override
    public SecKillingResultResponse getSecKillingResult(String goodsId) {
        // success then return order id -> success
        // sec-killing is over and not order id return -1 -> failed
        // sec-killing is not over and not order id return 0 -> neutral
        return null;
    }

    @Override
    public SecKillingPathResponse getSecKillingPath(String goodsId, Integer verifyCode) {
        // check verifyCode
        // gen path -> save redis
        return null;
    }

    @Override
    public void createVerifyCode(HttpServletResponse response) {
        // Generate a verification code
        // The code maybe image ? -> write code to response
        // Verification code is used to apply for path
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // get all sec-killing goods here
        List<Goods> goodsList = new ArrayList<>();

        goodsList.forEach(goods -> {
            redisCache.put(StrUtil.format(CacheKeyConstant.SEC_KILLING_GOODS_KEY, goods.getId()), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        });
    }
}
