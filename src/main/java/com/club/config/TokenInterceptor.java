package com.club.config;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.club.common.result.Result;
import com.club.entity.domain.Token;
import com.club.entity.domain.User;
import com.club.mapper.TokenMapper;
import com.club.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 放行注解
        String token = request.getHeader("Token");
        Map<String, Object> map = new HashMap<>();
        // 过滤knife4j
        if (request.getRequestURI().contains("doc.html")) {
            return true;
        }
        // swagger
        if (request.getRequestURI().contains("/v3/api-docs")) {
            return true;
        }

        try {
            if (StringUtils.isEmpty(token)) {
                if (method.isAnnotationPresent(AllowAnonymous.class)) {
                    return true;
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权的请求");
                    send401ToResponse(response, "token为空");
                    return false;
                }
            } else {
                var tokenObj = new LambdaQueryChainWrapper<>(tokenMapper).eq(Token::getToken, token).one();
                if ((tokenObj == null || tokenObj.getExpireTime().isBefore(java.time.LocalDateTime.now()))) {
                    if (method.isAnnotationPresent(AllowAnonymous.class)) {
                        return true;
                    }
                    send401ToResponse(response, "token无效");
                    return false;
                }
                User userObj = new LambdaQueryChainWrapper<>(userMapper).eq(User::getId, tokenObj.getUserId()).one();
                if (userObj == null) {
                    if (method.isAnnotationPresent(AllowAnonymous.class)) {
                        return true;
                    }
                    send401ToResponse(response, "未查到该用户");
                    return false;
                }
                request.setAttribute("userId", userObj.getId());
            }
            return true;
        } catch (Exception e) {
            map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            map.put("msg", "认证失败请登陆");
            log.error(e.getMessage());
        }


        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(json.getBytes());
        outputStream.flush();
        outputStream.close();
        return false;
    }

    private void send401ToResponse(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");

        // 创建错误消息对象
        Result errorMessage = Result.fail("请先登录");

        // 将错误消息对象转换成 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorMessage = objectMapper.writeValueAsString(errorMessage);

        // 发送 JSON 错误消息给客户端
        response.getWriter().write(jsonErrorMessage);
    }
}
