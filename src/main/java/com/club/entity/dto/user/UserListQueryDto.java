package com.club.entity.dto.user;

import com.club.entity.dto.base.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @date 2024年02月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserListQueryDto extends PageQuery {

    @Schema(description = "字典ID")
    private List<Long> dict;

    @Schema(description = "角色")
    private String role;

}
