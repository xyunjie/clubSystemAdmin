package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动用户映射
 *
 * @TableName t_activity_user_map
 */
@TableName(value = "t_activity_user_map")
@Data
public class ActivityUserMap implements Serializable {
    /**
     * 活动成员关系
     */
    @TableId
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 成员ID
     */
    private Long userId;

    /**
     * 审核状态
     */
    private Integer status;

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