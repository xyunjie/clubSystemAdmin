package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月18日
 */
@Getter
@AllArgsConstructor
public enum ActivityKindEnum {

    NOTICE("notice", "通知"),
    ACTIVITY("activity", "活动"),
    WARNING("warning", "预警"),
    SYSTEM_NOTICE("system_notice", "系统公告"),
    ;

    private String value;
    private String text;

}
