package com.club.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.Club;
import com.club.entity.domain.ExcitingMoments;
import com.club.entity.dto.exciting.ExcitingMomentsDto;
import com.club.mapper.ExcitingMomentsMapper;
import com.club.service.ClubService;
import com.club.service.ExcitingMomentsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @description 针对表【t_exciting_moments(活动精彩瞬间)】的数据库操作Service实现
* @createDate 2024-02-07 18:31:00
*/
@Service
public class ExcitingMomentsServiceImpl extends ServiceImpl<ExcitingMomentsMapper, ExcitingMoments>
    implements ExcitingMomentsService{

    @Resource
    private ClubService clubService;

    @Override
    public void saveExcitingMoments(ExcitingMomentsDto excitingMomentsDto, Long userId) {
        Club one = clubService.lambdaQuery().eq(Club::getCreatedBy, userId).one();
        if (one == null) {
            throw new GlobalException("该用户没有创建组织");
        }
        ExcitingMoments excitingMoments = new ExcitingMoments();
        excitingMoments.setClubId(one.getId());
        excitingMoments.setUrl(excitingMomentsDto.getUrl());
        this.save(excitingMoments);
    }

    @Override
    public List<ExcitingMomentsDto> getExcitingMomentsList(Long id) {
        // 获取该用户的组织ID
        Club one = clubService.lambdaQuery().eq(Club::getCreatedBy, id).one();
        if (one == null) {
            return List.of();
        }
        List<ExcitingMoments> list = this.lambdaQuery().eq(ExcitingMoments::getClubId, one.getId()).list();
        return list.stream().map(item -> {
            ExcitingMomentsDto excitingMomentsDto = new ExcitingMomentsDto();
            excitingMomentsDto.setClubId(item.getClubId());
            excitingMomentsDto.setUrl(item.getUrl());
            excitingMomentsDto.setId(item.getId());
            return excitingMomentsDto;
        }).toList();
    }

    @Override
    public void removeExcitingMoments(Long id, Long userId) {
        this.removeById(id);
    }

    @Override
    public List<ExcitingMomentsDto> getByClubIdExcitingMomentsList(Long id) {
        return this.lambdaQuery().eq(ExcitingMoments::getClubId, id).list().stream().map(item -> {
            ExcitingMomentsDto excitingMomentsDto = new ExcitingMomentsDto();
            excitingMomentsDto.setClubId(item.getClubId());
            excitingMomentsDto.setUrl(item.getUrl());
            excitingMomentsDto.setId(item.getId());
            return excitingMomentsDto;
        }).toList();
    }
}




