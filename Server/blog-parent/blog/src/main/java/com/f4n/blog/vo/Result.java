package com.f4n.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 *
 *  请求的返回格式
 *
 * */
@Data
@AllArgsConstructor
public class Result {
    private boolean success;
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object object) {
        return new Result(true, 200, "success", object);
    }

    public static Result fail(int code, String msg) {
        return new Result(false, code, msg, null);
    }
}
