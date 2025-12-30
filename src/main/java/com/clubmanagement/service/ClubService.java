package com.clubmanagement.service;

import com.clubmanagement.dao.IClubDAO;
import com.clubmanagement.dao.IUserDAO;
import com.clubmanagement.dao.impl.ClubDAOImpl;
import com.clubmanagement.dao.impl.UserDAOImpl;
import com.clubmanagement.model.Club;
import com.clubmanagement.model.User;

import java.util.List;

/**
 * 社团业务逻辑层
 * 处理社团相关的业务逻辑
 */
public class ClubService {
    private IClubDAO clubDAO;
    private IUserDAO userDAO;
    
    public ClubService() {
        this.clubDAO = new ClubDAOImpl();
        this.userDAO = new UserDAOImpl();
    }
    
    /**
     * 获取所有社团（供界面显示）
     * @return 社团列表
     */
    public List<Club> getAllClubs() {
        return clubDAO.getAllClubs();
    }
    
    /**
     * 创建社团（包含业务验证）
     * @param club 社团对象
     * @return 创建成功返回true
     * @throws IllegalArgumentException 如果参数无效或验证失败
     */
    public boolean createClub(Club club) {
        // 参数验证
        if (club == null) {
            throw new IllegalArgumentException("社团对象不能为空");
        }
        
        String name = club.getName();
        int presidentId = club.getPresidentId();
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("社团名称不能为空");
        }
        if (presidentId <= 0) {
            throw new IllegalArgumentException("社长ID无效");
        }
        
        // 验证社长是否存在
        User president = userDAO.getUserById(presidentId);
        if (president == null) {
            throw new IllegalArgumentException("社长用户不存在: ID=" + presidentId);
        }
        
        // 验证社团名称是否已存在（可选，根据业务需求）
        // 这里假设社团名称可以重复
        
        // 调用DAO层创建社团
        boolean success = clubDAO.createClub(club);
        
        if (success) {
            System.out.println("创建社团成功: " + club.getName());
        } else {
            System.out.println("创建社团失败: " + club.getName());
        }
        
        return success;
    }
    
    /**
     * 根据ID获取社团
     * @param id 社团ID
     * @return 社团对象，如果不存在返回null
     */
    public Club getClubById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("社团ID必须大于0");
        }
        
        return clubDAO.getClubById(id);
    }
    
    /**
     * 更新社团信息
     * @param club 社团对象
     * @return 更新成功返回true
     * @throws IllegalArgumentException 如果参数无效
     */
    public boolean updateClub(Club club) {
        if (club == null) {
            throw new IllegalArgumentException("社团对象不能为空");
        }
        
        if (club.getId() <= 0) {
            throw new IllegalArgumentException("社团ID无效");
        }
        
        String name = club.getName();
        int presidentId = club.getPresidentId();
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("社团名称不能为空");
        }
        if (presidentId <= 0) {
            throw new IllegalArgumentException("社长ID无效");
        }
        
        // 验证社长是否存在
        User president = userDAO.getUserById(presidentId);
        if (president == null) {
            throw new IllegalArgumentException("社长用户不存在: ID=" + presidentId);
        }
        
        // 检查社团是否存在
        Club existingClub = clubDAO.getClubById(club.getId());
        if (existingClub == null) {
            throw new IllegalArgumentException("社团不存在: ID=" + club.getId());
        }
        
        return clubDAO.updateClub(club);
    }
    
    /**
     * 删除社团
     * @param id 社团ID
     * @return 删除成功返回true
     * @throws IllegalArgumentException 如果ID无效或社团不存在
     */
    public boolean deleteClub(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("社团ID必须大于0");
        }
        
        // 检查社团是否存在
        Club club = clubDAO.getClubById(id);
        if (club == null) {
            throw new IllegalArgumentException("社团不存在: ID=" + id);
        }
        
        // 可以添加额外的业务逻辑，比如检查社团是否有活动等
        
        return clubDAO.deleteClub(id);
    }
    
    /**
     * 根据社长ID获取社团
     * @param presidentId 社长ID
     * @return 社团列表
     */
    public List<Club> getClubsByPresidentId(int presidentId) {
        if (presidentId <= 0) {
            throw new IllegalArgumentException("社长ID必须大于0");
        }
        
        // 验证社长是否存在
        User president = userDAO.getUserById(presidentId);
        if (president == null) {
            throw new IllegalArgumentException("社长用户不存在: ID=" + presidentId);
        }
        
        // 使用getClubsByCreator方法，因为presidentId就是creatorId
        return clubDAO.getClubsByCreator(presidentId);
    }
    
    /**
     * 搜索社团
     * @param keyword 搜索关键词
     * @return 符合条件的社团列表
     */
    public List<Club> searchClubs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // 如果关键词为空，返回所有社团
            return clubDAO.getAllClubs();
        }
        
        return clubDAO.searchClubs(keyword.trim());
    }
    
    /**
     * 获取社团数量
     * @return 社团总数
     */
    public int getClubCount() {
        return clubDAO.getClubCount();
    }
    
    /**
     * 验证社团信息
     * @param club 社团对象
     * @return 验证通过返回true，否则返回false
     */
    public boolean validateClub(Club club) {
        if (club == null) return false;
        
        String name = club.getName();
        int presidentId = club.getPresidentId();
        
        // 基本验证
        if (name == null || name.trim().isEmpty()) return false;
        if (presidentId <= 0) return false;
        
        // 名称长度限制
        if (name.trim().length() < 2 || name.trim().length() > 100) return false;
        
        // 验证社长是否存在
        User president = userDAO.getUserById(presidentId);
        if (president == null) return false;
        
        return true;
    }
    
    /**
     * 获取社团详情（包含社长信息）
     * @param clubId 社团ID
     * @return 包含社长信息的社团对象
     */
    public Club getClubWithPresidentInfo(int clubId) {
        Club club = getClubById(clubId);
        if (club != null && club.getPresidentId() > 0) {
            User president = userDAO.getUserById(club.getPresidentId());
            // 可以在这里添加社长信息到club对象中
            // 由于Club实体类没有社长名字字段，这里只是示例
            // 实际项目中可以扩展Club类或创建DTO
        }
        return club;
    }
}
