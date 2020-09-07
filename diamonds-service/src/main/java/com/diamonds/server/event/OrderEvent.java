package com.diamonds.server.event;

import com.diamonds.server.domin.UserInfoCache;
import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {

    private String message;
    private UserInfoCache userInfo;

    public OrderEvent(Object source, String message, UserInfoCache userInfo) {
        super(source);
        this.message = message;
        this.userInfo = userInfo;
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInfoCache getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoCache userInfo) {
        this.userInfo = userInfo;
    }
}
