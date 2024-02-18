package com.club.entity.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月07日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserLoginDto {
    @Schema(description = "账号")
    @NotNull(message = "账号不能为空")
    private String account;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;
}
