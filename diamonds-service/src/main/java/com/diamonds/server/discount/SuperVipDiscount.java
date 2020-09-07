package com.diamonds.server.discount;

import org.springframework.stereotype.Component;

@Component
public class SuperVipDiscount implements VipDiscount {
    @Override
    public Integer getVipLevel() {
        return 2;
    }

    @Override
    public String getDiscount() {
        return "0.80";
    }
}
