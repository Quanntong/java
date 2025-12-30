package com.clubmanagement.dao;

import com.clubmanagement.model.Club;
import com.clubmanagement.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 社团数据访问对象
 * 负责 clubs 表的 CRUD 操作
 */
public class ClubDAO {
    
    /**
     * 获取所有社团
     * @return 社团列表
     */
    public List<Club> getAllClubs() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟社团数据
                System.out.println("演示模式: 返回模拟社团数据");
                Club club1 = new Club();
                club1.setId(1);
                club1.setName("计算机社团");
                club1.setDescription("学习计算机技术的社团");
                club1.setPresidentId(1);
                club1.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                clubList.add(club1);
                
                Club club2 = new Club();
                club2.setId(2);
                club2.setName("篮球社团");
                club2.setDescription("热爱篮球运动的社团");
                club2.setPresidentId(2);
                club2.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                clubList.add(club2);
                
                return clubList;
            }
            
            stmt = conn.createStatement();
            String sql = "SELECT id, name, description, president_id, created_at " +
                        "FROM clubs ORDER BY created_at DESC";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setPresidentId(rs.getInt("president_id"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        
        return clubList;
    }
    
    /**
     * 创建社团
     * @param club 社团对象
     * @return 创建成功返回true
     */
    public boolean createClub(Club club) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：模拟创建成功
                System.out.println("演示模式: 模拟创建社团 - " + club.getName());
                return true;
            }
            
            String sql = "INSERT INTO clubs (name, description, president_id) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setInt(3, club.getPresidentId());
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 根据ID获取社团
     * @param id 社团ID
     * @return 社团对象
     */
    public Club getClubById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Club club = null;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟社团
                System.out.println("演示模式: 模拟获取社团ID - " + id);
                if (id == 1) {
                    club = new Club();
                    club.setId(1);
                    club.setName("计算机社团");
                    club.setDescription("学习计算机技术的社团");
                    club.setPresidentId(1);
                    club.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                } else if (id == 2) {
                    club = new Club();
                    club.setId(2);
                    club.setName("篮球社团");
                    club.setDescription("热爱篮球运动的社团");
                    club.setPresidentId(2);
                    club.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                return club;
            }
            
            String sql = "SELECT id, name, description, president_id, created_at " +
                        "FROM clubs WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setPresidentId(rs.getInt("president_id"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return club;
    }
    
    /**
     * 根据社长ID获取社团
     * @param presidentId 社长ID
     * @return 社团列表
     */
    public List<Club> getClubsByPresidentId(int presidentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, president_id, created_at " +
                        "FROM clubs WHERE president_id = ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, presidentId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setPresidentId(rs.getInt("president_id"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    /**
     * 更新社团信息
     * @param club 社团对象
     * @return 更新成功返回true
     */
    public boolean updateClub(Club club) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE clubs SET name = ?, description = ?, president_id = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setInt(3, club.getPresidentId());
            pstmt.setInt(4, club.getId());
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 删除社团
     * @param id 社团ID
     * @return 删除成功返回true
     */
    public boolean deleteClub(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：模拟删除成功
                System.out.println("演示模式: 模拟删除社团 - ID: " + id);
                return true;
            }
            
            String sql = "DELETE FROM clubs WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 搜索社团（按名称模糊搜索）
     * @param keyword 关键词
     * @return 社团列表
     */
    public List<Club> searchClubs(String keyword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, president_id, created_at " +
                        "FROM clubs WHERE name LIKE ? OR description LIKE ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setPresidentId(rs.getInt("president_id"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    /**
     * 获取社团数量
     * @return 社团总数
     */
    public int getClubCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM clubs";
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        
        return count;
    }
}
