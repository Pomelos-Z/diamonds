package com.diamonds.web.controller;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.server.SecKillingService;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.response.Goods;
import com.diamonds.server.response.Result;
import com.diamonds.web.access.Limit;
import com.diamonds.web.access.UserContext;
import com.framework.service.redis.RedisCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sec-killing")
public class SecKillingController implements InitializingBean {

    @Autowired
    private SecKillingService secKillingService;

    @Autowired
    private RedisCache redisCache;

    // 存放商品的是否售卖完毕的状态 卖完了为true
    private final HashMap<String, Boolean> localOverMap = new HashMap<String, Boolean>();

    @Limit(seconds = 5, maxCount = 5)
    @PostMapping("/{path}")
    String secKilling(@PathVariable("path") String path, @RequestParam("goodsId") String goodsId) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE);
        }
        // check path
        // order gen -> redis lock
        // redis getAndDecrement
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return Result.error(HttpStatus.GONE.value(), "Sold out.");
        }
        long decrement = redisCache.getAndDecrement(StrUtil.format(CacheKeyConstant.SEC_KILLING_GOODS_KEY, goodsId));
        if (decrement < 1) {
            localOverMap.put(goodsId, true);
            return Result.error(HttpStatus.GONE.value(), "Sold out.");
        }
        // mq sender
        return Result.success();
    }

    @Limit(seconds = 5, maxCount = 5)
    @GetMapping("/result")
    String getSecKillingResult(@RequestParam("goodsId") String goodsId) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE);
        }
        // success then return order id -> success
        // sec-killing is over and not order id return -1 -> failed
        // sec-killing is not over and not order id return 0 -> neutral
        return Result.success();
    }

    @Limit(seconds = 5, maxCount = 5)
    @GetMapping("/path")
    String getSecKillingPath(@RequestParam("goodsId") String goodsId, @RequestParam(value = "verifyCode") Integer verifyCode) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE);
        }
        // check verifyCode
        // gen path
        return Result.success(secKillingService.getSecKillingPath(goodsId, verifyCode));
    }

    @GetMapping("/verifyCode")
    String createVerifyCode(HttpServletResponse response) {
        // Generate a verification code
        // The code maybe image ?
        // Verification code is used to apply for path
        secKillingService.createVerifyCode(response);
        return Result.success();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // get all sec-killing goods here
        // List<Goods> goodsList = goodsService.listGoods();
        List<Goods> goodsList = new ArrayList<>();

        goodsList.forEach(goods -> {
            redisCache.put(StrUtil.format(CacheKeyConstant.SEC_KILLING_GOODS_KEY, goods.getId()), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        });
    }
}
