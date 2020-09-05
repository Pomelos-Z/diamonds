package com.diamonds.web.controller;

import com.diamonds.server.UserService;
import com.diamonds.server.request.UserLoginRequest;
import com.diamonds.server.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/logout")
    public String loginOut() {
        return Result.success(userService.logout());
    }

}
