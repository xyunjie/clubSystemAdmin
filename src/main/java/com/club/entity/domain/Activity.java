package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 社团公告/活动表
 *
 * @TableName t_activity
 */
@TableName(value = "t_activity")
@Data
public class Activity implements Serializable {
    /**
     * 公告id
     */
    @TableId
    private Long id;

    /**
     * 公告title
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告发布的社团
     */
    private Long clubId;

    /**
     * 类型
     */
    private String kind;

    /**
     * 公告浏览量
     */
    private Integer views;

    /**
     * 公告排序
     */
    private Integer sort;

    /**
     * 是否置顶
     */
    private Boolean top;

    /**
     * 公告状态
     */
    private Integer status;

    /**
     * 公告创建者
     */
    private Long createdBy;

    /**
     * 活动开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

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