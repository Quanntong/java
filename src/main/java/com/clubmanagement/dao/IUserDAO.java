package com.clubmanagement.dao;

import com.clubmanagement.model.User;

import java.util.List;

/**
 * 用户数据访问接口
 * 定义用户相关的数据库操作
 */
public interface IUserDAO {
    
    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象，如果登录失败返回null
     */
    User login(String username, String password);
    
    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册成功返回true，失败返回false
     */
    boolean register(User user);
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(int id);
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    boolean isUsernameExists(String username);
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();
    
    /**
     * 分页获取用户
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 用户列表
     */
    List<User> getUsersByPage(int page, int pageSize);
    
    /**
     * 根据角色获取用户
     * @param role 用户角色
     * @return 用户列表
     */
    List<User> getUsersByRole(String role);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true
     */
    boolean updateUser(User user);
    
    /**
     * 更新用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 更新成功返回true
     */
    boolean updatePassword(int userId, String newPassword);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除成功返回true
     */
    boolean deleteUser(int id);
    
    /**
     * 获取用户总数
     * @return 用户总数
     */
    int getUserCount();
    
    /**
     * 根据角色获取用户数量
     * @param role 用户角色
     * @return 用户数量
     */
    int getUserCountByRole(String role);
    
    /**
     * 搜索用户
     * @param keyword 搜索关键词（用户名或真实姓名）
     * @return 用户列表
     */
    List<User> searchUsers(String keyword);
}
