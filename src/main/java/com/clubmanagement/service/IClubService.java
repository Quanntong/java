package com.clubmanagement.service;

import com.clubmanagement.model.Club;

import java.util.List;

/**
 * 社团服务接口
 * 定义社团相关的业务逻辑操作
 */
public interface IClubService {
    
    /**
     * 创建社团
     * @param club 社团对象
     * @param creatorId 创建者ID
     * @return 创建成功返回true，失败返回false
     */
    boolean createClub(Club club, int creatorId);
    
    /**
     * 根据ID获取社团
     * @param id 社团ID
     * @return 社团对象
     */
    Club getClubById(int id);
    
    /**
     * 根据名称获取社团
     * @param name 社团名称
     * @return 社团对象
     */
    Club getClubByName(String name);
    
    /**
     * 检查社团名称是否已存在
     * @param name 社团名称
     * @return 存在返回true，不存在返回false
     */
    boolean isClubNameExists(String name);
    
    /**
     * 获取所有社团
     * @return 社团列表
     */
    List<Club> getAllClubs();
    
    /**
     * 分页获取社团
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 社团列表
     */
    List<Club> getClubsByPage(int page, int pageSize);
    
    /**
     * 根据状态获取社团
     * @param status 社团状态
     * @return 社团列表
     */
    List<Club> getClubsByStatus(int status);
    
    /**
     * 根据创建者获取社团
     * @param creatorId 创建者ID
     * @return 社团列表
     */
    List<Club> getClubsByCreator(int creatorId);
    
    /**
     * 更新社团信息
     * @param club 社团对象
     * @param operatorId 操作者ID
     * @return 更新成功返回true
     */
    boolean updateClub(Club club, int operatorId);
    
    /**
     * 更新社团状态
     * @param clubId 社团ID
     * @param status 新状态
     * @param operatorId 操作者ID
     * @return 更新成功返回true
     */
    boolean updateClubStatus(int clubId, int status, int operatorId);
    
    /**
     * 删除社团
     * @param id 社团ID
     * @param operatorId 操作者ID
     * @return 删除成功返回true
     */
    boolean deleteClub(int id, int operatorId);
    
    /**
     * 获取社团总数
     * @return 社团总数
     */
    int getClubCount();
    
    /**
     * 根据状态获取社团数量
     * @param status 社团状态
     * @return 社团数量
     */
    int getClubCountByStatus(int status);
    
    /**
     * 搜索社团
     * @param keyword 搜索关键词（社团名称或描述）
     * @return 社团列表
     */
    List<Club> searchClubs(String keyword);
    
    /**
     * 增加社团成员数量
     * @param clubId 社团ID
     * @return 更新成功返回true
     */
    boolean increaseMemberCount(int clubId);
    
    /**
     * 减少社团成员数量
     * @param clubId 社团ID
     * @return 更新成功返回true
     */
    boolean decreaseMemberCount(int clubId);
    
    /**
     * 获取热门社团（按成员数量排序）
     * @param limit 返回数量限制
     * @return 社团列表
     */
    List<Club> getPopularClubs(int limit);
    
    /**
     * 获取最新创建的社团
     * @param limit 返回数量限制
     * @return 社团列表
     */
    List<Club> getNewestClubs(int limit);
    
    /**
     * 检查用户是否有权限管理社团
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 如果有权限返回true，否则返回false
     */
    boolean hasClubPermission(int clubId, int userId);
    
    /**
     * 检查用户是否为社团创建者
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 如果是创建者返回true，否则返回false
     */
    boolean isClubCreator(int clubId, int userId);
    
    /**
     * 获取用户可管理的社团
     * @param userId 用户ID
     * @return 社团列表
     */
    List<Club> getUserManageableClubs(int userId);
    
    /**
     * 获取用户加入的社团
     * @param userId 用户ID
     * @return 社团列表
     */
    List<Club> getUserJoinedClubs(int userId);
    
    /**
     * 用户加入社团
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 加入成功返回true
     */
    boolean joinClub(int clubId, int userId);
    
    /**
     * 用户退出社团
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 退出成功返回true
     */
    boolean leaveClub(int clubId, int userId);
    
    /**
     * 检查用户是否已加入社团
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 如果已加入返回true，否则返回false
     */
    boolean isUserJoinedClub(int clubId, int userId);
}
