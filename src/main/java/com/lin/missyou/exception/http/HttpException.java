package com.lin.missyou.exception.http;


public class HttpException extends RuntimeException{
    //自定义的错误码
    protected Integer code;
    //http的状态码
    protected Integer httpStatusCode = 500;

    public Integer getCode() {
        return code;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
}