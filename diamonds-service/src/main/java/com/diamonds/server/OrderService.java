package com.diamonds.server;

import com.diamonds.server.domin.SecKillingOrderInfo;
import com.diamonds.server.request.OrderRequest;
import com.diamonds.server.response.OrderResponse;
import com.diamonds.server.response.SecKillingOrderResponse;

public interface OrderService {
    OrderResponse order(OrderRequest request);

    SecKillingOrderResponse secKillingOrder(String goodsId, String userPhone);

    SecKillingOrderInfo getSecKillingOrderByUserPhoneAndGoodsId(String goodsId, String userPhone);
}
