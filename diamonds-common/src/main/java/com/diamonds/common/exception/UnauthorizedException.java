package com.diamonds.common.exception;


import com.framework.common.exception.BaseException;

import java.text.MessageFormat;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public UnauthorizedException newInstance(String msgFormat, Object... args) {
        return new UnauthorizedException(MessageFormat.format(msgFormat, args));
    }
}
