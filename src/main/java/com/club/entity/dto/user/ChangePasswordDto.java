package com.club.entity.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ChangePasswordDto {

    @Schema(description = "旧密码")
    private String oldPassword;

    @Schema(description = "新密码")
    private String newPassword;

    @Schema(description = "重复密码")
    private String confirmPassword;

}
