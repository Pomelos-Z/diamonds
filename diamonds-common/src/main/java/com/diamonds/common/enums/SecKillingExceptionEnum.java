package com.diamonds.common.enums;

public enum SecKillingExceptionEnum {

    REQUEST_ILLEGAL("10001", "访问太频繁!"),
    SESSION_ERROR("10002", "Session不存在或者已经失效!"),

    SEC_KILLING_OVER("20001", "商品已经秒杀完毕"),
    CODE_FAIL("20002", "验证码不一致!"),
    SEC_KILLING_FAIL("20003", "秒杀失败"),
    GOODS_NOT_EXIST("20004", "商品不存在"),

    ;

    private String code;
    private String message;

    SecKillingExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
