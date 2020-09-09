package com.diamonds.common.exception;

import com.diamonds.common.enums.SecKillingExceptionEnum;
import com.framework.common.exception.BaseException;

import java.text.MessageFormat;

public class SecKillingException extends BaseException {

    public SecKillingException() {
        super();
    }

    public SecKillingException(String message) {
        super(message);
    }

    public SecKillingException(SecKillingExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    @Override
    public SecKillingException newInstance(String msgFormat, Object... args) {
        return new SecKillingException(MessageFormat.format(msgFormat, args));
    }
}
