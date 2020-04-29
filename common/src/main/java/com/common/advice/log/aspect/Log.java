package com.common.advice.log.aspect;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Data
@NoArgsConstructor
public class Log implements Serializable {

    private Long id;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 请求ip
     */
    private String requestIp;

    private String address;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 异常详细
     */
    private byte[] exceptionDetail;

    /**
     * 创建日期
     */
    private Date createTime;

    public Log(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
