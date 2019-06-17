package com.data4truth.pi.bean;

import java.io.Serializable;

/**
 * @author lindj
 * @date 2019/6/17 0017
 * @description
 */
public class BaseResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}


