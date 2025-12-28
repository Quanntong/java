package com.clubmanagement.service;

import com.clubmanagement.dao.UserDAO;
import com.clubmanagement.model.User;

import java.util.List;

/**
 * 用户业务逻辑层
 * 处理用户相关的业务逻辑
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * 用户登录业务逻辑
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象，失败返回null
     */
    public User login(String username, String password) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 调用DAO层进行登录验证
        User user = userDAO.login(username.trim(), password.trim());
        
        if (user == null) {
            // 可以记录日志或进行其他处理
            System.out.println("登录失败: 用户名或密码错误 - " + username);
        } else {
            System.out.println("登录成功: " + user.getUsername() + " (" + user.getRole() + ")");
        }
        
        return user;
    }
    
    /**
     * 用户注册业务逻辑
     * @param user 用户对象
     * @return 注册成功返回true，失败返回false
     * @throws IllegalArgumentException 如果参数无效或用户名已存在
     */
    public boolean register(User user) {
        // 参数验证
        if (user == null) {
            throw new IllegalArgumentException("用户对象不能为空");
        }
        
        String username = user.getUsername();
        String password = user.getPassword();
        String realName = user.getRealName();
        
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (realName == null || realName.trim().isEmpty()) {
            throw new IllegalArgumentException("真实姓名不能为空");
        }
        
        // 检查用户名是否已存在
        if (userDAO.isUsernameExists(username.trim())) {
            throw new IllegalArgumentException("用户名已存在: " + username);
        }
        
        // 设置默认角色（如果未指定）
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("STUDENT");
        }
        
        // 调用DAO层进行注册
        boolean success = userDAO.register(user);
        
        if (success) {
            System.out.println("注册成功: " + user.getUsername());
        } else {
            System.out.println("注册失败: " + user.getUsername());
        }
        
        return success;
    }
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象，如果不存在返回null
     */
    public User getUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("用户ID必须大于0");
        }
        
        return userDAO.getUserById(id);
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true
     * @throws IllegalArgumentException 如果参数无效
     */
    public boolean updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用户对象不能为空");
        }
        
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }
        
        // 检查用户名是否被其他用户使用（除了当前用户）
        User existingUser = userDAO.getUserById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("用户不存在: ID=" + user.getId());
        }
        
        // 如果用户名被修改，检查新用户名是否已被其他用户使用
        if (!existingUser.getUsername().equals(user.getUsername())) {
            if (userDAO.isUsernameExists(user.getUsername())) {
                throw new IllegalArgumentException("用户名已被其他用户使用: " + user.getUsername());
            }
        }
        
        return userDAO.updateUser(user);
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除成功返回true
     * @throws IllegalArgumentException 如果ID无效
     */
    public boolean deleteUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("用户ID必须大于0");
        }
        
        // 检查用户是否存在
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在: ID=" + id);
        }
        
        // 不能删除管理员账号（根据业务需求）
        if ("ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("不能删除管理员账号");
        }
        
        return userDAO.deleteUser(id);
    }
    
    /**
     * 检查用户名是否可用
     * @param username 用户名
     * @return 可用返回true，已存在返回false
     */
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        
        return !userDAO.isUsernameExists(username.trim());
    }
    
    /**
     * 验证用户信息
     * @param user 用户对象
     * @return 验证通过返回true，否则返回false
     */
    private boolean validateUser(User user) {
        if (user == null) return false;
        
        String username = user.getUsername();
        String password = user.getPassword();
        String realName = user.getRealName();
        
        // 基本验证
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        if (realName == null || realName.trim().isEmpty()) return false;
        
        // 用户名长度限制
        if (username.trim().length() < 3 || username.trim().length() > 50) return false;
        
        // 密码长度限制
        if (password.trim().length() < 6 || password.trim().length() > 100) return false;
        
        return true;
    }
}
