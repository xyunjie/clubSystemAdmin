package com.club.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 * @author Accepted
 */
@Getter
@SuppressWarnings("all")
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    PARAM_ERROR( 202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    PASSWORD_IS_INCONSISTENT(205, "两次密码不一致"),

    LOGIN_EXPIRED(207, "登录过期"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),

    PASSWORD_ERROR(210, "密码错误"),
    LOGIN_DISABLED_ERROR(212, "该用户已被禁用"),
    REGISTER_MOBLE_ERROR(213, "手机号已被使用"),
    LOGIN_AURH(214, "需要登录"),
    LOGIN_ACL(215, "没有权限");


    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
