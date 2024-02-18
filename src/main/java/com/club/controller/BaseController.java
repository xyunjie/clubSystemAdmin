package com.club.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author jzx
 * @date 2024年02月07日
 */
public abstract class BaseController {

    @Resource
    protected HttpServletRequest request;

    public String getToken() {
         return request.getAttribute("token") == null ? null : request.getAttribute("token").toString();
     }

    public Long getUserId() {
        return request.getAttribute("userId") == null ? null : Long.parseLong(request.getAttribute("userId").toString());
    }

}
