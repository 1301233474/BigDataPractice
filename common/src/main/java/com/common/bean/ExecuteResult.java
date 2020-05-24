package com.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.common.constant.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExecuteResult {

    private int code;

    private String message;

    private boolean success = false;

    private Object data;

    public ExecuteResult(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public ExecuteResult(Object data) {
        this(ResponseCode.OK.getCode(),ResponseCode.OK.getMessage());
        this.success = true;
        this.data = JSONObject.toJSON(data);
    }

    public ExecuteResult(boolean success,ResponseCode responseCode) {
        this(responseCode);
        this.success = success;
    }

    public ExecuteResult(boolean success,int code,String message) {
        this(code, message);
        this.success = success;
    }

    public static ExecuteResult failure(String message) {
        return new ExecuteResult(ResponseCode.FAILED.getCode(), message);
    }

    public static ExecuteResult failure() {
        return new ExecuteResult(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getMessage());
    }

    public static ExecuteResult success() {
        return new ExecuteResult(true,ResponseCode.OK);
    }

    public static ExecuteResult success(Object data) {
        return new ExecuteResult(data);
    }
}
