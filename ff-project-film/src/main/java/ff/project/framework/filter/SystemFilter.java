package ff.project.framework.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by F on 2017/6/15.
 */

public class SystemFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        String basePath = request.getContextPath();
        request.setAttribute("basePath",basePath);
        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
