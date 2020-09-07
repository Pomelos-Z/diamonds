package com.diamonds.web.controller;

import com.diamonds.server.OrderService;
import com.diamonds.server.request.OrderRequest;
import com.diamonds.server.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public String order(@RequestBody OrderRequest request) {
        return Result.success(orderService.order(request));
    }

}
