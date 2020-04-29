package com.common.util.id;

public class Id {

    private long ip;            //ip地址最后一个小数点结尾，8位，

    private long port;          //端口号，从8000到9023，8位，注意APP只能使用该区间的端口号

    private long timestamp;     //毫秒时间戳

    private long sequence;      //序列号
}
