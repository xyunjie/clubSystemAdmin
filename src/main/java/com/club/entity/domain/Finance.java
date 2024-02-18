package com.club.entity.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 财务表
 * @TableName t_finance
 */
@TableName(value ="t_finance")
@Data
public class Finance implements Serializable {
    /**
     * 财务ID
     */
    @TableId
    private Long id;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 余额
     */
    private BigDecimal balance;

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