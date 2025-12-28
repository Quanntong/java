package com.clubmanagement.dao.impl;

import com.clubmanagement.dao.IClubDAO;
import com.clubmanagement.model.Club;
import com.clubmanagement.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 社团数据访问实现类
 */
public class ClubDAOImpl implements IClubDAO {
    
    @Override
    public boolean createClub(Club club) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO clubs (name, description, creator_id, member_count, status, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setInt(3, club.getCreatorId());
            pstmt.setInt(4, club.getMemberCount());
            pstmt.setInt(5, club.getStatus());
            pstmt.setTimestamp(6, new Timestamp(club.getCreatedAt().getTime()));
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public Club getClubById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Club club = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return club;
    }
    
    @Override
    public Club getClubByName(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Club club = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return club;
    }
    
    @Override
    public boolean isClubNameExists(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM clubs WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return exists;
    }
    
    @Override
    public List<Club> getAllClubs() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs ORDER BY created_at DESC";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        
        return clubList;
    }
    
    @Override
    public List<Club> getClubsByPage(int page, int pageSize) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs ORDER BY created_at DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    @Override
    public List<Club> getClubsByStatus(int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs WHERE status = ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    @Override
    public List<Club> getClubsByCreator(int creatorId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs WHERE creator_id = ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, creatorId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    @Override
    public boolean updateClub(Club club) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE clubs SET name = ?, description = ?, member_count = ?, status = ?, updated_at = ? " +
                        "WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setInt(3, club.getMemberCount());
            pstmt.setInt(4, club.getStatus());
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(6, club.getId());
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public boolean updateClubStatus(int clubId, int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE clubs SET status = ?, updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, clubId);
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public boolean deleteClub(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
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
    
    @Override
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
    
    @Override
    public int getClubCountByStatus(int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM clubs WHERE status = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return count;
    }
    
    @Override
    public List<Club> searchClubs(String keyword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
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
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    @Override
    public boolean increaseMemberCount(int clubId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE clubs SET member_count = member_count + 1, updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, clubId);
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public boolean decreaseMemberCount(int clubId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE clubs SET member_count = GREATEST(0, member_count - 1), updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, clubId);
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public List<Club> getPopularClubs(int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs ORDER BY member_count DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
    
    @Override
    public List<Club> getNewestClubs(int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Club> clubList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, creator_id, member_count, status, created_at, updated_at " +
                        "FROM clubs ORDER BY created_at DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Club club = new Club();
                club.setId(rs.getInt("id"));
                club.setName(rs.getString("name"));
                club.setDescription(rs.getString("description"));
                club.setCreatorId(rs.getInt("creator_id"));
                club.setMemberCount(rs.getInt("member_count"));
                club.setStatus(rs.getInt("status"));
                club.setCreatedAt(rs.getTimestamp("created_at"));
                club.setUpdatedAt(rs.getTimestamp("updated_at"));
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return clubList;
    }
}
