package com.example.yin.common;

import com.example.yin.constant.ResultEnum;
import lombok.Data;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/4 19:04
 **/
@Data
public class R {

    private int code;

    private String message;

    private String type;

    private Boolean success;

    private Object data;

    public static R success(String message) {
        R r = new R();
        r.setCode(ResultEnum.SUCCESS.getCode());
        r.setMessage(message);
        r.setSuccess(true);
        r.setType("success");
        r.setData(null);
        return r;
    }

    public static R success(String message, Object data) {
        R r = success(message);
        r.setData(data);
        return r;
    }

    public static R warning(String message) {
        R r = error(message);
        r.setType("warning");
        return r;
    }

    public static R error(String message) {
        R r = new R();
        r.setCode(ResultEnum.ERROR.getCode());
        r.setMessage(message);
        r.setSuccess(false);
        r.setType("error");
        return r;
    }

    public static R error(Integer code, String message) {
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        r.setSuccess(false);
        r.setType("error");
        r.setData(null);
        return r;
    }

    public static R fatal(String message) {
        R r = error(message);
        r.setType("fatal");
        return r;
    }
}
