package com.humane.util.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * http://brantiffy.axisj.com/archives/451
 * https://github.com/brant-hwang/spring-logback-slack-notification-example
 * useage :
 * RequestWrapper requestWrapper = RequestWrapper.of(request);
 * <p>
 * log.info("uri : {}", requestWrapper.getRequestUri());
 * log.info("header : {}", requestWrapper.headerMap());
 * log.info("parameter : {}", requestWrapper.parameterMap());
 * log.info("body : {}", requestWrapper.getBody());
 * <p>
 * chain.doFilter(request, response);
 */
public class RequestWrapper {

    private HttpServletRequest request;

    private RequestWrapper(HttpServletRequest request) {
        this.request = request;
    }

    public static RequestWrapper of(HttpServletRequest request) {
        return new RequestWrapper(request);
    }

    public static RequestWrapper of(ServletRequest request) {
        return of((HttpServletRequest) request);
    }

    public Map<String, String> headerMap() {
        Map<String, String> convertedHeaderMap = new HashMap<>();

        Enumeration<String> headerMap = request.getHeaderNames();

        while (headerMap.hasMoreElements()) {
            String name = headerMap.nextElement();
            String value = request.getHeader(name);

            convertedHeaderMap.put(name, value);
        }
        return convertedHeaderMap;
    }

    public Map<String, String> parameterMap() {
        Map<String, String> convertedParameterMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (String key : parameterMap.keySet()) {
            String[] values = parameterMap.get(key);
            StringJoiner valueString = new StringJoiner(",");

            for (String value : values) {
                valueString.add(value);
            }

            convertedParameterMap.put(key, valueString.toString());
        }
        return convertedParameterMap;
    }

    public String getRequestUri() {
        return request.getRequestURI();
    }

    public String getBody() throws IOException {
        return IOUtils.toString(request.getReader());
    }
}