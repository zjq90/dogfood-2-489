
package com.lgyf.demo.filter;

import com.lgyf.demo.controller.PublicController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("all")
@WebFilter
public class LoginFilter extends PublicController implements Filter {

    static Logger logger = (Logger) LoggerFactory.getLogger(LoginFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取请求路径
        String uri = request.getRequestURI();
        if (uri.endsWith("/Erleihu/send")) {
            chain.doFilter(request, response);
            return;
        }else{
            response.sendError(500,"错误请求链接地址！！！");
        }
    }
}


