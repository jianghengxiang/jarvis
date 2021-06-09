package com.jhxstudio.jarvis.common.resolver;

import com.alibaba.fastjson.JSON;
import com.jhxstudio.jarvis.common.annotation.JsonParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jhx
 * @date 2019-08-07
 **/
@Component
public class JsonParamResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(String.class) && parameter.hasParameterAnnotation(JsonParam.class);

    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String body = getRequestBody(request);
        Object val = null;
        if (StringUtils.isEmpty(body)) {
            return null;
        }
        String key = parameter.getParameterAnnotation(JsonParam.class).value();
        if (StringUtils.isEmpty(key)) {
            key = parameter.getParameterName();
        }
        val = JSON.parseObject(body).get(key);
        return val;
    }


    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest.getAttribute(JSON_REQUEST_BODY);
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getInputStream(),"utf-8");
                servletRequest.setAttribute(JSON_REQUEST_BODY, jsonBody);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;

    }
}
