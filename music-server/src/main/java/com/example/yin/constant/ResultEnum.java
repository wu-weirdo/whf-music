package com.example.yin.constant;

/**
 * 结果枚举
 *
 * @author whf
 * @date 2023/04/21
 */
public enum ResultEnum {

    SUCCESS(200, "请求成功"),
    ERROR(500, "请求失败"),
    PARAMETER_ERROR(1001, "请求参数有误!"),
    UNKNOWN_ERROR(9999, "未知的错误!");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}