package com.common.util.id;

import com.common.bean.exception.IDGenerateException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * id由四部分构成，分别是：
 *
 *     ip;            //ip地址最后一个小数点结尾，8位，
 *
 *     port;          //APP_ID,同名APP，ID相同，从0到1023，10位，注意APP只能使用该区间的端口号
 *
 *     timestamp;     //毫秒时间戳，31位
 *
 *     sequence;      //序列号，15位
 */
@Slf4j
public class IdUtils {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private static IdUtils idUtils = new IdUtils();

    private long beforeTimeStamp = 0;

    private IdUtils() {

    }

    public static IdUtils getInstance() {
        if (idUtils == null) {
            idUtils = new IdUtils();
        }
        return idUtils;
    }


    public long generateId(int appId) {
        if (appId < 0 || appId >= 1024) {
            throw new IDGenerateException("APP_ID范围：0(include) - 1024(exclude),当前APP_ID号：" + appId);
        }
        long id = 0;
        long ip = getIpEnd();
        id |= ip;
        id |= appId << IdMeta.PORT_START_POS;
        synchronized (this) {
            id |= generateSeq() << IdMeta.SEQ_START_POS;
            long currentTimeStamp = System.currentTimeMillis() - IdMeta.ID_START_TIME;
            if (currentTimeStamp > beforeTimeStamp) {
                id |= (currentTimeStamp) << IdMeta.TIMESTAMP_START_POS;
                beforeTimeStamp = currentTimeStamp;
            } else {
                Date now = new Date(currentTimeStamp + IdMeta.ID_START_TIME);
                Date before = new Date(beforeTimeStamp + IdMeta.ID_START_TIME);
                log.error("服务器时间倒流！上次时间：{},本次时间：{}",before,now);
                throw new IDGenerateException("服务器时间倒流！上次时间：" + before + ",本次时间：" + now);
            }

        }
        return id;
    }

    public long generateIdByMachine(int appId) {
        if (appId < 0 || appId >= 1024) {
            throw new IDGenerateException("APP_ID范围：0(include) - 1024(exclude),当前APP_ID号：" + appId);
        }
        long id = 0;
        long ip = getIpEnd();
        id |= ip;
        id |= appId << IdMeta.PORT_START_POS;
        return id;
    }



    /**
     * 获取IP小数点最后一位值,异常时，返回1
     * @return
     */
    private long getIpEnd() {
        String sIP = "127.0.0.1";
        InetAddress ip = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP)
                    break;
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
            if (null != ip)
                sIP = ip.getHostAddress();
        } catch (Exception e) {
            log.error("获取IP异常！");
            e.printStackTrace();
        }
        return Long.parseLong(sIP.split("\\.")[3]);
    }

    /**
     * seq最大长度为16位
     * @return
     */
    private int generateSeq() {
        int seq = atomicInteger.getAndIncrement();
        if (seq > Short.MAX_VALUE) {
            seq = 0;
            atomicInteger.set(seq);
        }
        return seq;
    }
}
