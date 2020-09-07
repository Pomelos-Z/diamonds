package com.diamonds.server.listener;

import com.diamonds.server.AsyncService;
import com.diamonds.server.event.OrderEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SmsListener implements ApplicationListener<OrderEvent>, AsyncService<OrderEvent> {

    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {
        // send message to phone
        this.handleAsync(orderEvent);
    }

    @Override
    @Async("asyncServiceExecutor")
    public void handleAsync(OrderEvent orderEvent) {
        String message = orderEvent.getMessage();
        String phoneNo = orderEvent.getUserInfo().getPhone();
        System.out.println("send " + message + " to " + phoneNo);
    }
}
