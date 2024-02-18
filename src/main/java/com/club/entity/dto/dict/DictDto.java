package com.club.entity.dto.dict;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class DictDto {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "父级id")
    private Long parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "年级")
    private Long grade;

    @Schema(description = "是否年级")
    private Boolean isGrade;

    @Schema(description = "描述")
    private String description;

}
