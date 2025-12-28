package com.clubmanagement.dao;

import com.clubmanagement.model.Activity;

import java.util.Date;
import java.util.List;

/**
 * 活动数据访问接口
 * 定义活动相关的数据库操作
 */
public interface IActivityDAO {
    
    /**
     * 创建活动
     * @param activity 活动对象
     * @return 创建成功返回true，失败返回false
     */
    boolean createActivity(Activity activity);
    
    /**
     * 根据ID获取活动
     * @param id 活动ID
     * @return 活动对象
     */
    Activity getActivityById(int id);
    
    /**
     * 根据标题获取活动
     * @param title 活动标题
     * @return 活动对象
     */
    Activity getActivityByTitle(String title);
    
    /**
     * 检查活动标题是否已存在（在同一社团内）
     * @param clubId 社团ID
     * @param title 活动标题
     * @return 存在返回true，不存在返回false
     */
    boolean isActivityTitleExists(int clubId, String title);
    
    /**
     * 获取所有活动
     * @return 活动列表
     */
    List<Activity> getAllActivities();
    
    /**
     * 分页获取活动
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 活动列表
     */
    List<Activity> getActivitiesByPage(int page, int pageSize);
    
    /**
     * 根据社团ID获取活动
     * @param clubId 社团ID
     * @return 活动列表
     */
    List<Activity> getActivitiesByClub(int clubId);
    
    /**
     * 根据状态获取活动
     * @param status 活动状态
     * @return 活动列表
     */
    List<Activity> getActivitiesByStatus(String status);
    
    /**
     * 获取可报名的活动
     * @return 可报名的活动列表
     */
    List<Activity> getRegisterableActivities();
    
    /**
     * 获取进行中的活动
     * @return 进行中的活动列表
     */
    List<Activity> getOngoingActivities();
    
    /**
     * 获取即将开始的活动
     * @param days 未来天数
     * @return 即将开始的活动列表
     */
    List<Activity> getUpcomingActivities(int days);
    
    /**
     * 根据时间范围获取活动
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 活动列表
     */
    List<Activity> getActivitiesByDateRange(Date startDate, Date endDate);
    
    /**
     * 更新活动信息
     * @param activity 活动对象
     * @return 更新成功返回true
     */
    boolean updateActivity(Activity activity);
    
    /**
     * 更新活动状态
     * @param activityId 活动ID
     * @param status 新状态
     * @return 更新成功返回true
     */
    boolean updateActivityStatus(int activityId, String status);
    
    /**
     * 增加活动参与者数量
     * @param activityId 活动ID
     * @return 更新成功返回true
     */
    boolean increaseParticipantCount(int activityId);
    
    /**
     * 减少活动参与者数量
     * @param activityId 活动ID
     * @return 更新成功返回true
     */
    boolean decreaseParticipantCount(int activityId);
    
    /**
     * 删除活动
     * @param id 活动ID
     * @return 删除成功返回true
     */
    boolean deleteActivity(int id);
    
    /**
     * 获取活动总数
     * @return 活动总数
     */
    int getActivityCount();
    
    /**
     * 根据社团获取活动数量
     * @param clubId 社团ID
     * @return 活动数量
     */
    int getActivityCountByClub(int clubId);
    
    /**
     * 根据状态获取活动数量
     * @param status 活动状态
     * @return 活动数量
     */
    int getActivityCountByStatus(String status);
    
    /**
     * 搜索活动
     * @param keyword 搜索关键词（活动标题、描述或地点）
     * @return 活动列表
     */
    List<Activity> searchActivities(String keyword);
    
    /**
     * 获取热门活动（按参与者数量排序）
     * @param limit 返回数量限制
     * @return 活动列表
     */
    List<Activity> getPopularActivities(int limit);
    
    /**
     * 获取最新创建的活动
     * @param limit 返回数量限制
     * @return 活动列表
     */
    List<Activity> getNewestActivities(int limit);
    
    /**
     * 检查用户是否已报名活动
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 如果已报名返回true，否则返回false
     */
    boolean isUserRegistered(int activityId, int userId);
    
    /**
     * 用户报名活动
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 报名成功返回true
     */
    boolean registerActivity(int activityId, int userId);
    
    /**
     * 用户取消报名活动
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 取消成功返回true
     */
    boolean cancelRegistration(int activityId, int userId);
    
    /**
     * 获取用户报名的活动
     * @param userId 用户ID
     * @return 活动列表
     */
    List<Activity> getUserRegisteredActivities(int userId);
    
    /**
     * 获取活动的报名用户
     * @param activityId 活动ID
     * @return 用户ID列表
     */
    List<Integer> getActivityRegisteredUsers(int activityId);
    
    /**
     * 获取活动的报名用户数量
     * @param activityId 活动ID
     * @return 报名用户数量
     */
    int getActivityRegistrationCount(int activityId);
}
