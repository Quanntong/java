package com.clubmanagement.service.impl;

import com.clubmanagement.dao.IUserDAO;
import com.clubmanagement.dao.impl.UserDAOImpl;
import com.clubmanagement.model.User;
import com.clubmanagement.service.IUserService;
import com.clubmanagement.common.Constants;
import com.clubmanagement.common.exception.BusinessException;
import com.clubmanagement.util.SecurityUtil;

import java.util.List;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements IUserService {
    
    private IUserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User login(String username, String password) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        
        // 调用DAO层进行登录验证
        User user = userDAO.login(username, password);
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        return user;
    }
    
    @Override
    public boolean register(User user) {
        // 参数验证
        if (user == null) {
            throw new BusinessException("用户信息不能为空");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            throw new BusinessException("真实姓名不能为空");
        }
        
        // 检查用户名是否已存在
        if (userDAO.isUsernameExists(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 设置默认角色（学生）
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole(Constants.ROLE_STUDENT);
        }
        
        // 调用DAO层进行注册
        return userDAO.register(user);
    }
    
    @Override
    public User getUserById(int id) {
        if (id <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        return user;
    }
    
    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        return user;
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        
        return userDAO.isUsernameExists(username);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    @Override
    public List<User> getUsersByPage(int page, int pageSize) {
        if (page <= 0) {
            throw new BusinessException("页码必须大于0");
        }
        if (pageSize <= 0) {
            throw new BusinessException("每页大小必须大于0");
        }
        
        return userDAO.getUsersByPage(page, pageSize);
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new BusinessException("角色不能为空");
        }
        
        return userDAO.getUsersByRole(role);
    }
    
    @Override
    public boolean updateUser(User user) {
        // 参数验证
        if (user == null) {
            throw new BusinessException("用户信息不能为空");
        }
        if (user.getId() <= 0) {
            throw new BusinessException("用户ID无效");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            throw new BusinessException("真实姓名不能为空");
        }
        
        // 检查用户是否存在
        User existingUser = userDAO.getUserById(user.getId());
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名是否被其他用户使用
        User userByUsername = userDAO.getUserByUsername(user.getUsername());
        if (userByUsername != null && userByUsername.getId() != user.getId()) {
            throw new BusinessException("用户名已被其他用户使用");
        }
        
        // 调用DAO层进行更新
        return userDAO.updateUser(user);
    }
    
    @Override
    public boolean updatePassword(int userId, String oldPassword, String newPassword) {
        // 参数验证
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new BusinessException("旧密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }
        if (newPassword.length() < 6) {
            throw new BusinessException("新密码长度不能少于6位");
        }
        
        // 检查用户是否存在
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证旧密码
        String encryptedOldPassword = SecurityUtil.md5(oldPassword);
        if (!user.getPassword().equals(encryptedOldPassword)) {
            throw new BusinessException("旧密码错误");
        }
        
        // 更新密码
        return userDAO.updatePassword(userId, newPassword);
    }
    
    @Override
    public boolean resetPassword(int userId, String newPassword) {
        // 参数验证
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }
        if (newPassword.length() < 6) {
            throw new BusinessException("新密码长度不能少于6位");
        }
        
        // 检查用户是否存在
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 重置密码
        return userDAO.updatePassword(userId, newPassword);
    }
    
    @Override
    public boolean deleteUser(int id) {
        if (id <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        // 检查用户是否存在
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 调用DAO层进行删除
        return userDAO.deleteUser(id);
    }
    
    @Override
    public int getUserCount() {
        return userDAO.getUserCount();
    }
    
    @Override
    public int getUserCountByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new BusinessException("角色不能为空");
        }
        
        return userDAO.getUserCountByRole(role);
    }
    
    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException("搜索关键词不能为空");
        }
        
        return userDAO.searchUsers(keyword);
    }
    
    @Override
    public boolean hasPermission(int userId, String requiredRole) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        if (requiredRole == null || requiredRole.trim().isEmpty()) {
            throw new BusinessException("所需角色不能为空");
        }
        
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return false;
        }
        
        // 检查用户角色
        String userRole = user.getRole();
        
        // 管理员拥有所有权限
        if (Constants.ROLE_ADMIN.equals(userRole)) {
            return true;
        }
        
        // 检查是否匹配所需角色
        return requiredRole.equals(userRole);
    }
    
    @Override
    public boolean isAdmin(int userId) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return false;
        }
        
        return Constants.ROLE_ADMIN.equals(user.getRole());
    }
    
    @Override
    public boolean isStudent(int userId) {
        if (userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return false;
        }
        
        return Constants.ROLE_STUDENT.equals(user.getRole());
    }
}
