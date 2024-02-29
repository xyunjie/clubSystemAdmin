package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统表
 * @date 2024/2/29 15:42
 * @TableName t_activity
 */
@TableName(value = "t_system")
@Data
public class System implements Serializable {

    /**
     * 配置ID
     */
    @TableId
    private Long id;

    /**
     * 关于我们
     */
    private String about;

    /**
     * 系统简介
     */
    private String introduction;

    /**
     * 社团模板文件
     */
    private Long clubTemplate;

    /**
     * 更新者
     */
    private String updatedBy;

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
