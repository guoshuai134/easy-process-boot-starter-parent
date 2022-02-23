package com.shallow.universe.process.web;

import java.io.Serializable;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\10 0010
 */
public class ApiResult<T> implements Serializable {

    private T body;
    private String message;
    private Integer code;
    private Long total;

    public ApiResult() {
    }

    public ApiResult<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public ApiResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public ApiResult<T> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public ApiResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public Long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Result{" +
                "body=" + body +
                ", msg='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
