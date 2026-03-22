package com.lgyf.demo.util;

import javax.servlet.http.HttpServletResponse;

public class Cookie {
    public static void  setCookie_redis_val(String key, String value, HttpServletResponse response) throws Exception {
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(key ,java.net.URLEncoder.encode(value, "UTF-8"));
        cookie.setPath("/");
        cookie.setMaxAge(999999999);
        response.addCookie(cookie);
    }

    public static void  setCookie_redis_key(String key, String value, HttpServletResponse response) throws Exception {
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(key ,java.net.URLEncoder.encode(value, "UTF-8"));
        cookie.setPath("/");
        cookie.setMaxAge(999999999);
        response.addCookie(cookie);
    }
}
