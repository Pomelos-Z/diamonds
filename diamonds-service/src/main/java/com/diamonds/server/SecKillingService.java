package com.diamonds.server;

import com.diamonds.server.response.SecKillingPathResponse;
import com.diamonds.server.response.SecKillingResultResponse;

import javax.servlet.http.HttpServletResponse;

public interface SecKillingService {

    void secKilling(String path, String goodsId, String userPhone);

    SecKillingResultResponse getSecKillingResult(String goodsId,String userPhone);

    SecKillingPathResponse getSecKillingPath(String goodsId, String verifyCode, String userPhone);

    void createVerifyCode(String userPhone, HttpServletResponse response);
}
