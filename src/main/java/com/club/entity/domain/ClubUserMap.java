package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 社团/角色映射表
 *
 * @TableName t_club_user_map
 */
@TableName(value = "t_club_user_map")
@Data
public class ClubUserMap implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 成员ID
     */
    private Long userId;

    /**
     * 加入状态
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