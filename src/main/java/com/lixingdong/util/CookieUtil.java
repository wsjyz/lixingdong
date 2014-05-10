package com.lixingdong.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dam on 14-5-9.
 */
public class CookieUtil {

    public static void addCookie(HttpServletResponse response,String key,String value){
        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(60*60*24*7);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    public static String findCookie(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
        if(cookies != null){
            for(Cookie cookie : cookies){
                String name = cookie.getName();
                if(name != null && name.equals(key)){
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
