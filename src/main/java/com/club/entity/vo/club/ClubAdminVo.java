package com.club.entity.vo.club;

import com.club.entity.vo.UserVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月15日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubAdminVo extends UserVo {

    @Schema(description = "社团信息")
    private ClubListVo clubListVo;

}
