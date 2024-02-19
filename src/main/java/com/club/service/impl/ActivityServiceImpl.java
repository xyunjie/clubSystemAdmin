package com.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.*;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.club.ClubActivityQueryDto;
import com.club.entity.dto.club.ClubActivitySaveDto;
import com.club.entity.enums.ActivityStatusEnum;
import com.club.entity.vo.club.ClubActivityVo;
import com.club.mapper.ActivityMapper;
import com.club.mapper.ClubMapper;
import com.club.mapper.ClubUserMapMapper;
import com.club.service.ActivityService;
import com.club.service.ActivityUserMapService;
import com.club.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 针对表【t_activity(社团公告/活动表)】的数据库操作Service实现
 * @createDate 2024-02-07 18:31:00
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
        implements ActivityService {

    @Resource
    private UserService userService;

    @Resource
    private ClubUserMapMapper clubUserMapMapper;

    @Resource
    private ActivityUserMapService activityUserMapService;

    @Resource
    private ClubMapper clubMapper;

    @Override
    public Page<ClubActivityVo> getClubNoticeList(ClubActivityQueryDto clubActivityQueryDto) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(clubActivityQueryDto.getId() != null, Activity::getClubId, clubActivityQueryDto.getId());
        queryWrapper.eq(StringUtils.isNotEmpty(clubActivityQueryDto.getKind()), Activity::getKind, clubActivityQueryDto.getKind());
        queryWrapper.eq(clubActivityQueryDto.getStatus() != null, Activity::getStatus, clubActivityQueryDto.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(clubActivityQueryDto.getQuery()), Activity::getTitle, clubActivityQueryDto.getQuery());
        queryWrapper.like(StringUtils.isNotEmpty(clubActivityQueryDto.getQuery()), Activity::getContent, clubActivityQueryDto.getQuery());
        if (StringUtils.isNotEmpty(clubActivityQueryDto.getQuery())) {
            LambdaQueryWrapper<Club> clubLambdaQueryWrapper = new LambdaQueryWrapper<>();
            clubLambdaQueryWrapper.like(Club::getName, clubActivityQueryDto.getQuery());
            List<Club> clubs = clubMapper.selectList(clubLambdaQueryWrapper);
            queryWrapper.in(!clubs.isEmpty(), Activity::getClubId, clubs.stream().map(Club::getId).collect(Collectors.toList()));
            List<User> users = userService.lambdaQuery().like(User::getName, clubActivityQueryDto.getQuery()).list();
            queryWrapper.in(!users.isEmpty(), Activity::getCreatedBy, users.stream().map(User::getId).collect(Collectors.toList()));
        }
        queryWrapper.orderByDesc(Activity::getCreatedTime);
        Page<Activity> page = new Page<>(clubActivityQueryDto.getPageNumber(), clubActivityQueryDto.getPageSize());
        this.page(page, queryWrapper);
        Page<ClubActivityVo> result = new Page<>();
        BeanUtils.copyProperties(page, result, "records");
        if (page.getRecords().isEmpty()) {
            result.setRecords(new ArrayList<>());
            return result;
        }
        List<Long> clubIds = page.getRecords().stream().map(Activity::getClubId).distinct().toList();
        List<Long> userIds = page.getRecords().stream().map(Activity::getCreatedBy).distinct().toList();
        Map<Long, Club> clubMap = clubMapper.selectBatchIds(clubIds).stream().collect(Collectors.toMap(Club::getId, item -> item));
        Map<Long, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, item -> item));
        List<ClubActivityVo> clubActivityVoStream = page.getRecords().stream().map(item -> {
            ClubActivityVo clubActivityVo = new ClubActivityVo();
            BeanUtils.copyProperties(item, clubActivityVo);
            clubActivityVo.setCreatedUser(userMap.get(item.getCreatedBy()));
            clubActivityVo.setClubName(clubMap.get(item.getClubId()).getName());
            return clubActivityVo;
        }).toList();
        return result.setRecords(clubActivityVoStream);
    }

    @Override
    public void modifyActivityStatus(ModifyStatusDto modifyStatusDto) {
        ActivityStatusEnum enumByValue = ActivityStatusEnum.getEnumByValue(modifyStatusDto.getStatus());
        if (enumByValue == null) {
            throw new GlobalException("状态不正确");
        }
        boolean update = lambdaUpdate()
                .eq(Activity::getId, modifyStatusDto.getId())
                .set(Activity::getStatus, modifyStatusDto.getStatus())
                .update();
        if (!update) {
            throw new GlobalException("修改状态失败");
        }
    }

    @Override
    public void removeAcvitity(Long id, Long userId) {
        boolean b = this.removeById(id);
        if (!b) {
            throw new GlobalException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveActivity(ClubActivitySaveDto clubActivitySaveDto, Long userId) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(clubActivitySaveDto, activity);
        if (clubActivitySaveDto.getId() == null) {
            // 新增
            activity.setCreatedBy(userId);
        }
        activity.setStatus(ActivityStatusEnum.WAIT_AUDIT.getValue());
        boolean b = this.saveOrUpdate(activity);
        if (!b) {
            throw new GlobalException("保存失败");
        }
    }

    @Override
    public void entryActivity(Long id, Long userId) {
        // 查询活动信息
        Activity activity = this.getById(id);
        LambdaQueryWrapper<ClubUserMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubUserMap::getClubId, activity.getCreatedBy());
        queryWrapper.eq(ClubUserMap::getUserId, userId);
        ClubUserMap clubUserMap = clubUserMapMapper.selectOne(queryWrapper);
        if (clubUserMap == null) {
            throw new GlobalException("您不是该社团成员，无法报名");
        }
        ActivityUserMap activityUserMap = activityUserMapService.lambdaQuery().eq(ActivityUserMap::getActivityId, id).eq(ActivityUserMap::getUserId, userId).one();
        if (activityUserMap != null) {
            throw new GlobalException("您已经报名，无需重复报名");
        }
        ActivityUserMap saveMap = new ActivityUserMap();
        saveMap.setActivityId(id);
        saveMap.setUserId(userId);
        saveMap.setStatus(ActivityStatusEnum.WAIT_AUDIT.getValue());
        boolean b = activityUserMapService.save(saveMap);
        if (!b) {
            throw new GlobalException("报名失败");
        }
    }

    @Override
    public void modifyActivityEntryStatus(ModifyStatusDto modifyStatusDto) {
        ActivityStatusEnum enumByValue = ActivityStatusEnum.getEnumByValue(modifyStatusDto.getStatus());
        if (enumByValue == null) {
            throw new GlobalException("状态码错误");
        }
        ActivityUserMap activityUserMap = activityUserMapService.getById(modifyStatusDto.getId());
        if (activityUserMap == null) {
            throw new GlobalException("活动报名记录不存在");
        }
        boolean update = activityUserMapService.lambdaUpdate()
                .eq(ActivityUserMap::getId, modifyStatusDto.getId())
                .set(ActivityUserMap::getStatus, modifyStatusDto.getStatus()).update();
        if (!update) {
            throw new GlobalException("修改状态失败");
        }
    }

    @Override
    public void removeEntry(Long id, Long userId) {
        boolean b = activityUserMapService.removeById(id);
        if (!b) {
            throw new GlobalException("删除失败");
        }
    }
}




