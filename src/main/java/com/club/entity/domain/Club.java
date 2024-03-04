package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 社团表
 *
 * @TableName t_club
 */
@TableName(value = "t_club")
@Data
public class Club implements Serializable {
    /**
     * 社团ID
     */
    @TableId
    private Long id;

    /**
     * 社团名称
     */
    private String name;

    /**
     * 社团描述
     */
    private String description;

    /**
     * 加入需要的钱
     */
    private BigDecimal money;

    /**
     * 社团余额
     */
    private BigDecimal balance;

    /**
     * 社团状态，0-正常，1-注销
     */
    private Integer status;

    /**
     * 社团创建者
     */
    private Long createdBy;

    /**
     * 社团创建附件
     */
    private String appendix;

    /**
     * 社团浏览量
     */
    private Integer views;

    /**
     * 社团排序
     */
    private Integer sort;

    /**
     * 是否置顶
     */
    private Boolean top;

    /**
     * 社团创建时间
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