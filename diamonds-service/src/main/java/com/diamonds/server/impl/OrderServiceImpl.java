package com.diamonds.server.impl;

import cn.hutool.core.util.StrUtil;
import com.diamonds.common.constants.CacheKeyConstant;
import com.diamonds.server.OrderService;
import com.diamonds.server.dao.OrderDao;
import com.diamonds.server.discount.VipDiscount;
import com.diamonds.server.domin.OrderInfo;
import com.diamonds.server.domin.SecKillingOrderInfo;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.event.OrderEvent;
import com.diamonds.server.manager.WebManager;
import com.diamonds.server.request.OrderRequest;
import com.diamonds.server.response.OrderResponse;
import com.diamonds.server.response.SecKillingOrderResponse;
import com.diamonds.server.utils.OrderSnGen;
import com.framework.service.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private WebManager webManager;

    @Autowired
    private OrderSnGen orderSnGen;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ApplicationContext applicationContext;

    private final HashMap<Integer, VipDiscount> vipDiscountMap = new HashMap<>();

    public OrderServiceImpl(List<VipDiscount> discountList) {
        discountList.forEach(vipDiscount -> vipDiscountMap.put(vipDiscount.getVipLevel(), vipDiscount));
    }

    @Override
    @Transactional
    public OrderResponse order(OrderRequest request) {
        // 事务处理 自定义事务处理配置 注解
        // 1、获取当前下单用户
        UserInfoCache currentUserInfo = webManager.getCurrentUserInfo();
        // 2、生成订单编号
        String orderSN = orderSnGen.generate(currentUserInfo.getPhone().substring(7));
        // 3、保存订单基本信息数据

        // 4、获取商品数据
        List<String> goodsIdList = request.getGoodsIdList();
        // 5、遍历商品下单
        //    获取用户折扣 -> 判断库存 -> 减少库存 增加销量 -> 保存订单
        String discount = vipDiscountMap.get(currentUserInfo.getVipLevel()).getDiscount();
        goodsIdList.forEach(goodsId -> {
            // 乐观锁 or 队列 秒杀系列的这里暂不考虑 秒杀系列在另外的controller中处理
        });
        // 6、成功后的邮件or推送or短信
        applicationContext.publishEvent(new OrderEvent(this, "message", currentUserInfo));

        OrderResponse response = new OrderResponse();
        response.setSuccess(true);
        return response;
    }

    @Override
    @Transactional
    public SecKillingOrderResponse secKillingOrder(String goodsId, String userPhone) {
        OrderInfo orderInfo = new OrderInfo();
        // 手机号截取后四位
        orderInfo.setOrderSn(orderSnGen.generate(userPhone.substring(7)));
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsId);
        orderInfo.setStatus(0);
        orderInfo.setUserId(userPhone);
        orderInfo.setOrderChannel(1);
        orderDao.insert(orderInfo);
        SecKillingOrderInfo secKillingOrderInfo = new SecKillingOrderInfo();
        secKillingOrderInfo.setGoodsId(goodsId);
        secKillingOrderInfo.setOrderSn(orderInfo.getOrderSn());
        secKillingOrderInfo.setUserId(userPhone);
        orderDao.insertSecKillingOrder(secKillingOrderInfo);
        // cache
        redisCache.put(StrUtil.format(CacheKeyConstant.SEC_KILLING_ORDER_BY_GOODS_ID_AND_USER_PHONE_KEY, goodsId, userPhone), secKillingOrderInfo);
        SecKillingOrderResponse response = new SecKillingOrderResponse();
        response.setOrderSn(orderInfo.getOrderSn());
        return response;
    }

    @Override
    public SecKillingOrderInfo getSecKillingOrderByUserPhoneAndGoodsId(String goodsId, String userPhone) {
        return redisCache.get(StrUtil.format(CacheKeyConstant.SEC_KILLING_ORDER_BY_GOODS_ID_AND_USER_PHONE_KEY, goodsId, userPhone));
    }
}
