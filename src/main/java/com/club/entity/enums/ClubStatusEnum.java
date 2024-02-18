package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月10日
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public enum ClubStatusEnum {

    WAIT_AUDIT(0, "待审核"),

    PASS(1, "审核通过"),

    NO_PASS(2, "审核失败"),
    UPDATE_INFO(3, "修改信息待审核"),
    BAN(4, "封禁");

    private final Integer value;
    private final String text;

    public static ClubStatusEnum getEnum(Integer status) {
        for (ClubStatusEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static String getDesc(Integer status) {
        for (ClubStatusEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(status)) {
                return statusEnum.getText();
            }
        }
        return "状态错误";
    }
}
