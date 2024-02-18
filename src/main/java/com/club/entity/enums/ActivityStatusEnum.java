package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月18日
 */
@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {

    WAIT_AUDIT(0, "待审核"),
    AUDIT_PASS(1, "审核通过"),
    AUDIT_FAIL(2, "审核未通过"),
    END(3, "已结束");

    private Integer value;
    private String text;

    public static ActivityStatusEnum getEnumByValue(Integer status) {
        for (ActivityStatusEnum activityStatusEnum : values()) {
            if (activityStatusEnum.getValue().equals(status)) {
                return activityStatusEnum;
            }
        }
        return null;
    }
}
