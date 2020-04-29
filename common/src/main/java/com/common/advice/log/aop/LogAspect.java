package com.common.advice.log.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.util.RequestHolder;
import com.common.util.StringUtils;
import com.common.util.ThrowableUtil;
import com.common.util.auth.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/***
 * 切面输出方法调用的开始时间、结束时间等日志
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    /***
     * 第1个*代表任意返回类型
     * ..代表service包及其子包
     *第2个*代表任意类
     *第3个*代表任意方法
     * (..)表示任意数量的参数
     */
    @Pointcut("@annotation(com.common.advice.log.aop.Log)")
    public void log() {
    }

    /***
     * 前置通知：在某个连接点之前执行的通知，除非抛出一个异常，否则这个通知不能阻止连接点之前的执行流程
     * @param joinPoint
     */
//    @Before("log()")
    public void before(JoinPoint joinPoint) {
        log.info("方法{}执行前前，时间：{}", joinPoint.getSignature().toShortString(), new Date());
    }

    /***
     * 环绕通知
     * @param proceedingJoinPoint
     */
    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        String businessId = UUID.randomUUID().toString();
        Log aopLog = method.getAnnotation(Log.class);
        String methodName = proceedingJoinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        //用户外部请求，获取用户信息
        StringBuffer userInfo = new StringBuffer();;
        if (aopLog.userUsed()) {
            String token = RequestHolder.getHttpServletRequest().getHeader("Authorization");
            String username = TokenUtils.getUsername(token);
            userInfo.append(", username:");
            userInfo.append(username);
            String ip = StringUtils.getIP(RequestHolder.getHttpServletRequest());
            userInfo.append(", ip:");
            userInfo.append(ip);
            userInfo.append(", address:");
        }
        String description = aopLog.value();

        StringBuffer params = new StringBuffer();
        params.append("{");
        //参数值
        Object[] argValues = proceedingJoinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)proceedingJoinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]).append(";");
            }
        }
        params.append("}");

        log.info("{} start! methodName:{}, businessId:{} , params:{}," + userInfo.toString(), description, methodName, businessId, params);
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        log.info("{}end！ businessId:{},methodName:{} ，返回值:{} ，消耗时间:{} millis ",description, businessId, methodName, result, System.currentTimeMillis() - start);
        return result;
    }

    /***
     * 方法抛出异常通知
     * @param joinPoint
     */
    @AfterThrowing(pointcut = "log()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("方法{}抛出异常通知，结束时间：{},errorInfo:{}", joinPoint.getSignature().toShortString(), new Date(), ThrowableUtil.getStackTrace(e));
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        String SHORT_METHOD_NAME_SUFFIX = "(..)";
        if (methodName.endsWith(SHORT_METHOD_NAME_SUFFIX)) {
            methodName = methodName.substring(0, methodName.length() - SHORT_METHOD_NAME_SUFFIX.length());
        }
        return methodName;
    }

    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            //移除敏感内容
            String paramStr;
            if (arg instanceof HttpServletResponse) {
                paramStr = HttpServletResponse.class.getSimpleName();
            } else if (arg instanceof HttpServletRequest) {
                paramStr = HttpServletRequest.class.getSimpleName();
            } else if (arg instanceof MultipartFile) {
                long size = ((MultipartFile) arg).getSize();
                paramStr = MultipartFile.class.getSimpleName() + " size:" + size;
            } else if (arg.getClass().isArray()) {
                paramStr = JSONObject.toJSON(arg).toString();
            } else {
                paramStr = arg.toString();
            }
            sb.append(paramStr).append(",");
        }
        return sb.toString();
    }

    /**
     * 删除参数中的敏感内容
     * @param obj 参数对象
     * @return 去除敏感内容后的参数对象
     */
    private String deleteSensitiveContent(Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (obj == null || obj instanceof Exception) {
            return jsonObject.toJSONString();
        }

        try {
            String param = JSON.toJSONString(obj);
            jsonObject = JSONObject.parseObject(param);
            List<String> sensitiveFieldList = this.getSensitiveFieldList();
            for (String sensitiveField : sensitiveFieldList) {
                if (jsonObject.containsKey(sensitiveField)) {
                    jsonObject.put(sensitiveField, "******");
                }
            }
        } catch (ClassCastException e) {
            return String.valueOf(obj);
        }
        return jsonObject.toJSONString();
    }

    /**
     * 敏感字段列表（当然这里你可以更改为可配置的）
     */
    private List<String> getSensitiveFieldList() {
        List<String> sensitiveFieldList = new ArrayList<>();
        sensitiveFieldList.add("pwd");
        sensitiveFieldList.add("password");
        return sensitiveFieldList;
    }
}
