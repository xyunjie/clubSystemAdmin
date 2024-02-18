package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动精彩瞬间
 * @TableName t_exciting_moments
 */
@TableName(value ="t_exciting_moments")
@Data
public class ExcitingMoments implements Serializable {
    /**
     * 活动瞬间ID
     */
    @TableId
    private Long id;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 图片链接
     */
    private String url;

    /**
     * 创建者
     */
    private Long createdBy;

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