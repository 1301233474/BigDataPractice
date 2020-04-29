package com.common.util.auth;

import com.alibaba.fastjson.JSONObject;
import com.common.util.MapUtils;
import com.common.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author : 韩鹏飞
 * @Date : 2018/11/21 4:16 PM
 * @Description :
 **/
@Slf4j
public class TokenUtils {

    public static final String SECRET = "bwcxljsm";


    /**  生成token   **/
    public static String generateToken(BaseToken token) {
        try {
            return Jwts.builder().signWith(SignatureAlgorithm.HS256, SECRET).setClaims(MapUtils.convertToMap(token)).compact();
        } catch (Exception e) {
            log.error("生成token失败！userId:{},userName:{}",token.getUserId());
            return null;
        }
    }


    /**  将token转为token对象   **/
    public static BaseToken translateToken(String tokenStr) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenStr);
        return JSONObject.parseObject(claims.getBody().toString().replace("=", ":"), BaseToken.class);
    }

    public static String getUsername(String tokenStr) {
        return translateToken(tokenStr).getUsername();
    }

    /**  判断token是否合法且有效      **/
    public static boolean validToken(String tokenStr) {
        try {
            if (StringUtils.isEmpty(tokenStr)) {
                log.info("token不能为空！");
                return false;
            }
            BaseToken token = translateToken(tokenStr);
            return new Date().getTime() < token.getGenerateDate() + token.getOverdueTime();
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
