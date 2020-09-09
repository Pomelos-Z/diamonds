package com.diamonds.server.impl;

import com.diamonds.server.SecKillingService;
import com.diamonds.server.response.SecKillingPathResponse;
import com.diamonds.server.response.SecKillingResultResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class SecKillingServiceImpl implements SecKillingService {
    @Override
    public void secKilling(String path, String goodsId) {

    }

    @Override
    public SecKillingResultResponse getSecKillingResult(String goodsId) {
        return null;
    }

    @Override
    public SecKillingPathResponse getSecKillingPath(String goodsId, Integer verifyCode) {
        return null;
    }

    @Override
    public void createVerifyCode(HttpServletResponse response) {

    }
}
