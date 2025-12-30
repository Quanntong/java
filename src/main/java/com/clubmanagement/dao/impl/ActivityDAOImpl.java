package com.clubmanagement.dao.impl;

import com.clubmanagement.dao.IActivityDAO;
import com.clubmanagement.model.Activity;
import com.clubmanagement.util.DBUtil;
import com.clubmanagement.common.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动数据访问实现类
 */
public class ActivityDAOImpl implements IActivityDAO {
    
    @Override
    public boolean createActivity(Activity activity) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：模拟创建成功
                System.out.println("演示模式: 模拟创建活动 - " + activity.getTitle());
                return true;
            }
            
            String sql = "INSERT INTO activities (title, description, club_id, start_time, end_time, " +
                        "location, max_participants, current_participants, status, registration_deadline, " +
                        "created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, activity.getTitle());
            pstmt.setString(2, activity.getDescription());
            pstmt.setInt(3, activity.getClubId());
            pstmt.setTimestamp(4, new Timestamp(activity.getStartTime().getTime()));
            pstmt.setTimestamp(5, new Timestamp(activity.getEndTime().getTime()));
            pstmt.setString(6, activity.getLocation());
            pstmt.setInt(7, activity.getMaxParticipants());
            pstmt.setInt(8, activity.getCurrentParticipants());
            pstmt.setString(9, activity.getStatus());
            if (activity.getRegistrationDeadline() != null) {
                pstmt.setTimestamp(10, new Timestamp(activity.getRegistrationDeadline().getTime()));
            } else {
                pstmt.setNull(10, Types.TIMESTAMP);
            }
            pstmt.setTimestamp(11, new Timestamp(activity.getCreatedAt().getTime()));
            pstmt.setTimestamp(12, new Timestamp(activity.getUpdatedAt().getTime()));
            
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
    public Activity getActivityById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Activity activity = null;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟活动
                System.out.println("演示模式: 模拟获取活动ID - " + id);
                if (id == 1) {
                    activity = new Activity();
                    activity.setId(1);
                    activity.setTitle("Java编程入门讲座");
                    activity.setDescription("学习Java编程基础");
                    activity.setClubId(1);
                    activity.setClubName("计算机社团");
                    activity.setStartTime(new java.sql.Timestamp(System.currentTimeMillis() + 86400000)); // 明天
                    activity.setEndTime(new java.sql.Timestamp(System.currentTimeMillis() + 90000000)); // 明天+1小时
                    activity.setLocation("教学楼A101");
                    activity.setMaxParticipants(50);
                    activity.setCurrentParticipants(25);
                    activity.setStatus("REGISTERING");
                    activity.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    activity.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                } else if (id == 2) {
                    activity = new Activity();
                    activity.setId(2);
                    activity.setTitle("Python数据分析实战");
                    activity.setDescription("学习Python数据分析技巧");
                    activity.setClubId(1);
                    activity.setClubName("计算机社团");
                    activity.setStartTime(new java.sql.Timestamp(System.currentTimeMillis() + 172800000)); // 后天
                    activity.setEndTime(new java.sql.Timestamp(System.currentTimeMillis() + 176400000)); // 后天+1小时
                    activity.setLocation("实验楼B201");
                    activity.setMaxParticipants(30);
                    activity.setCurrentParticipants(15);
                    activity.setStatus("REGISTERING");
                    activity.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    activity.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                } else if (id == 3) {
                    activity = new Activity();
                    activity.setId(3);
                    activity.setTitle("校园篮球友谊赛");
                    activity.setDescription("校园篮球比赛");
                    activity.setClubId(2);
                    activity.setClubName("篮球社团");
                    activity.setStartTime(new java.sql.Timestamp(System.currentTimeMillis() + 259200000)); // 3天后
                    activity.setEndTime(new java.sql.Timestamp(System.currentTimeMillis() + 262800000)); // 3天后+1小时
                    activity.setLocation("体育馆篮球场");
                    activity.setMaxParticipants(20);
                    activity.setCurrentParticipants(10);
                    activity.setStatus("REGISTERING");
                    activity.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    activity.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                return activity;
            }
            
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id WHERE a.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                activity = extractActivityFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activity;
    }
    
    @Override
    public Activity getActivityByTitle(String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Activity activity = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id WHERE a.title = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                activity = extractActivityFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activity;
    }
    
    @Override
    public boolean isActivityTitleExists(int clubId, String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM activities WHERE club_id = ? AND title = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clubId);
            pstmt.setString(2, title);
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
    public List<Activity> getAllActivities() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟活动数据
                System.out.println("演示模式: 返回模拟活动数据");
                Activity activity1 = new Activity();
                activity1.setId(1);
                activity1.setTitle("Java编程入门讲座");
                activity1.setDescription("学习Java编程基础");
                activity1.setClubId(1);
                activity1.setClubName("计算机社团");
                activity1.setStartTime(new java.sql.Timestamp(System.currentTimeMillis() + 86400000)); // 明天
                activity1.setEndTime(new java.sql.Timestamp(System.currentTimeMillis() + 90000000)); // 明天+1小时
                activity1.setLocation("教学楼A101");
                activity1.setMaxParticipants(50);
                activity1.setCurrentParticipants(25);
                activity1.setStatus("REGISTERING");
                activity1.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                activity1.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                activityList.add(activity1);
                
                Activity activity2 = new Activity();
                activity2.setId(2);
                activity2.setTitle("Python数据分析实战");
                activity2.setDescription("学习Python数据分析技巧");
                activity2.setClubId(1);
                activity2.setClubName("计算机社团");
                activity2.setStartTime(new java.sql.Timestamp(System.currentTimeMillis() + 172800000)); // 后天
                activity2.setEndTime(new java.sql.Timestamp(System.currentTimeMillis() + 176400000)); // 后天+1小时
                activity2.setLocation("实验楼B201");
                activity2.setMaxParticipants(30);
                activity2.setCurrentParticipants(15);
                activity2.setStatus("REGISTERING");
                activity2.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                activity2.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                activityList.add(activity2);
                
                Activity activity3 = new Activity();
                activity3.setId(3);
                activity3.setTitle("校园篮球友谊赛");
                activity3.setDescription("校园篮球比赛");
                activity3.setClubId(2);
                activity3.setClubName("篮球社团");
                activity3.setStartTime(new java.sql.Timestamp(System.currentTimeMillis() + 259200000)); // 3天后
                activity3.setEndTime(new java.sql.Timestamp(System.currentTimeMillis() + 262800000)); // 3天后+1小时
                activity3.setLocation("体育馆篮球场");
                activity3.setMaxParticipants(20);
                activity3.setCurrentParticipants(10);
                activity3.setStatus("REGISTERING");
                activity3.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                activity3.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                activityList.add(activity3);
                
                return activityList;
            }
            
            stmt = conn.createStatement();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id ORDER BY a.created_at DESC";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getActivitiesByPage(int page, int pageSize) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id ORDER BY a.created_at DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getActivitiesByClub(int clubId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id WHERE a.club_id = ? ORDER BY a.created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clubId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getActivitiesByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id WHERE a.status = ? ORDER BY a.created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getRegisterableActivities() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "WHERE a.status = ? AND (a.registration_deadline IS NULL OR a.registration_deadline > ?) " +
                        "AND a.current_participants < a.max_participants ORDER BY a.created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Constants.ACTIVITY_STATUS_REGISTERING);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getOngoingActivities() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "WHERE a.status = ? AND a.start_time <= ? AND a.end_time >= ? ORDER BY a.start_time ASC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Constants.ACTIVITY_STATUS_ONGOING);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(2, now);
            pstmt.setTimestamp(3, now);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getUpcomingActivities(int days) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "WHERE a.start_time BETWEEN ? AND ? ORDER BY a.start_time ASC";
            pstmt = conn.prepareStatement(sql);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp future = new Timestamp(System.currentTimeMillis() + days * 24L * 60 * 60 * 1000);
            pstmt.setTimestamp(1, now);
            pstmt.setTimestamp(2, future);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getActivitiesByDateRange(Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "WHERE a.start_time BETWEEN ? AND ? ORDER BY a.start_time ASC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public boolean updateActivity(Activity activity) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE activities SET title = ?, description = ?, start_time = ?, end_time = ?, " +
                        "location = ?, max_participants = ?, current_participants = ?, status = ?, " +
                        "registration_deadline = ?, updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, activity.getTitle());
            pstmt.setString(2, activity.getDescription());
            pstmt.setTimestamp(3, new Timestamp(activity.getStartTime().getTime()));
            pstmt.setTimestamp(4, new Timestamp(activity.getEndTime().getTime()));
            pstmt.setString(5, activity.getLocation());
            pstmt.setInt(6, activity.getMaxParticipants());
            pstmt.setInt(7, activity.getCurrentParticipants());
            pstmt.setString(8, activity.getStatus());
            if (activity.getRegistrationDeadline() != null) {
                pstmt.setTimestamp(9, new Timestamp(activity.getRegistrationDeadline().getTime()));
            } else {
                pstmt.setNull(9, Types.TIMESTAMP);
            }
            pstmt.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(11, activity.getId());
            
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
    public boolean updateActivityStatus(int activityId, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE activities SET status = ?, updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, activityId);
            
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
    public boolean increaseParticipantCount(int activityId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE activities SET current_participants = current_participants + 1, updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, activityId);
            
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
    public boolean decreaseParticipantCount(int activityId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE activities SET current_participants = GREATEST(0, current_participants - 1), updated_at = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, activityId);
            
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
    public boolean deleteActivity(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM activities WHERE id = ?";
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
    public int getActivityCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM activities";
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
    public int getActivityCountByClub(int clubId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM activities WHERE club_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clubId);
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
    public int getActivityCountByStatus(String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM activities WHERE status = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
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
    public List<Activity> searchActivities(String keyword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "WHERE a.title LIKE ? OR a.description LIKE ? OR a.location LIKE ? " +
                        "ORDER BY a.created_at DESC";
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getPopularActivities(int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "ORDER BY a.current_participants DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Activity> getNewestActivities(int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "ORDER BY a.created_at DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public boolean isUserRegistered(int activityId, int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean registered = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：模拟检查用户是否已报名
                System.out.println("演示模式: 模拟检查用户报名 - 活动ID: " + activityId + ", 用户ID: " + userId);
                // 在演示模式中，假设用户未报名任何活动
                return false;
            }
            
            String sql = "SELECT COUNT(*) FROM activity_registrations WHERE activity_id = ? AND user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                registered = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return registered;
    }
    
    @Override
    public boolean registerActivity(int activityId, int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：模拟报名成功
                System.out.println("演示模式: 模拟报名活动 - 活动ID: " + activityId + ", 用户ID: " + userId);
                return true;
            }
            
            String sql = "INSERT INTO activity_registrations (activity_id, user_id, registered_at) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
            
            // 增加活动参与者数量
            if (success) {
                increaseParticipantCount(activityId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public boolean cancelRegistration(int activityId, int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM activity_registrations WHERE activity_id = ? AND user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            
            int rows = pstmt.executeUpdate();
            success = rows > 0;
            
            // 减少活动参与者数量
            if (success) {
                decreaseParticipantCount(activityId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, null);
        }
        
        return success;
    }
    
    @Override
    public List<Activity> getUserRegisteredActivities(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Activity> activityList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT a.*, c.name as club_name FROM activities a " +
                        "LEFT JOIN clubs c ON a.club_id = c.id " +
                        "INNER JOIN activity_registrations ar ON a.id = ar.activity_id " +
                        "WHERE ar.user_id = ? ORDER BY ar.registered_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Activity activity = extractActivityFromResultSet(rs);
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return activityList;
    }
    
    @Override
    public List<Integer> getActivityRegisteredUsers(int activityId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> userIdList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT user_id FROM activity_registrations WHERE activity_id = ? ORDER BY registered_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, activityId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                userIdList.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return userIdList;
    }
    
    @Override
    public int getActivityRegistrationCount(int activityId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM activity_registrations WHERE activity_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, activityId);
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
    
    /**
     * 从ResultSet中提取Activity对象
     * @param rs ResultSet对象
     * @return Activity对象
     * @throws SQLException 如果数据库操作失败
     */
    private Activity extractActivityFromResultSet(ResultSet rs) throws SQLException {
        Activity activity = new Activity();
        activity.setId(rs.getInt("id"));
        activity.setTitle(rs.getString("title"));
        activity.setDescription(rs.getString("description"));
        activity.setClubId(rs.getInt("club_id"));
        activity.setClubName(rs.getString("club_name"));
        activity.setStartTime(rs.getTimestamp("start_time"));
        activity.setEndTime(rs.getTimestamp("end_time"));
        activity.setLocation(rs.getString("location"));
        activity.setMaxParticipants(rs.getInt("max_participants"));
        activity.setCurrentParticipants(rs.getInt("current_participants"));
        activity.setStatus(rs.getString("status"));
        activity.setRegistrationDeadline(rs.getTimestamp("registration_deadline"));
        activity.setCreatedAt(rs.getTimestamp("created_at"));
        activity.setUpdatedAt(rs.getTimestamp("updated_at"));
        return activity;
    }
}
