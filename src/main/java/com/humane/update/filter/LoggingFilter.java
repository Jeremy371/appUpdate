package com.humane.update.filter;


import com.humane.util.filter.RequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;
import java.util.Map;

@Slf4j
/**
 * 소스 참조
 * http://brantiffy.axisj.com/archives/451
 * https://github.com/brant-hwang/spring-logback-slack-notification-example
 */
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = RequestWrapper.of(request);

        String uri = requestWrapper.getRequestUri();
        Map<String, String> headerMap = requestWrapper.headerMap();
        Map<String, String> parameterMap = requestWrapper.parameterMap();

        String body = requestWrapper.getBody();
        if (uri.matches("^(/)(api|score|check|data|model|setting|status)(/).*$")){
            log.info("uri : {}, header: {}, parameter : {}, body : {}", uri, headerMap, parameterMap, body);
        }

        /*log.info("uri : {}, parameter : {}, body : {}",
                requestWrapper.getRequestUri(),
                //requestWrapper.headerMap(),
                requestWrapper.parameterMap(),
                requestWrapper.getBody());*/

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}