package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月09日
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    // 待审核
    WAIT_AUDIT(-1, "待审核"),
    // 审核通过
    NORMAL(0, "正常"),
    // 审核不通过
    AUDIT_FAIL(1, "审核不通过"),
    DISABLE(2, "禁用");

    private final Integer value;
    private final String text;

    public static UserStatusEnum getEnum(Integer status) {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.getValue().equals(status)) {
                return userStatusEnum;
            }
        }
        return null;
    }
}
