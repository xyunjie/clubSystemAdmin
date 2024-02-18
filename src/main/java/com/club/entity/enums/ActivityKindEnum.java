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
    WARINING("warning", "预警");

    private String value;
    private String text;

}
