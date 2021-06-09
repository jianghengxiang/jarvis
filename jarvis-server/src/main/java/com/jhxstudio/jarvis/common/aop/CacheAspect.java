package com.jhxstudio.jarvis.common.aop;

import com.alibaba.fastjson.JSON;
import com.jhxstudio.jarvis.common.utils.HttpContextUtils;
import com.jhxstudio.jarvis.common.utils.R;
import com.jhxstudio.jarvis.factory.cache.JarvisContentCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

/**
 * @author jhx
 * @date 2021/6/8
 **/
@Aspect
@Slf4j
@Order(1)
public class CacheAspect {


    private JarvisContentCache jarvisContentCache;

    public CacheAspect(JarvisContentCache cache) {
        log.info("初始化 -------- CacheAspect");
        this.jarvisContentCache = cache;
    }



    @Pointcut("@annotation(com.jhxstudio.jarvis.common.annotation.NeedCache)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void before(JoinPoint point) {
        log.info("测试before是在around之前还是之后");
    }

    @After("logPointCut()")
    public void after(JoinPoint point) {
        log.info("测试after的执行顺序");
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("正在执行 CacheAspect 环绕增强: {}",point.getSignature().getDeclaringTypeName());
        String cacheKey = "jarvis:content:cache:" + HttpContextUtils.getHttpServletRequest().getRequestURI() + "?"
                + HttpContextUtils.getHttpServletRequest().getQueryString();
        log.info("CacheAspect cacheKey is :{}", cacheKey);
        Object result = getCache(cacheKey);
        if (result != null) {
            log.info("-------- 获取到cache");
            return result;
        }
        try {
            log.info("-------- 未获取到cache,执行查询");
            result = point.proceed();
            setCache(cacheKey, result);
            return result;


        } catch (Exception e) {
            throw e;
        }
    }

    private Object getCache(String cacheKey) {
        try {
            String cache = jarvisContentCache.get(cacheKey);
            if (StringUtils.isNotEmpty(cache)) {
                return JSON.parseObject(cache).toJavaObject(R.class);
            }
        } catch (Exception e) {
            //todo 记录异常,并进行告警
            e.printStackTrace();
        }
        return null;
    }

    private void setCache(String cacheKey, Object result) {
        try {
            String cache = JSON.toJSONString(result);
            jarvisContentCache.set(cacheKey, cache);
         } catch (Exception e) {
            //todo 记录异常,并进行告警
            e.printStackTrace();
        }
    }

}
