package com.common.util.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author : 韩鹏飞
 * @Date : 2018/11/22 9:48 AM
 * @Description :
 **/
@Data
@Accessors(chain = true)
public class BaseToken {

    private String userId;
    private String username;
    private String deviceCode;
    private Long generateDate;
    private Long overdueTime = 86400000L;

    public BaseToken(Long overdueTimeMillions) {
        this.overdueTime = overdueTimeMillions;
    }
}
