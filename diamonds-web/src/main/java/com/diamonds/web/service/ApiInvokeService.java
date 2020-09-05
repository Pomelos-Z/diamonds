package com.diamonds.web.service;

import com.framework.web.service.AbstractInvokeService;
import org.springframework.stereotype.Service;

@Service
public class ApiInvokeService extends AbstractInvokeService {

    @Override
    protected boolean checkPermission(String appId, String methodName) {
        return true;
    }

    @Override
    protected boolean checkSign(String content, String appId) {
        return true;
    }
}
