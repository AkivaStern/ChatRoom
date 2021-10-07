package com.example.ex4.filters;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Interceptor for routes needed to verify if the user is logged in correctly.
 */
public class LoggedInFilter implements HandlerInterceptor {

    /**
     * Constructor
     */
    public LoggedInFilter() {
    }

    /**
     * The prehandling of request to see if logged in
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

            if (request.getSession().getAttribute("loggedIn") == null) {
                response.sendRedirect("/");
                return false;
            }

            return true;
        }
}