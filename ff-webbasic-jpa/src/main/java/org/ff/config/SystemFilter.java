package org.ff.config;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 57257 on 2017/5/20.
 * 系統過濾器
 */

public class SystemFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String basePath = request.getContextPath();
        //全局变量
        request.setAttribute("basePath",basePath);
        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
