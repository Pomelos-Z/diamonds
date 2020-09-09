package com.diamonds.server.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class Result {

    private static final SerializeConfig SERIALIZE_CONFIG;

    private Integer code;
    private Boolean success;
    private String message;
    private Object data;


    static {
        SERIALIZE_CONFIG = new SerializeConfig();
        SERIALIZE_CONFIG.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
    }

    // 将返回值中为null，替换为空字符串
    public static String success(Object data) {
        Result result = new Result();
        result.setCode(HttpStatus.OK.value());
        result.setSuccess(Boolean.TRUE);
        result.setData(data);
        return JSONObject.toJSONString(result, SERIALIZE_CONFIG, SerializerFeature.WriteNullStringAsEmpty);
    }

    public static String success(Integer code, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setSuccess(Boolean.TRUE);
        result.setData(data);
        return JSONObject.toJSONString(result, SERIALIZE_CONFIG, SerializerFeature.WriteNullStringAsEmpty);
    }

    public static String success() {
        Result result = new Result();
        result.setCode(HttpStatus.OK.value());
        result.setSuccess(Boolean.TRUE);
        result.setData(new HashMap<>());
        return JSONObject.toJSONString(result, SERIALIZE_CONFIG, SerializerFeature.WriteNullStringAsEmpty);
    }

    public static String error(Integer code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setSuccess(Boolean.FALSE);
        result.setMessage(message);
        return JSONObject.toJSONString(result, SERIALIZE_CONFIG, SerializerFeature.WriteNullStringAsEmpty);
    }

    public static String error(HttpStatus status) {
        Result result = new Result();
        result.setCode(status.value());
        result.setSuccess(Boolean.FALSE);
        result.setMessage(status.getReasonPhrase());
        return JSONObject.toJSONString(result, SERIALIZE_CONFIG, SerializerFeature.WriteNullStringAsEmpty);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
