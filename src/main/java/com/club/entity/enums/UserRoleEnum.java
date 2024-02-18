package com.club.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @date 2024年02月11日
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {

    ADMIN("admin", "超级管理员"),
    USER("user", "普通用户"),
    CLUB_ADMIN("club_admin", "社团/组织管理员"),
    ;

    private final String value;
    private final String text;

}
