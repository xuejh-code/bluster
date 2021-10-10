package com.lin.missyou.core;

import com.lin.missyou.exception.CreateSuccess;
import com.lin.missyou.exception.DeleteSuccess;
import com.lin.missyou.exception.UpdateSuccess;

public class UnifyResponse{
    private int code;
    private String message;
    private String request;

    public UnifyResponse(int code,String message,String request){
        this.code = code;
        this.message = message;
        this.request = request;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "UnifyResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", request='" + request + '\'' +
                '}';
    }

    public static void createSuccess(int code){
        throw new CreateSuccess(code);
    }

    public static void updateSuccess(int code){
        throw new UpdateSuccess(code);
    }

    public static void deleteSuccess(int code){
        throw new DeleteSuccess(code);
    }
}
