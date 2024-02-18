package com.club.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @date 2024年02月09日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DictVo {

    @Schema(name = "id", description = "字典ID")
    private Long id;

    @Schema(name = "parentId", description = "字典父ID")
    private Long parentId;

    @Schema(name = "name", description = "字典名称")
    private String name;

    @Schema(name = "grade", description = "年级")
    private Long grade;

    @Schema(name = "description", description = "字典描述")
    private String description;

    @Schema(name = "children", description = "子级字典")
    private List<DictVo> children;
}
