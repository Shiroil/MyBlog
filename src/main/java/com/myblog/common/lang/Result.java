package com.myblog.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data){
        return msg_return(200, "成功", data);
    }

    public static Result fail(String msg){
        return msg_return(400, msg, null);
    }

    public static Result msg_return(int code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
