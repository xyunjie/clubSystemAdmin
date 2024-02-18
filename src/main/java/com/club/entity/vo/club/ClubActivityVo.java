package com.club.entity.vo.club;

import com.club.entity.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @date 2024年02月18日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubActivityVo {

    @Schema(description = "公告id")
    private Long id;

    @Schema(description = "公告title")
    private String title;

    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "公告发布的社团")
    private Long clubId;

    @Schema(description = "类型")
    private String kind;

    @Schema(description = "公告排序")
    private Integer sort;

    @Schema(description = "是否置顶")
    private Boolean top;

    @Schema(description = "公告创建者ID")
    private Long createdBy;

    @Schema(description = "公告创建者")
    private User createdUser;

    @Schema(description = "活动开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

}
