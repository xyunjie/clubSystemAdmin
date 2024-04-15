package com.club.service;

import com.club.entity.domain.ExcitingMoments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.dto.exciting.ExcitingMomentsDto;

import java.util.List;

/**
* @description 针对表【t_exciting_moments(活动精彩瞬间)】的数据库操作Service
* @createDate 2024-02-07 18:31:00
*/
public interface ExcitingMomentsService extends IService<ExcitingMoments> {

    void saveExcitingMoments(ExcitingMomentsDto excitingMomentsDto, Long userId);

    List<ExcitingMomentsDto> getExcitingMomentsList(Long clubId);

    void removeExcitingMoments(Long id, Long userId);

    List<ExcitingMomentsDto> getByClubIdExcitingMomentsList(Long id);
}
