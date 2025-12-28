package com.clubmanagement.service.impl;

import com.clubmanagement.dao.IActivityDAO;
import com.clubmanagement.dao.impl.ActivityDAOImpl;
import com.clubmanagement.model.Activity;
import com.clubmanagement.service.IActivityService;
import com.clubmanagement.service.IClubService;
import com.clubmanagement.service.IUserService;
import com.clubmanagement.service.impl.ClubServiceImpl;
import com.clubmanagement.service.impl.UserServiceImpl;
import com.clubmanagement.common.Constants;
import com.clubmanagement.common.exception.BusinessException;

import java.util.Date;
import java.util.List;

/**
 * 活动服务实现类
 */
public class ActivityServiceImpl implements IActivityService {
    
    private IActivityDAO activityDAO;
    private IClubService clubService;
    private IUserService userService;
    
    public ActivityServiceImpl() {
        this.activityDAO = new ActivityDAOImpl();
        this.clubService = new ClubServiceImpl();
        this.userService = new UserServiceImpl();
    }
    
    @Override
    public boolean createActivity(Activity activity, int creatorId) {
        // 参数验证
        if (activity == null) {
            throw new BusinessException("活动信息不能为空");
        }
        if (activity.getTitle() == null || activity.getTitle().trim().isEmpty()) {
            throw new BusinessException("活动标题不能为空");
        }
        if (activity.getDescription() == null || activity.getDescription().trim().isEmpty()) {
            throw new BusinessException("活动描述不能为空");
        }
        if (activity.getClubId() <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (activity.getStartTime() == null) {
            throw new BusinessException("活动开始时间不能为空");
        }
        if (activity.getEndTime() == null) {
            throw new BusinessException("活动结束时间不能为空");
        }
        if (activity.getLocation() == null || activity.getLocation().trim().isEmpty()) {
            throw new BusinessException("活动地点不能为空");
        }
        if (activity.getMaxParticipants() <= 0) {
            throw new BusinessException("最大参与人数必须大于0");
        }
        if (creatorId <= 0) {
            throw new BusinessException("创建者ID无效");
        }
        
        // 检查开始时间是否早于结束时间
        if (activity.getStartTime().after(activity.getEndTime())) {
            throw new BusinessException("活动开始时间不能晚于结束时间");
        }
        
        // 检查社团是否存在
        if (clubService.getClubById(activity.getClubId()) == null) {
            throw new BusinessException("社团不存在");
        }
        
        // 检查创建者是否有权限在该社团创建活动
        if (!clubService.hasClubPermission(activity.getClubId(), creatorId)) {
            throw new BusinessException("没有权限在该社团创建活动");
        }
        
        // 检查活动标题是否已存在（在同一社团内）
        if (activityDAO.isActivityTitleExists(activity.getClubId(), activity.getTitle())) {
            throw new BusinessException("该社团下已存在相同标题的活动");
        }
        
        // 设置默认值
        activity.setCurrentParticipants(0);
        activity.setStatus(Constants.ACTIVITY_STATUS_PLANNING);
        activity.setCreatedAt(new Date());
        activity.setUpdatedAt(new Date());
        
        // 调用DAO层创建活动
        return activityDAO.createActivity(activity);
    }
    
    @Override
    public Activity getActivityById(int id) {
        if (id <= 0) {
            throw new BusinessException("活动ID无效");
        }
        
        Activity activity = activityDAO.getActivityById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        return activity;
    }
    
    @Override
    public Activity getActivityByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException("活动标题不能为空");
        }
        
        Activity activity = activityDAO.getActivityByTitle(title);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        return activity;
    }
    
    @Override
    public boolean isActivityTitleExists(int clubId, String title) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException("活动标题不能为空");
        }
        
        return activityDAO.isActivityTitleExists(clubId, title);
    }
    
    @Override
    public List<Activity> getAllActivities() {
        return activityDAO.getAllActivities();
    }
    
    @Override
    public List<Activity> getActivitiesByPage(int page, int pageSize) {
        if (page <= 0) {
            throw new BusinessException("页码必须大于0");
        }
        if (pageSize <= 0) {
            throw new BusinessException("每页大小必须大于0");
        }
        
        return activityDAO.getActivitiesByPage(page, pageSize);
    }
    
    @Override
    public List<Activity> getActivitiesByClub(int clubId) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        
        return activityDAO.getActivitiesByClub(clubId);
    }
    
    @Override
    public List<Activity> getActivitiesByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("活动状态不能为空");
        }
        
        return activityDAO.getActivitiesByStatus(status);
    }
    
    @Override
    public List<Activity> getRegisterableActivities() {
        return activityDAO.getRegisterableActivities();
    }
    
    @Override
    public List<Activity> getOngoingActivities() {
        return activityDAO.getOngoingActivities();
    }
    
    @Override
    public List<Activity> getUpcomingActivities(int days) {
        if (days <= 0) {
            throw new BusinessException("天数必须大于0");
        }
        
        return activityDAO.getUpcomingActivities(days);
    }
    
    @Override
    public List<Activity> getActivitiesByDateRange(Date startDate, Date endDate) {
        if (startDate == null) {
            throw new BusinessException("开始日期不能为空");
        }
        if (endDate == null) {
            throw new BusinessException("结束日期不能为空");
        }
        if (startDate.after(endDate)) {
            throw new BusinessException("开始日期不能晚于结束日期");
        }
        
        return activityDAO.getActivitiesByDateRange(startDate, endDate);
    }
    
    @Override
    public boolean updateActivity(Activity activity, int operatorId) {
        // 参数验证
        if (activity == null) {
            throw new BusinessException("活动信息不能为空");
        }
        if (activity.getId() <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (activity.getTitle() == null || activity.getTitle().trim().isEmpty()) {
            throw new BusinessException("活动标题不能为空");
        }
        if (activity.getDescription() == null || activity.getDescription().trim().isEmpty()) {
            throw new BusinessException("活动描述不能为空");
        }
        if (activity.getStartTime() == null) {
            throw new BusinessException("活动开始时间不能为空");
        }
        if (activity.getEndTime() == null) {
            throw new BusinessException("活动结束时间不能为空");
        }
        if (activity.getLocation() == null || activity.getLocation().trim().isEmpty()) {
            throw new BusinessException("活动地点不能为空");
        }
        if (activity.getMaxParticipants() <= 0) {
            throw new BusinessException("最大参与人数必须大于0");
        }
        if (operatorId <= 0) {
            throw new BusinessException("操作者ID无效");
        }
        
        // 检查开始时间是否早于结束时间
        if (activity.getStartTime().after(activity.getEndTime())) {
            throw new BusinessException("活动开始时间不能晚于结束时间");
        }
        
        // 检查活动是否存在
        Activity existingActivity = activityDAO.getActivityById(activity.getId());
        if (existingActivity == null) {
            throw new BusinessException("活动不存在");
        }
        
        // 检查操作者权限
        if (!hasActivityPermission(activity.getId(), operatorId)) {
            throw new BusinessException("没有权限更新该活动");
        }
        
        // 检查活动标题是否被其他活动使用（在同一社团内）
        if (!existingActivity.getTitle().equals(activity.getTitle())) {
            if (activityDAO.isActivityTitleExists(existingActivity.getClubId(), activity.getTitle())) {
                throw new BusinessException("该社团下已存在相同标题的活动");
            }
        }
        
        // 更新更新时间
        activity.setUpdatedAt(new Date());
        
        // 调用DAO层更新活动
        return activityDAO.updateActivity(activity);
    }
    
    @Override
    public boolean updateActivityStatus(int activityId, String status, int operatorId) {
        // 参数验证
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("活动状态不能为空");
        }
        if (operatorId <= 0) {
            throw new BusinessException("操作者ID无效");
        }
        
        // 检查活动是否存在
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        // 检查操作者权限
        if (!hasActivityPermission(activityId, operatorId)) {
            throw new BusinessException("没有权限更新该活动状态");
        }
        
        // 调用DAO层更新活动状态
        return activityDAO.updateActivityStatus(activityId, status);
    }
    
    @Override
    public boolean deleteActivity(int id, int operatorId) {
        // 参数验证
        if (id <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (operatorId <= 0) {
            throw new BusinessException("操作者ID无效");
        }
        
        // 检查活动是否存在
        Activity activity = activityDAO.getActivityById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        // 检查操作者权限
        if (!hasActivityPermission(id, operatorId)) {
            throw new BusinessException("没有权限删除该活动");
        }
        
        // 调用DAO层删除活动
        return activityDAO.deleteActivity(id);
    }
    
    @Override
    public int getActivityCount() {
        return activityDAO.getActivityCount();
    }
    
    @Override
    public int getActivityCountByClub(int clubId) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        
        return activityDAO.getActivityCountByClub(clubId);
    }
    
    @Override
    public int getActivityCountByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("活动状态不能为空");
        }
        
        return activityDAO.getActivityCountByStatus(status);
    }
    
    @Override
    public List<Activity> searchActivities(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException("搜索关键词不能为空");
        }
        
        return activityDAO.searchActivities(keyword);
    }
    
    @Override
    public List<Activity> getPopularActivities(int limit) {
        if (limit <= 0) {
            throw new BusinessException("数量限制必须大于0");
        }
        
        return activityDAO.getPopularActivities(limit);
    }
    
    @Override
    public List<Activity> getNewestActivities(int limit) {
        if (limit <= 0) {
            throw new BusinessException("数量限制必须大于0");
        }
        
        return activityDAO.getNewestActivities(limit);
    }
    
    @Override
    public boolean isUserRegistered(int activityId, int userId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        return activityDAO.isUserRegistered(activityId, userId);
    }
    
    @Override
    public boolean registerActivity(int activityId, int userId) {
        // 参数验证
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 检查活动是否存在
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        // 检查用户是否存在
        if (userService.getUserById(userId) == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户是否已报名
        if (activityDAO.isUserRegistered(activityId, userId)) {
            throw new BusinessException("您已报名该活动");
        }
        
        // 检查活动是否可报名
        if (!activity.isRegisterable()) {
            throw new BusinessException("该活动当前不可报名");
        }
        
        // 检查活动是否已满
        if (activity.isFull()) {
            throw new BusinessException("该活动已满员");
        }
        
        // 调用DAO层报名活动
        return activityDAO.registerActivity(activityId, userId);
    }
    
    @Override
    public boolean cancelRegistration(int activityId, int userId) {
        // 参数验证
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 检查活动是否存在
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        
        // 检查用户是否已报名
        if (!activityDAO.isUserRegistered(activityId, userId)) {
            throw new BusinessException("您未报名该活动");
        }
        
        // 检查活动是否已开始
        if (activity.isStarted()) {
            throw new BusinessException("活动已开始，无法取消报名");
        }
        
        // 调用DAO层取消报名
        return activityDAO.cancelRegistration(activityId, userId);
    }
    
    @Override
    public List<Activity> getUserRegisteredActivities(int userId) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        return activityDAO.getUserRegisteredActivities(userId);
    }
    
    @Override
    public List<Integer> getActivityRegisteredUsers(int activityId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        
        return activityDAO.getActivityRegisteredUsers(activityId);
    }
    
    @Override
    public int getActivityRegistrationCount(int activityId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        
        return activityDAO.getActivityRegistrationCount(activityId);
    }
    
    @Override
    public boolean hasActivityPermission(int activityId, int userId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 检查活动是否存在
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            return false;
        }
        
        // 管理员拥有所有权限
        if (userService.isAdmin(userId)) {
            return true;
        }
        
        // 检查用户是否有权限管理该活动所属的社团
        return clubService.hasClubPermission(activity.getClubId(), userId);
    }
    
    @Override
    public boolean isActivityRegisterable(int activityId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            return false;
        }
        
        return activity.isRegisterable();
    }
    
    @Override
    public boolean isActivityFull(int activityId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            return false;
        }
        
        return activity.isFull();
    }
    
    @Override
    public int getActivityRemainingSlots(int activityId) {
        if (activityId <= 0) {
            throw new BusinessException("活动ID无效");
        }
        
        Activity activity = activityDAO.getActivityById(activityId);
        if (activity == null) {
            return 0;
        }
        
        return activity.getRemainingSlots();
    }
    
    @Override
    public List<Activity> getUserManageableActivities(int userId) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 获取用户可管理的社团
        List<com.clubmanagement.model.Club> manageableClubs = clubService.getUserManageableClubs(userId);
        
        // 获取这些社团的所有活动
        List<Activity> manageableActivities = new java.util.ArrayList<>();
        for (com.clubmanagement.model.Club club : manageableClubs) {
            manageableActivities.addAll(activityDAO.getActivitiesByClub(club.getId()));
        }
        
        return manageableActivities;
    }
    
    @Override
    public List<Activity> getUserRegisterableActivities(int userId) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 获取所有可报名的活动
        List<Activity> registerableActivities = activityDAO.getRegisterableActivities();
        
        // 过滤掉用户已报名的活动
        List<Activity> result = new java.util.ArrayList<>();
        for (Activity activity : registerableActivities) {
            if (!activityDAO.isUserRegistered(activity.getId(), userId)) {
                result.add(activity);
            }
        }
        
        return result;
    }
}
