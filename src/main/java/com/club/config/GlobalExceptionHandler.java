package com.club.config;


import com.club.common.exception.GlobalException;
import com.club.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * @author jacky
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> validateException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> list = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            list.add(error.getDefaultMessage());
        }
        return Result.fail("参数有误！" + list.get(0));
    }

    @ExceptionHandler(value = Exception.class)
    public Result<Object> exceptionHandle(final HttpServletRequest request, Exception e) {
        log.error("捕获异常：", e);
        return Result.fail("系统异常，请稍后再试！");
    }

    @ExceptionHandler(GlobalException.class)
    public Result<Object> exceptionHandler(GlobalException ex) {
        log.error(ex.getMessage());
        return Result.fail(ex.getMessage());
    }
}