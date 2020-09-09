package com.diamonds.web.controller;

import com.diamonds.common.enums.SecKillingExceptionEnum;
import com.diamonds.common.exception.SecKillingException;
import com.diamonds.server.SecKillingService;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.response.Result;
import com.diamonds.web.access.Limit;
import com.diamonds.web.access.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/sec-killing")
public class SecKillingController {

    @Autowired
    private SecKillingService secKillingService;

    @Limit(seconds = 5, maxCount = 5)
    @PostMapping("/{path}")
    String secKilling(@PathVariable("path") String path, @RequestParam("goodsId") String goodsId) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            throw new SecKillingException(SecKillingExceptionEnum.SESSION_ERROR);
        }
        secKillingService.secKilling(path, goodsId, userInfoCache.getPhone());
        return Result.success();
    }

    @Limit(seconds = 5, maxCount = 5)
    @GetMapping("/result")
    String getSecKillingResult(@RequestParam("goodsId") String goodsId) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            throw new SecKillingException(SecKillingExceptionEnum.SESSION_ERROR);
        }
        return Result.success(secKillingService.getSecKillingResult(goodsId));
    }

    @Limit(seconds = 5, maxCount = 5)
    @GetMapping("/path")
    String getSecKillingPath(@RequestParam("goodsId") String goodsId, @RequestParam(value = "verifyCode") String verifyCode) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            throw new SecKillingException(SecKillingExceptionEnum.SESSION_ERROR);
        }

        return Result.success(secKillingService.getSecKillingPath(goodsId, verifyCode, userInfoCache.getPhone()));
    }

    @GetMapping("/verifyCode")
    String createVerifyCode(HttpServletResponse response) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            throw new SecKillingException(SecKillingExceptionEnum.SESSION_ERROR);
        }
        secKillingService.createVerifyCode(userInfoCache.getPhone(), response);
        return Result.success();
    }


}
