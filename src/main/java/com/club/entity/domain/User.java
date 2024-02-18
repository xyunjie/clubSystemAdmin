package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @TableName t_user
 */
@TableName(value = "t_user")
@Data
public class User implements Serializable {
    /**
     * 成员ID
     */
    @TableId
    private Long id;

    /**
     * 成员姓名
     */
    private String name;

    /**
     * 学号/登录账号
     */
    private String studentId;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Boolean sex;

    /**
     * 个人介绍
     */
    private String userInfo;

    /**
     * qq
     */
    private String qq;

    /**
     * 微信账号
     */
    private String weChat;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 学院
     */
    private Long college;

    /**
     * 专业
     */
    private Long major;

    /**
     * 班级
     */
    private Long clazz;

    /**
     * 入学年级
     */
    private Long grade;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 角色
     */
    private String role;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}