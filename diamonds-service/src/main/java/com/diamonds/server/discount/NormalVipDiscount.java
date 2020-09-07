package com.diamonds.server.discount;

import org.springframework.stereotype.Component;

@Component
public class NormalVipDiscount implements VipDiscount {
    @Override
    public Integer getVipLevel() {
        return 1;
    }

    @Override
    public String getDiscount() {
        return "0.90";
    }
}
