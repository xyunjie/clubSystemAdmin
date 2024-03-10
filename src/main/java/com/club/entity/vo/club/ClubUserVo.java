package com.club.entity.vo.club;

import com.club.entity.vo.UserVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @date 2024年02月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubUserVo extends UserVo {

    @Schema(description = "加入社团的状态,-1-社团创建者, 0-已申请, 1-已加入")
    private Integer clubStatus;

    @Schema(description = "社团名称")
    private String clubName;

    @Schema(description = "申请加入时间")
    private LocalDateTime createdTime;

}
