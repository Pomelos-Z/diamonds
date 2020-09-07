package com.diamonds.server.impl;

import com.diamonds.server.OrderService;
import com.diamonds.server.discount.VipDiscount;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.manager.WebManager;
import com.diamonds.server.request.OrderRequest;
import com.diamonds.server.response.OrderResponse;
import com.diamonds.server.utils.OrderSnGen;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private WebManager webManager;

    @Autowired
    private OrderSnGen orderSnGen;

    private final HashMap<Integer, VipDiscount> vipDiscountMap = new HashMap<>();

    public OrderServiceImpl(List<VipDiscount> discountList) {
        discountList.forEach(vipDiscount -> vipDiscountMap.put(vipDiscount.getVipLevel(), vipDiscount));
    }

    @Override
    public OrderResponse order(OrderRequest request) {
        // 事务处理 自定义事务处理配置 注解
        // 1、获取当前下单用户
        UserInfoCache currentUserInfo = webManager.getCurrentUserInfo();
        // 2、生成订单编号
        String orderSN = orderSnGen.generate(currentUserInfo.getUserId());
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

        OrderResponse response = new OrderResponse();
        response.setSuccess(true);
        return response;
    }
}
