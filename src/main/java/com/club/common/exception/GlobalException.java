package com.club.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @date 2024年02月07日
 */
@Slf4j
public class GlobalException extends RuntimeException {
    private final Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message
     * @param code
     */
    public GlobalException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     *
     * @param message
     */
    public GlobalException(String message) {
        super(message);
        this.code = 201;
    }

    @Override
    public String toString() {
        return "ThSystemException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
