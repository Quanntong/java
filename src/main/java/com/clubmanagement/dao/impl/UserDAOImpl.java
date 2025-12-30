package com.clubmanagement.dao.impl;

import com.clubmanagement.dao.IUserDAO;
import com.clubmanagement.model.User;
import com.clubmanagement.util.DBUtil;
import com.clubmanagement.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问实现类
 */
public class UserDAOImpl implements IUserDAO {
    
    @Override
    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟用户
                System.out.println("演示模式: 模拟用户登录 - " + username);
                if ("admin".equals(username) && "123456".equals(password)) {
                    user = new User();
                    user.setId(1);
                    user.setUsername("admin");
                    user.setPassword(SecurityUtil.md5("123456"));
                    user.setRole("ADMIN");
                    user.setRealName("管理员");
                    user.setClassName("计算机科学");
                    user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                } else if ("student".equals(username) && "123456".equals(password)) {
                    user = new User();
                    user.setId(2);
                    user.setUsername("student");
                    user.setPassword(SecurityUtil.md5("123456"));
                    user.setRole("STUDENT");
                    user.setRealName("张三");
                    user.setClassName("计算机科学2023");
                    user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                return user;
            }
            
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, SecurityUtil.md5(password)); // 密码加密后比较
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return user;
    }
    
    @Override
    public boolean register(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：模拟注册成功
                System.out.println("演示模式: 模拟用户注册 - " + user.getUsername());
                return true;
            }
            
            String sql = "INSERT INTO users (username, password, role, real_name, class_name) " +
                        "VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, SecurityUtil.md5(user.getPassword())); // 密码加密存储
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getRealName());
            pstmt.setString(5, user.getClassName());
            
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
    public User getUserById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟用户
                System.out.println("演示模式: 模拟获取用户ID - " + id);
                if (id == 1) {
                    user = new User();
                    user.setId(1);
                    user.setUsername("admin");
                    user.setPassword(SecurityUtil.md5("123456"));
                    user.setRole("ADMIN");
                    user.setRealName("管理员");
                    user.setClassName("计算机科学");
                    user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                } else if (id == 2) {
                    user = new User();
                    user.setId(2);
                    user.setUsername("student");
                    user.setPassword(SecurityUtil.md5("123456"));
                    user.setRole("STUDENT");
                    user.setRealName("张三");
                    user.setClassName("计算机科学2023");
                    user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                return user;
            }
            
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return user;
    }
    
    @Override
    public User getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：返回模拟用户
                System.out.println("演示模式: 模拟获取用户名 - " + username);
                if ("admin".equals(username)) {
                    user = new User();
                    user.setId(1);
                    user.setUsername("admin");
                    user.setPassword(SecurityUtil.md5("123456"));
                    user.setRole("ADMIN");
                    user.setRealName("管理员");
                    user.setClassName("计算机科学");
                    user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                } else if ("student".equals(username)) {
                    user = new User();
                    user.setId(2);
                    user.setUsername("student");
                    user.setPassword(SecurityUtil.md5("123456"));
                    user.setRole("STUDENT");
                    user.setRealName("张三");
                    user.setClassName("计算机科学2023");
                    user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                return user;
            }
            
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return user;
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 演示模式：检查是否为预定义用户
                System.out.println("演示模式: 检查用户名是否存在 - " + username);
                return "admin".equals(username) || "student".equals(username);
            }
            
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
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
    public List<User> getAllUsers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users ORDER BY created_at DESC";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        
        return userList;
    }
    
    @Override
    public List<User> getUsersByPage(int page, int pageSize) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users ORDER BY created_at DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return userList;
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users WHERE role = ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return userList;
    }
    
    @Override
    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET username = ?, password = ?, role = ?, " +
                        "real_name = ?, class_name = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, SecurityUtil.md5(user.getPassword())); // 密码加密更新
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getRealName());
            pstmt.setString(5, user.getClassName());
            pstmt.setInt(6, user.getId());
            
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
    public boolean updatePassword(int userId, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, SecurityUtil.md5(newPassword)); // 密码加密更新
            pstmt.setInt(2, userId);
            
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
    public boolean deleteUser(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM users WHERE id = ?";
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
    public int getUserCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM users";
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
    public int getUserCountByRole(String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
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
    public List<User> searchUsers(String keyword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, role, real_name, class_name, created_at " +
                        "FROM users WHERE username LIKE ? OR real_name LIKE ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setRealName(rs.getString("real_name"));
                user.setClassName(rs.getString("class_name"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, pstmt, rs);
        }
        
        return userList;
    }
}
