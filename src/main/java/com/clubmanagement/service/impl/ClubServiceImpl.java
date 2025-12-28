package com.clubmanagement.service.impl;

import com.clubmanagement.dao.IClubDAO;
import com.clubmanagement.dao.impl.ClubDAOImpl;
import com.clubmanagement.model.Club;
import com.clubmanagement.service.IClubService;
import com.clubmanagement.service.IUserService;
import com.clubmanagement.service.impl.UserServiceImpl;
import com.clubmanagement.common.Constants;
import com.clubmanagement.common.exception.BusinessException;

import java.util.List;

/**
 * 社团服务实现类
 */
public class ClubServiceImpl implements IClubService {
    
    private IClubDAO clubDAO;
    private IUserService userService;
    
    public ClubServiceImpl() {
        this.clubDAO = new ClubDAOImpl();
        this.userService = new UserServiceImpl();
    }
    
    @Override
    public boolean createClub(Club club, int creatorId) {
        // 参数验证
        if (club == null) {
            throw new BusinessException("社团信息不能为空");
        }
        if (club.getName() == null || club.getName().trim().isEmpty()) {
            throw new BusinessException("社团名称不能为空");
        }
        if (club.getDescription() == null || club.getDescription().trim().isEmpty()) {
            throw new BusinessException("社团描述不能为空");
        }
        if (creatorId <= 0) {
            throw new BusinessException("创建者ID无效");
        }
        
        // 检查创建者是否存在
        if (userService.getUserById(creatorId) == null) {
            throw new BusinessException("创建者不存在");
        }
        
        // 检查社团名称是否已存在
        if (clubDAO.isClubNameExists(club.getName())) {
            throw new BusinessException("社团名称已存在");
        }
        
        // 设置创建者ID和默认值
        club.setCreatorId(creatorId);
        club.setMemberCount(0);
        club.setStatus(Constants.STATUS_PENDING); // 新创建的社团需要审核
        
        // 调用DAO层创建社团
        return clubDAO.createClub(club);
    }
    
    @Override
    public Club getClubById(int id) {
        if (id <= 0) {
            throw new BusinessException("社团ID无效");
        }
        
        Club club = clubDAO.getClubById(id);
        if (club == null) {
            throw new BusinessException("社团不存在");
        }
        
        return club;
    }
    
    @Override
    public Club getClubByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("社团名称不能为空");
        }
        
        Club club = clubDAO.getClubByName(name);
        if (club == null) {
            throw new BusinessException("社团不存在");
        }
        
        return club;
    }
    
    @Override
    public boolean isClubNameExists(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("社团名称不能为空");
        }
        
        return clubDAO.isClubNameExists(name);
    }
    
    @Override
    public List<Club> getAllClubs() {
        return clubDAO.getAllClubs();
    }
    
    @Override
    public List<Club> getClubsByPage(int page, int pageSize) {
        if (page <= 0) {
            throw new BusinessException("页码必须大于0");
        }
        if (pageSize <= 0) {
            throw new BusinessException("每页大小必须大于0");
        }
        
        return clubDAO.getClubsByPage(page, pageSize);
    }
    
    @Override
    public List<Club> getClubsByStatus(int status) {
        if (status < 0 || status > 2) {
            throw new BusinessException("社团状态无效");
        }
        
        return clubDAO.getClubsByStatus(status);
    }
    
    @Override
    public List<Club> getClubsByCreator(int creatorId) {
        if (creatorId <= 0) {
            throw new BusinessException("创建者ID无效");
        }
        
        return clubDAO.getClubsByCreator(creatorId);
    }
    
    @Override
    public boolean updateClub(Club club, int operatorId) {
        // 参数验证
        if (club == null) {
            throw new BusinessException("社团信息不能为空");
        }
        if (club.getId() <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (club.getName() == null || club.getName().trim().isEmpty()) {
            throw new BusinessException("社团名称不能为空");
        }
        if (club.getDescription() == null || club.getDescription().trim().isEmpty()) {
            throw new BusinessException("社团描述不能为空");
        }
        if (operatorId <= 0) {
            throw new BusinessException("操作者ID无效");
        }
        
        // 检查社团是否存在
        Club existingClub = clubDAO.getClubById(club.getId());
        if (existingClub == null) {
            throw new BusinessException("社团不存在");
        }
        
        // 检查操作者权限
        if (!hasClubPermission(club.getId(), operatorId)) {
            throw new BusinessException("没有权限更新该社团");
        }
        
        // 检查社团名称是否被其他社团使用
        Club clubByName = clubDAO.getClubByName(club.getName());
        if (clubByName != null && clubByName.getId() != club.getId()) {
            throw new BusinessException("社团名称已被其他社团使用");
        }
        
        // 调用DAO层更新社团
        return clubDAO.updateClub(club);
    }
    
    @Override
    public boolean updateClubStatus(int clubId, int status, int operatorId) {
        // 参数验证
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (status < 0 || status > 2) {
            throw new BusinessException("社团状态无效");
        }
        if (operatorId <= 0) {
            throw new BusinessException("操作者ID无效");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(clubId);
        if (club == null) {
            throw new BusinessException("社团不存在");
        }
        
        // 检查操作者权限（只有管理员可以修改社团状态）
        if (!userService.isAdmin(operatorId)) {
            throw new BusinessException("只有管理员可以修改社团状态");
        }
        
        // 调用DAO层更新社团状态
        return clubDAO.updateClubStatus(clubId, status);
    }
    
    @Override
    public boolean deleteClub(int id, int operatorId) {
        // 参数验证
        if (id <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (operatorId <= 0) {
            throw new BusinessException("操作者ID无效");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(id);
        if (club == null) {
            throw new BusinessException("社团不存在");
        }
        
        // 检查操作者权限（只有管理员或社团创建者可以删除社团）
        if (!userService.isAdmin(operatorId) && club.getCreatorId() != operatorId) {
            throw new BusinessException("没有权限删除该社团");
        }
        
        // 调用DAO层删除社团
        return clubDAO.deleteClub(id);
    }
    
    @Override
    public int getClubCount() {
        return clubDAO.getClubCount();
    }
    
    @Override
    public int getClubCountByStatus(int status) {
        if (status < 0 || status > 2) {
            throw new BusinessException("社团状态无效");
        }
        
        return clubDAO.getClubCountByStatus(status);
    }
    
    @Override
    public List<Club> searchClubs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException("搜索关键词不能为空");
        }
        
        return clubDAO.searchClubs(keyword);
    }
    
    @Override
    public boolean increaseMemberCount(int clubId) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(clubId);
        if (club == null) {
            throw new BusinessException("社团不存在");
        }
        
        // 调用DAO层增加成员数量
        return clubDAO.increaseMemberCount(clubId);
    }
    
    @Override
    public boolean decreaseMemberCount(int clubId) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(clubId);
        if (club == null) {
            throw new BusinessException("社团不存在");
        }
        
        // 调用DAO层减少成员数量
        return clubDAO.decreaseMemberCount(clubId);
    }
    
    @Override
    public List<Club> getPopularClubs(int limit) {
        if (limit <= 0) {
            throw new BusinessException("数量限制必须大于0");
        }
        
        return clubDAO.getPopularClubs(limit);
    }
    
    @Override
    public List<Club> getNewestClubs(int limit) {
        if (limit <= 0) {
            throw new BusinessException("数量限制必须大于0");
        }
        
        return clubDAO.getNewestClubs(limit);
    }
    
    @Override
    public boolean hasClubPermission(int clubId, int userId) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(clubId);
        if (club == null) {
            return false;
        }
        
        // 管理员拥有所有权限
        if (userService.isAdmin(userId)) {
            return true;
        }
        
        // 社团创建者拥有权限
        if (club.getCreatorId() == userId) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean isClubCreator(int clubId, int userId) {
        if (clubId <= 0) {
            throw new BusinessException("社团ID无效");
        }
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(clubId);
        if (club == null) {
            return false;
        }
        
        return club.getCreatorId() == userId;
    }
    
    @Override
    public List<Club> getUserManageableClubs(int userId) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 管理员可以管理所有社团
        if (userService.isAdmin(userId)) {
            return clubDAO.getAllClubs();
        }
        
        // 普通用户只能管理自己创建的社团
        return clubDAO.getClubsByCreator(userId);
    }
    
    @Override
    public List<Club> getUserJoinedClubs(int userId) {
        // 注意：这个方法需要实现社团成员关系表
        // 由于时间关系，这里返回空列表
        // 实际项目中需要实现社团成员关系表
        throw new BusinessException("功能待实现：需要社团成员关系表");
    }
    
    @Override
    public boolean joinClub(int clubId, int userId) {
        // 注意：这个方法需要实现社团成员关系表
        // 由于时间关系，这里返回false
        // 实际项目中需要实现社团成员关系表
        throw new BusinessException("功能待实现：需要社团成员关系表");
    }
    
    @Override
    public boolean leaveClub(int clubId, int userId) {
        // 注意：这个方法需要实现社团成员关系表
        // 由于时间关系，这里返回false
        // 实际项目中需要实现社团成员关系表
        throw new BusinessException("功能待实现：需要社团成员关系表");
    }
    
    @Override
    public boolean isUserJoinedClub(int clubId, int userId) {
        // 注意：这个方法需要实现社团成员关系表
        // 由于时间关系，这里返回false
        // 实际项目中需要实现社团成员关系表
        throw new BusinessException("功能待实现：需要社团成员关系表");
    }
}
