package com.diamonds.server;

import com.diamonds.server.request.OrderRequest;
import com.diamonds.server.response.OrderResponse;

public interface OrderService {
    OrderResponse order(OrderRequest request);
}
