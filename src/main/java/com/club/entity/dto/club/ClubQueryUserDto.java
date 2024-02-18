package com.club.entity.dto.club;

import com.club.entity.dto.base.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月11日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubQueryUserDto extends PageQuery {

    private Long clubId;

}
