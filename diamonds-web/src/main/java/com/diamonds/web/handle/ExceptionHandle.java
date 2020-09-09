package com.diamonds.web.handle;

import com.diamonds.common.exception.SecKillingException;
import com.diamonds.common.exception.UnauthorizedException;
import com.diamonds.server.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ExceptionHandle {

    @ExceptionHandler(value = SecKillingException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleSecKilling(SecKillingException e) {
        return Result.error(Integer.valueOf(e.getCode()), e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String handleUnauthorized(UnauthorizedException e) {
        return Result.error(Integer.valueOf(e.getCode()), e.getMessage());
    }
}
