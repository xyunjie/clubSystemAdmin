package com.club.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.*;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.club.ClubActivityQueryDto;
import com.club.entity.dto.club.ClubActivitySaveDto;
import com.club.entity.dto.club.ClubQueryUserDto;
import com.club.entity.enums.ActivityKindEnum;
import com.club.entity.enums.ActivityStatusEnum;
import com.club.entity.enums.ClubUserStatusEnum;
import com.club.entity.vo.UserVo;
import com.club.entity.vo.club.ClubActivityUserVo;
import com.club.entity.vo.club.ClubActivityVo;
import com.club.mapper.ActivityMapper;
import com.club.mapper.ClubMapper;
import com.club.mapper.ClubUserMapMapper;
import com.club.service.ActivityService;
import com.club.service.ActivityUserMapService;
import com.club.service.DictService;
import com.club.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

    @Resource
    private DictService dictService;

    @Override
    public Page<ClubActivityVo> getClubNoticeList(ClubActivityQueryDto clubActivityQueryDto) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(clubActivityQueryDto.getId() != null, Activity::getClubId, clubActivityQueryDto.getId());
        queryWrapper.eq(StringUtils.isNotEmpty(clubActivityQueryDto.getKind()), Activity::getKind, clubActivityQueryDto.getKind());
        queryWrapper.eq(clubActivityQueryDto.getStatus() != null, Activity::getStatus, clubActivityQueryDto.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(clubActivityQueryDto.getQuery()), Activity::getTitle, clubActivityQueryDto.getQuery());
        queryWrapper.like(StringUtils.isNotEmpty(clubActivityQueryDto.getQuery()), Activity::getContent, clubActivityQueryDto.getQuery());
        queryWrapper.eq(!clubActivityQueryDto.getIsAdmin(), Activity::getStatus, ActivityStatusEnum.AUDIT_PASS.getValue());
        queryWrapper.or(item -> {
            item.eq(Activity::getCreatedBy, clubActivityQueryDto.getUserId());
            item.eq(StringUtils.isNotEmpty(clubActivityQueryDto.getKind()), Activity::getKind, clubActivityQueryDto.getKind());
        });
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
            clubActivityVo.setCreatedUser(userMap.getOrDefault(item.getCreatedBy(), new User()));
            clubActivityVo.setClubName(clubMap.getOrDefault(item.getClubId(), new Club()).getName());
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
        queryWrapper.eq(ClubUserMap::getClubId, activity.getClubId());
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

    @Override
    public Page<ClubActivityUserVo> getActivityUserList(ClubQueryUserDto clubQueryUserDto) {
        Page<ActivityUserMap> page = new Page<>();
        activityUserMapService.lambdaQuery()
                .eq(ActivityUserMap::getActivityId, clubQueryUserDto.getActivityId()).page(page);
        List<ActivityUserMap> records = page.getRecords();
        Activity activity = getById(clubQueryUserDto.getActivityId());
        Page<ClubActivityUserVo> result = new Page<>();
        BeanUtils.copyProperties(page, result, "records");
        List<Long> userIds = records.stream().map(ActivityUserMap::getUserId).toList();
        Map<Long, UserVo> collect = userService.getUserListByIds(userIds).stream().collect(Collectors.toMap(UserVo::getId, item -> item));
        List<ClubActivityUserVo> resultRecords = records.stream().map(item -> {
            ClubActivityUserVo clubActivityUserVo = new ClubActivityUserVo();
            BeanUtils.copyProperties(item, clubActivityUserVo);
            clubActivityUserVo.setUser(collect.get(item.getUserId()));
            clubActivityUserVo.setBeginTime(activity.getBeginTime());
            clubActivityUserVo.setEndTime(activity.getEndTime());
            return clubActivityUserVo;
        }).toList();
        return result.setRecords(resultRecords);
    }

    @Override
    public List<ClubActivityVo> getHotActivityList() {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getStatus, ActivityStatusEnum.AUDIT_PASS.getValue());
        queryWrapper.eq(Activity::getKind, ActivityKindEnum.ACTIVITY.getValue());
        queryWrapper.orderByDesc(Activity::getViews);
        queryWrapper.orderByDesc(Activity::getCreatedTime);
        queryWrapper.last("limit 10");
        List<Activity> list = this.list(queryWrapper);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> clubIds = list.stream().map(Activity::getClubId).distinct().toList();
        List<Long> userIds = list.stream().map(Activity::getCreatedBy).distinct().toList();
        Map<Long, Club> clubMap = clubMapper.selectBatchIds(clubIds).stream().collect(Collectors.toMap(Club::getId, item -> item));
        Map<Long, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, item -> item));
        return list.stream().map(item -> {
            ClubActivityVo clubActivityVo = new ClubActivityVo();
            BeanUtils.copyProperties(item, clubActivityVo);
            clubActivityVo.setCreatedUser(userMap.getOrDefault(item.getCreatedBy(), new User()));
            clubActivityVo.setClubName(clubMap.getOrDefault(item.getClubId(), new Club()).getName());
            return clubActivityVo;
        }).toList();
    }

    @Override
    public List<ClubActivityVo> getClubWarning(Long userId) {
        LambdaQueryWrapper<ClubUserMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubUserMap::getUserId, userId);
        queryWrapper.eq(ClubUserMap::getStatus, ClubUserStatusEnum.CLUB_CREATOR.getValue());
        queryWrapper.select(ClubUserMap::getClubId);
        List<Long> list = clubUserMapMapper.selectList(queryWrapper).stream().map(ClubUserMap::getClubId).toList();
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Activity> activityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activityLambdaQueryWrapper
                .in(Activity::getClubId, list)
                .eq(Activity::getKind, ActivityKindEnum.WARNING.getValue());
        List<Activity> activities = this.list(activityLambdaQueryWrapper);
        if (activities.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> users = userService.listByIds(activities.stream().map(Activity::getCreatedBy).toList());
        List<Club> clubs = clubMapper.selectBatchIds(list);
        return activities.stream().map(item -> {
            ClubActivityVo clubActivityVo = new ClubActivityVo();
            BeanUtils.copyProperties(item, clubActivityVo);
            clubActivityVo.setCreatedUser(users.stream().filter(user -> user.getId().equals(item.getCreatedBy())).findFirst().orElse(new User()));
            clubActivityVo.setClubName(clubs.stream().filter(club -> club.getId().equals(item.getClubId())).findFirst().orElse(new Club()).getName());
            return clubActivityVo;
        }).toList();
    }

    @Override
    public Page<ClubActivityUserVo> getMyActivity(ClubActivityQueryDto clubActivityQueryDto) {
        List<Activity> list = this.lambdaQuery()
                .eq(Activity::getCreatedBy, clubActivityQueryDto.getUserId())
                .eq(Activity::getKind, ActivityKindEnum.ACTIVITY.getValue()).list();
        if (list.isEmpty()) {
            return new Page<>();
        }
        List<Long> activityIds = list.stream().map(Activity::getId).toList();
        Page<ActivityUserMap> page = new Page<>(clubActivityQueryDto.getPageNumber(), clubActivityQueryDto.getPageSize());
        activityUserMapService.lambdaQuery().in(ActivityUserMap::getActivityId, activityIds).orderByDesc(ActivityUserMap::getActivityId).page(page);
        List<ActivityUserMap> activityUserMapList = page.getRecords();
        List<Long> userIds = activityUserMapList.stream().map(ActivityUserMap::getUserId).distinct().toList();
        Map<Long, UserVo> collect = userService.getUserListByIds(userIds).stream().collect(Collectors.toMap(UserVo::getId, item -> item));
        List<ClubActivityUserVo> clubActivityUserVoList = activityUserMapList.stream().map(item -> {
            ClubActivityUserVo clubActivityUserVo = new ClubActivityUserVo();
            BeanUtils.copyProperties(item, clubActivityUserVo);
            Activity activity = list.stream()
                    .filter(a -> a.getId().equals(item.getActivityId())).findFirst().orElse(new Activity());
            clubActivityUserVo.setUser(collect.get(item.getUserId()));
            clubActivityUserVo.setActivityName(activity.getTitle());
            clubActivityUserVo.setBeginTime(activity.getBeginTime());
            clubActivityUserVo.setEndTime(activity.getEndTime());
            return clubActivityUserVo;
        }).toList();
        Page<ClubActivityUserVo> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage, "records");
        resPage.setRecords(clubActivityUserVoList);
        return resPage;
    }

    @Override
    public void exportActivityUserList(ClubQueryUserDto clubQueryUserDto, Long userId, HttpServletResponse response) {
        if (clubQueryUserDto.getActivityId() == null) {
            throw new GlobalException("活动id不能为空");
        }
        List<ActivityUserMap> list = activityUserMapService.lambdaQuery().eq(ActivityUserMap::getActivityId, clubQueryUserDto.getActivityId()).list();
        List<Long> userIds = list.stream().map(ActivityUserMap::getUserId).toList();

        List<User> users = userService.listByIds(userIds);
        List<Dict> dicts = dictService.list();
        List<UserVo> userVos = users.stream().map(item -> userService.parseUserToUserVo(item, dicts)).toList();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(LocalDateTime.now() + "导出活动人员列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, UserVo.class).sheet("user").doWrite(userVos);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new GlobalException(e.getMessage(), 400);
        }

    }
}




