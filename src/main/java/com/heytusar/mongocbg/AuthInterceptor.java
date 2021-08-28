package com.heytusar.mongocbg;

import com.heytusar.mongocbg.service.AuthService;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private Logger log =  LoggerFactory.getLogger(AuthInterceptor.class);
    private AuthService authService;

    AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle
        (HttpServletRequest request, HttpServletResponse response, Object handler) 
        throws Exception {
        log.info("preHandle called ------------------------------->");
        Boolean authStatus = false;
        Cookie[] cookies = request.getCookies();
        String part1 = "";
        String part2 = "";
        if (cookies != null && cookies.length >1) {
            for(Cookie c : cookies) {
                if(c.getName().equals("cookie1")) {
                    part1 = c.getValue();
                }
                else if(c.getName().equals("cookie2")) {
                    part2 = c.getValue();
                }
            }
            if(part1 != "" && part2 != "") {
                String bearer = part1 +"." + part2;
                Map result = authService.verifyAuth(bearer);
                authStatus = (Boolean) result.get("isValid");
                String userId = (String) result.get("userId");
                request.setAttribute("userId", userId);
            }
        }
        if(!authStatus) {
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\" : " + HttpServletResponse.SC_UNAUTHORIZED + "}");
            response.getWriter().flush();
            //response.sendRedirect("/signin");
        }
        log.info("preHandle authStatus ------------------------------->" + authStatus);
        return authStatus;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
        Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion
        (HttpServletRequest request, HttpServletResponse response, Object 
        handler, Exception exception) throws Exception {
    }
}