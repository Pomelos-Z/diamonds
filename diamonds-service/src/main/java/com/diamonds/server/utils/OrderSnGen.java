package com.diamonds.server.utils;

import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.common.constants.DataConstant;
import com.framework.service.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class OrderSnGen {

    @Autowired
    private RedisCache redisCache;

    // 简单使用redis实现 可考虑雪花算法
    public String generate(String str) {
        DateFormat df = new SimpleDateFormat(DataConstant.DATA_FORMAT);
        Calendar calendar = Calendar.getInstance();
        String data = df.format(calendar.getTime());
        long increment = redisCache.getAndIncrement(CacheKeyConstant.ORDER_SN_KEY, getTomorrowZeroSeconds());
        String orderSn = data + str + "%05d";
        return String.format(orderSn, increment);
    }


    private static long getTomorrowZeroSeconds() {
        long current = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (calendar.getTimeInMillis() - current) / 1000;
    }

}
