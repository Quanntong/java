package com.clubmanagement.dao;

import com.clubmanagement.model.Club;

import java.util.List;

/**
 * 社团数据访问接口
 * 定义社团相关的数据库操作
 */
public interface IClubDAO {
    
    /**
     * 创建社团
     * @param club 社团对象
     * @return 创建成功返回true，失败返回false
     */
    boolean createClub(Club club);
    
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
     * @return 更新成功返回true
     */
    boolean updateClub(Club club);
    
    /**
     * 更新社团状态
     * @param clubId 社团ID
     * @param status 新状态
     * @return 更新成功返回true
     */
    boolean updateClubStatus(int clubId, int status);
    
    /**
     * 删除社团
     * @param id 社团ID
     * @return 删除成功返回true
     */
    boolean deleteClub(int id);
    
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
}
