package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月15日
 */
@Getter
@AllArgsConstructor
public enum FinanceDetailStatusEnum {

    /**
     * 审核中
     */
    AUDITING(0, "审核中"),
    /**
     * 审核驳回
     */
    AUDIT_REJECT(1, "审核驳回"),
    /**
     * 审核通过
     */
    AUDIT_PASS(2, "审核通过"),
    ;

    private final Integer value;
    private final String text;

}
