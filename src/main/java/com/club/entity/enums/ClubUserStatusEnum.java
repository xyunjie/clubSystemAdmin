package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月15日
 */
@Getter
@AllArgsConstructor
public enum ClubUserStatusEnum {

    CLUB_CREATOR(-1, "社长"),
    CLUB_APPLY(0, "申请中"),
    CLUB_MEMBER(1, "社员/允许加入"),
    CLUB_REFUSE(2, "拒绝"),
    CLUB_EXIT(3, "退出"),
    CLUB_DISMISS(4, "解散");

    private final Integer value;
    private final String text;

    public static ClubUserStatusEnum getEnumByValue(Integer status) {
        for (ClubUserStatusEnum userStatusEnum : values()) {
            if (userStatusEnum.getValue().equals(status)) {
                return userStatusEnum;
            }
        }
        return null;
    }
}
