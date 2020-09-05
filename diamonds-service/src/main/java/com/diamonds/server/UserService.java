package com.diamonds.server;

import com.diamonds.server.request.UserLoginRequest;
import com.diamonds.server.response.UserLoginResponse;
import com.diamonds.server.response.UserLogoutResponse;

public interface UserService {

    UserLoginResponse login(UserLoginRequest request);

    UserLogoutResponse logout();

}
