
package com.lgyf.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("all")
public class LoginFilter2  {

    static Logger logger = (Logger) LoggerFactory.getLogger(LoginFilter2.class);

    public  boolean isLoginUrl(HttpServletRequest request){
        String uri = request.getRequestURI();
        String base = request.getContextPath();
        if(uri.endsWith(base)||uri.endsWith(base+"/")||uri.endsWith("/to_login")){
            return true;
        }
        return false;
    }

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        request.getHeader("token");
//        //获取请求路径
//        String uri = request.getRequestURI();
////        logger.info("uri:"+uri);
//        //获取项目名
//        String base = request.getContextPath();
//
//        Cookie[] cookies = request.getCookies();
//        String userCode_cookie = null;
//
//        String userToken_cookie = null;
//
//        String mobile = null;
//        String is_salesman = null;
//        String salesman_no = null;
//
//        // 如果是登录页面，不拦截
//        if (uri.endsWith("/AgentRim/selByPwd") || uri.endsWith("/Customer/invite_register")
//                || uri.endsWith("/AgentRim/selBySms") || uri.endsWith("/AgentRim/to_login")|| uri.endsWith("/IDCardScan/checkIdcard")
//                || uri.endsWith("/AgentRim/register_user") || uri.endsWith("/AgentRim/to_name_certification")|| uri.endsWith("/AgentRim/selBankName")
//                || uri.endsWith("/AgentRim/insert_name_certification") || uri.endsWith("/AgentRim/to_binding_bankcard")
//                || uri.endsWith("/Agent/user_set_paypwd") || uri.endsWith("/Go/to_Scan") || uri.endsWith("/Go/to_down") || uri.endsWith("/public/file/app.apk")
//                || uri.endsWith("/Agent/user_set_loginpwd") || uri.endsWith("/Agent/user_update_pwd")|| uri.endsWith("/Agent/user_update_paypwd") || uri.endsWith("/sms_random/sendsms")
//                || uri.endsWith(".jsp") || uri.endsWith(".css")
//                || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpeg") || uri.endsWith(".jpg")
//                || uri.endsWith(".bmp") || uri.endsWith(".gif")) {
//            chain.doFilter(request, response);
//        } else {
//            // 不放行获取sessions
//            String userCode_sessions = (String) request.getSession().getAttribute("userCode");
////            logger.info("userCode_sessions:"+userCode_sessions);
//            String token_sessions = (String) request.getSession().getAttribute("userToken");
////            logger.info("token_sessions:"+token_sessions);
//            // 判断浏览器的cookie是否为空
//            if(cookies==null){ // 如果cookies为空，跳转到登录页
////                logger.info("cookies为null");
//                request.getRequestDispatcher("/AgentRim/to_login").forward(request, response);
//            } else {
//                // cookies不为空，遍历浏览器的cookies
//                for (Cookie cookie : cookies) {
//                    // 如果cookies中含有键为userCode,把值添加到 userCode_cookie
//                    if (cookie.getName().equals("userCode")) {
//                        userCode_cookie = cookie.getValue();
//                    }
//                    // 如果cookies中含有键为userToken,把值添加到 userToken_cookie
//                    if (cookie.getName().equals("userToken")) {
//                        userToken_cookie = cookie.getValue();
//                    }
//                    if (cookie.getName().equals("mobile")) {
//                        mobile = cookie.getValue();
//                    }
//                    if (cookie.getName().equals("is_salesman")) {
//                        is_salesman = cookie.getValue();
//                    }
//                    if (cookie.getName().equals("salesman_no")) {
//                        salesman_no = cookie.getValue();
//                    }
//                }
////                logger.info("userCode_cookie:"+userCode_cookie);
////                logger.info("userToken_cookie:"+userToken_cookie);
//                //???这里应该判断一下userCode_cookie 和 userToken_cookie 是否存在
//                if(userCode_cookie!=null&&userToken_cookie!=null&&is_salesman!=null&&salesman_no!=null) {
//                    // 如果sesiion的值不为空
//                    if(userCode_sessions!=null && token_sessions !=null&&!userCode_sessions.equals("null")&&!token_sessions.equals("null")){
//                        // 如果保存的sessions中的token不等于 redis中的token  退出并删除sessions 和清除cookie
//                        if(!userToken_cookie.equals(new RedisUtil().getJedis().get(userCode_cookie))){
//                            if(mobile!=null) {
//                                request.getSession().setAttribute("mobile",mobile);
//                            }
////                            logger.info("如果保存的sessions中的token不等于 redis中的token  退出并删除sessions 和清除cookie");
//                            request.getRequestDispatcher("/AgentRim/to_login").forward(request, response);
//                        }else{
//                            // 如果登录路径为/或to_login(登录页)则存sessions  跳转主页
//                            if(isLoginUrl(request)){
////                                logger.info("如果登录路径为/或to_login(登录页)则存sessions  跳转主页");
//                                request.getSession().setAttribute("userCode",userCode_cookie);
//                                request.getSession().setAttribute("userToken",userToken_cookie);
//                                request.getRequestDispatcher("/PolicyDetail/to_my_rate").forward(request, response);
//                            }else{
//                                // 否则放行
////                                logger.info("放行");
//                                chain.doFilter(request, response);
//                            }
//                        }
//                    } else { //sessions为空， 查询cookie并查询redis 存到sessions
//                        // 判断键是否存在redis库
//                        boolean havToken = new RedisUtil().getJedis().exists(userCode_cookie); // A00000001是否存在
////                        logger.info("userCode_cookie是否存在redis:"+havToken);
//                        if(!havToken){
////                            logger.info("userCode_cookie不存在，跳登录");
//                            request.getRequestDispatcher("/AgentRim/to_login").forward(request, response);
//                        } else {
//                            //如果缓存中存在，证明token有效
//                            // 获取token
//                            String userToken = new RedisUtil().getJedis().get(userCode_cookie);
////                            logger.info("缓存中存在，证明token有效，为："+userToken);
//                            // 判断redis的token与cookie的token是否相等
//                            if (userToken.equals(userToken_cookie)) {
//                                // 相等 存sessions
//                                request.getSession().setAttribute("userCode",userCode_cookie);
//                                request.getSession().setAttribute("userToken",userToken);
////                                logger.info("缓存中存在，证明token有效，为："+userToken);
//                                // 如果访问路径为/或to_login(登录页)则存sessions  跳转主页
//                                if(this.isLoginUrl(request)) {
//                                    request.getRequestDispatcher("/PolicyDetail/to_my_rate").forward(request, response);
//                                }else{
//                                    // 放行
//                                    chain.doFilter(request, response);
//                                }
//                            } else {
//                                if(mobile!=null) {
//                                    request.getSession().setAttribute("mobile",mobile);
//                                }
//                                request.getRequestDispatcher("/AgentRim/to_login").forward(request, response);
//                            }
//                        }
//                    }
//                } else {
//                    if(mobile!=null) {
//                        request.getSession().setAttribute("mobile",mobile);
//                    }
//                    request.getRequestDispatcher("/AgentRim/to_login").forward(request, response);
//                }
//            }
//        }
//    }
}


