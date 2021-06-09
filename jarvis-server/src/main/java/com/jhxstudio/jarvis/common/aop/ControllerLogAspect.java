package com.jhxstudio.jarvis.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jhxstudio.jarvis.common.annotation.IgnoreParamLog;
import com.jhxstudio.jarvis.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;

/**
 * @author jhx
 */
@Component
@Aspect
@Slf4j
@Order(5)
public class ControllerLogAspect {

    @Pointcut("execution(* com.jhxstudio.jarvis.controller..*.*(..))")
    public void endpointAround() {}

    @Around(value = "endpointAround()")
    public Object endpointAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("正在执行 ---  ControllerLogAspect");
        Signature signature = pjp.getSignature();
        String methodDesc = pjp.getTarget().getClass().getSimpleName() + ":" + signature.getName();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
//        log.info("[ControllerLogAspect]:" + methodDesc);
        Object result;
        StringBuilder paramStrBuild = new StringBuilder("");
        String paramStr = "";
        //Get paramStr for logging
        Object[] params = pjp.getArgs();
        try {
            // I/O参数的忽略
            Boolean logFlag = !method.isAnnotationPresent(IgnoreParamLog.class) && ObjectUtils.isNotEmpty(params);
            if (logFlag) {
                if (ObjectUtils.isNotEmpty(params)) {
                    for (Object param : params) {
                        if (param == null) {
                            continue;
                        }
                        if (ObjectUtils.isBasicType(param)) {
                            paramStrBuild.append("||").append(param);
                        } else {
                            if (param instanceof ServletRequest || param instanceof ServletResponse || param instanceof MultipartFile) {
                                continue;
                            }
                            paramStrBuild.append("||").append(JSON.toJSONString(param, SerializerFeature.WriteMapNullValue));
                        }
                    }
                    if (paramStrBuild.length() > 2) {
                        paramStr = paramStrBuild.substring(2);
                    }
                }
            }
        } catch (Exception e) {
            log.error("[ControllerLogAspect]: JSON.toJSONString(model) error, {}", methodDesc, e);
        }

        Long startTime = System.currentTimeMillis();
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            Long endTime = System.currentTimeMillis();
            Long duration = endTime - startTime;
            log.error("[ControllerLogAspect]: invoke {} error, time cost {} ms, model: {}", methodDesc, duration, paramStr, e);
            //will be handled by JsonResponseExceptionHandler
            throw e;
        }
        Long endTime = System.currentTimeMillis();
        Long duration = endTime - startTime;
        String resultStr = "";
        try {
            resultStr = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            log.error("[ControllerLogAspect]: JSON.toJSONString(result) error, {}", methodDesc, e);
        }
        log.info("[ControllerLogAspect]: invoke {} success, time cost {} ms, model: {}, result: {}", methodDesc, duration, paramStr, resultStr);
        // RequestContextHolder.getRequestAttributes().getAttribute();
        return result;
    }
}
