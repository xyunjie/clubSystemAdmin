package com.club.entity.dto.user;

import com.club.entity.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月09日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserAddOrUpdateDto {

    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;

    @Schema(description = "学号")
    @NotNull(message = "学号不能为空")
    private String studentId;

    @Schema(description = "姓名")
    @NotNull(message = "姓名不能为空")
    private String name;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(description = "手机号码")
    @NotNull(message = "手机号码不能为空")
    private String phone;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "性别")
    private Boolean sex;

    @Schema(description = "个人介绍")
    private String userInfo;

    @Schema(description = "qq")
    private String qq;

    @Schema(description = "微信账号")
    private String weChat;

    @Schema(description = "邮箱")
    private String mail;

    @Schema(description = "学院")
//    @NotNull(message = "学院不能为空")
    private Long college;

    @Schema(description = "专业")
//    @NotNull(message = "专业不能为空")
    private Long major;

    @Schema(description = "班级")
//    @NotNull(message = "班级不能为空")
    private Long clazz;

    @Schema(description = "入学年级")
    @NotNull(message = "入学年级不能为空")
    private Long grade;
}
