package com.humane.util.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MultiReadableHttpServletRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        MultiReadableHttpServletRequest multiReadRequest = new MultiReadableHttpServletRequest((HttpServletRequest) req);

        chain.doFilter(multiReadRequest, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}