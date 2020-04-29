package com.common.advice;


import com.alibaba.fastjson.JSONObject;
import com.common.bean.exception.IDGenerateException;
import com.common.constant.ResponseCode;
import com.common.bean.ExecuteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLSyntaxErrorException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Throwable.class)
    public Object handleException(Throwable e) {
        log.error("服务器异常",e);
        return JSONObject.toJSON(new ExecuteResult(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMessage()));
    }

    public Object handleSqlException(SQLSyntaxErrorException e) {
        log.error("SQL异常出现！",e);
        return JSONObject.toJSON(new ExecuteResult(ResponseCode.SQL_EXCEPTION.getCode(), ResponseCode.SQL_EXCEPTION.getMessage()));
    }

    public Object handleIdGenerateException(IDGenerateException e) {
        log.error("Id生成器异常！", e);
        return JSONObject.toJSON(new ExecuteResult(ResponseCode.ID_GENERATE_EXCEPTION.getCode(), ResponseCode.ID_GENERATE_EXCEPTION.getMessage()));
    }
}
