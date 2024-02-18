package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 字典表
 * @TableName t_dict
 */
@TableName(value ="t_dict")
@Data
public class Dict implements Serializable {
    /**
     * 字典ID
     */
    @TableId
    private Long id;

    /**
     * 字典父ID
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 年级（用于班级字典）
     */
    private Long grade;

    /**
     *  是否年级字典
     */
    private Boolean isGrade;

    /**
     * 字典描述
     */
    private String description;

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