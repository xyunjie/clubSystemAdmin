package com.club.entity.vo.club;

import com.club.entity.vo.UserVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @date 2024年02月21日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubActivityUserVo {

    @Schema(description = "记录ID")
    private Long id;
    @Schema(description = "活动ID")
    private Long activityId;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "用户信息")
    private UserVo user;
    @Schema(description = "报名状态")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "开始时间")
    private LocalDateTime beginTime;
    @Schema(description = "更新时间")
    private LocalDateTime endTime;
    @Schema(description = "报名时间")
    private LocalDateTime createdTime;
}
