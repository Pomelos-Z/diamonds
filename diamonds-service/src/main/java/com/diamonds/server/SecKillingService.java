package com.diamonds.server;

import com.diamonds.server.response.SecKillingPathResponse;
import com.diamonds.server.response.SecKillingResultResponse;

import javax.servlet.http.HttpServletResponse;

public interface SecKillingService {

    void secKilling(String path, String goodsId);

    SecKillingResultResponse getSecKillingResult(String goodsId);

    SecKillingPathResponse getSecKillingPath(String goodsId, Integer verifyCode);

    void createVerifyCode(HttpServletResponse response);
}
