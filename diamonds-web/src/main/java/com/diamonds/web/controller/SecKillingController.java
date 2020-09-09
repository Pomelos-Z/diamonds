package com.diamonds.web.controller;

import com.diamonds.server.SecKillingService;
import com.diamonds.server.domin.UserInfoCache;
import com.diamonds.server.response.Result;
import com.diamonds.server.response.SecKillingResponse;
import com.diamonds.web.access.Limit;
import com.diamonds.web.access.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            return Result.error(HttpStatus.NOT_ACCEPTABLE);
        }
        SecKillingResponse response = secKillingService.secKilling(path, goodsId);
        if (response.getSuccess()) {
            return Result.success();
        } else {
            return Result.error(response.getCode(), response.getMessage());
        }
    }

    @Limit(seconds = 5, maxCount = 5)
    @GetMapping("/result")
    String getSecKillingResult(@RequestParam("goodsId") String goodsId) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE);
        }
        return Result.success(secKillingService.getSecKillingResult(goodsId));
    }

    @Limit(seconds = 5, maxCount = 5)
    @GetMapping("/path")
    String getSecKillingPath(@RequestParam("goodsId") String goodsId, @RequestParam(value = "verifyCode") Integer verifyCode) {
        UserInfoCache userInfoCache = UserContext.getUser();
        if (userInfoCache == null) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE);
        }
        return Result.success(secKillingService.getSecKillingPath(goodsId, verifyCode));
    }

    @GetMapping("/verifyCode")
    String createVerifyCode(HttpServletResponse response) {
        secKillingService.createVerifyCode(response);
        return Result.success();
    }


}
