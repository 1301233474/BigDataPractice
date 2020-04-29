package com.common.util.id;

public interface IdMeta {


    static int PORT_START_POS = 8;              //端口号范围，初始端口编号（包含） + 32 = 结束端口编号(不包含)

    static int SEQ_START_POS = 13;              //序列号长度10位，每毫秒最大产生1024个序列号

    static int TIMESTAMP_START_POS = 23;        //时间戳长度为41位，从起始时间开始，可用68年

    static long ID_START_TIME = 1571500800000L; //起始时间为2019-10-20 00:00:00的毫秒时间戳
}
