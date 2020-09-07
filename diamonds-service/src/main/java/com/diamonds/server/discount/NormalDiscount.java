package com.diamonds.server.discount;

import org.springframework.stereotype.Component;

@Component
public class NormalDiscount implements VipDiscount {
    @Override
    public Integer getVipLevel() {
        return 0;
    }

    @Override
    public String getDiscount() {
        return "1.00";
    }
}
