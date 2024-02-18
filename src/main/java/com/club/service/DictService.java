package com.club.service;

import com.club.entity.domain.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.dto.dict.DictDto;
import com.club.entity.vo.DictVo;

import java.util.List;

/**
* @description 针对表【t_dict(字典表)】的数据库操作Service
* @createDate 2024-02-07 18:31:00
*/
public interface DictService extends IService<Dict> {

    /**
     *  获取字典列表
     * @param parentId
     * @return
     */
    List<DictVo> getDictList(Long parentId, Boolean isGrade);

    /**
     *  保存字典
     * @param dictDto
     */
    void saveDict(DictDto dictDto);

    /**
     *   获取字典详情
     * @param gradeId
     * @return
     */
    List<DictVo> getListByGrade(Long gradeId);
}
