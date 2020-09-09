package com.diamonds.server.impl;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.common.enums.SecKillingExceptionEnum;
import com.diamonds.common.exception.SecKillingException;
import com.diamonds.server.SecKillingService;
import com.diamonds.server.response.*;
import com.framework.service.lock.exception.LockException;
import com.framework.service.redis.RedisCache;
import com.framework.service.lock.RedisDistributedLockManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SecKillingServiceImpl implements SecKillingService, InitializingBean {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisDistributedLockManager redisDistributedLockManager;

    // 存放商品的是否售卖完毕的状态 卖完了为true
    private final ConcurrentHashMap<String, Boolean> localOverMap = new ConcurrentHashMap<>();

    @Override
    public void secKilling(String path, String goodsId, String userPhone) {
        // check path
        String cachePath = redisCache.getString(StrUtil.format(CacheKeyConstant.SEC_KILLING_USER_PATH_KEY, userPhone, goodsId));
        if (cachePath == null || !cachePath.equals(path)) {
            throw new SecKillingException(SecKillingExceptionEnum.GOODS_NOT_EXIST);
        }
        // order gen -> redis lock
        String orderId = null;
        try {
            orderId = redisDistributedLockManager.execute(StrUtil.format(CacheKeyConstant.SEC_KILLING_ORDER_LOCK_KEY, goodsId),
                    300L, 1000L, () -> orderPerHandle(goodsId, userPhone));
        } catch (LockException e) {
            throw new SecKillingException(SecKillingExceptionEnum.SEC_KILLING_FAIL);
        }
        // redis getAndDecrement
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            throw new SecKillingException(SecKillingExceptionEnum.SEC_KILLING_OVER);
        }
        long decrement = redisCache.getAndDecrement(StrUtil.format(CacheKeyConstant.SEC_KILLING_GOODS_KEY, goodsId));
        if (decrement < 1) {
            throw new SecKillingException(SecKillingExceptionEnum.SEC_KILLING_OVER);
        }
        // mq sender -> handle order


    }

    private String orderPerHandle(String goodsId, String userPhone) {
        // 订单初步生成
        String orderId = "xxx";
        return orderId;
    }

    @Override
    public SecKillingResultResponse getSecKillingResult(String goodsId) {
        // success then return order id -> success
        // sec-killing is over and not order id return -1 -> failed
        // sec-killing is not over and not order id return 0 -> neutral
        return null;
    }

    @Override
    public SecKillingPathResponse getSecKillingPath(String goodsId, String verifyCode, String userPhone) {
        // check verifyCode
        String cacheCode = redisCache.get(StrUtil.format(CacheKeyConstant.SEC_KILLING_VERIFY_CODE_KEY, userPhone));
        if (cacheCode == null || !cacheCode.equals(verifyCode)) {
            throw new SecKillingException(SecKillingExceptionEnum.CODE_FAIL);
        }
        redisCache.remove(StrUtil.format(CacheKeyConstant.SEC_KILLING_VERIFY_CODE_KEY, userPhone));
        // gen path -> save redis
        String path = UUID.randomUUID().toString().replace("-", "");
        redisCache.put(StrUtil.format(CacheKeyConstant.SEC_KILLING_USER_PATH_KEY, userPhone, goodsId), path);
        SecKillingPathResponse response = new SecKillingPathResponse();
        response.setPath(path);
        return response;
    }

    @Override
    public void createVerifyCode(String userPhone, HttpServletResponse response) {
        // Generate a verification code -> random
        String code = "example";
        // The code maybe image ? -> write code to response
        redisCache.put(StrUtil.format(CacheKeyConstant.SEC_KILLING_VERIFY_CODE_KEY, userPhone), code);
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
