package com.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.entity.domain.Activity;
import com.club.entity.domain.System;
import com.club.entity.dto.base.PageQuery;
import com.club.entity.enums.ActivityKindEnum;
import com.club.entity.vo.club.ClubActivityVo;
import com.club.mapper.ActivityMapper;
import com.club.mapper.SystemMapper;
import com.club.service.SystemService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2024/2/29 15:46
 **/
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System>
        implements SystemService {

    @Resource
    private ActivityMapper activityMapper;

    @Override
    public System getSystemInfo() {
        return lambdaQuery().one();
    }

    @Override
    public void saveSystemInfo(System system, Long userId) {
        System one = this.lambdaQuery().one();
        system.setUpdatedBy(userId);
        if (one == null) {
            save(system);
            return;
        }
        LambdaUpdateWrapper<System> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(System::getId, one.getId());
        updateWrapper.set(StringUtils.isNotEmpty(system.getAbout()), System::getAbout, system.getAbout());
        updateWrapper.set(StringUtils.isNotEmpty(system.getClubTemplate()), System::getClubTemplate, system.getClubTemplate());
        updateWrapper.set(StringUtils.isNotEmpty(system.getIntroduction()), System::getIntroduction, system.getIntroduction());
        updateWrapper.set(System::getUpdatedBy, system.getUpdatedBy());
        this.update(updateWrapper);
    }

    @Override
    public Page<ClubActivityVo> getNotice(PageQuery pageQuery) {
        Page<Activity> page = new Page<>(pageQuery.getPageNumber(), pageQuery.getPageSize());
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getKind, ActivityKindEnum.SYSTEM_NOTICE.getValue());
        queryWrapper.orderByAsc(Activity::getCreatedTime);
        Page<Activity> activityPage = activityMapper.selectPage(page, queryWrapper);
        List<Activity> records = activityPage.getRecords();
        Page<ClubActivityVo> res = new Page<>();
        BeanUtils.copyProperties(activityPage, res, "records");
        List<ClubActivityVo> list = records.stream().map(activity -> {
            ClubActivityVo vo = new ClubActivityVo();
            BeanUtils.copyProperties(activity, vo);
            return vo;
        }).toList();
        res.setRecords(list);
        return res;
    }
}
