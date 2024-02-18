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
 * 财务明细
 *
 * @TableName t_finance_detail
 */
@TableName(value = "t_finance_detail")
@Data
public class FinanceDetail implements Serializable {
    /**
     * 资产明细
     */
    @TableId
    private Long id;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 用户加入社团关系ID
     */
    private Long clubUserMapId;

    /**
     * 操作人
     */
    private Long createdBy;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易后余额
     */
    private BigDecimal balance;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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