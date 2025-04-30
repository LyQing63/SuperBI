package io.lyqing64.github.superbi.common;

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String message;
    private T data;

    // 新增构造方法，支持传入 BusinessCode 枚举
    public static <T> Response<T> error(BusinessCode businessCode) {
        Response<T> response = new Response<>();
        response.setCode(businessCode.getCode());
        response.setMessage(businessCode.getMessage());
        return response;
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(data);
        return response;
    }

    public static <T> Response<T> error(int code, String message) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}