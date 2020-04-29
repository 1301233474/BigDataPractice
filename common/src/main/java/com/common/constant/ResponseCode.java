package com.common.constant;

import lombok.Data;

public enum ResponseCode {

    OK(100, "成功"),
    OPERATION_SUCCESS(101, "操作成功"),
    OPERATION_TIMEOUT(102, "http请求超时"),

    SERVER_ERROR(500, "服务器内部错误"),
    PARAM_ERROR(510, "参数值不合法"),
    FAILED(511, "业务执行失败！"),
    SQL_EXCEPTION(512, "数据库异常！"),
    TOKEN_OUT_OF_DATE(513, "Token已过期"),
    NO_DATA(514, "无相关数据"),
    ID_GENERATE_EXCEPTION(515, "ID生成器异常！"),

    ;
    private int code;

    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
