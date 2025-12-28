package com.clubmanagement.model;

import java.sql.Timestamp;

/**
 * 用户实体类
 * 对应数据库 users 表
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role; // ADMIN 或 STUDENT
    private String realName;
    private String className;
    private Timestamp createdAt;
    
    // 无参构造方法
    public User() {
    }
    
    // 带参数构造方法
    public User(int id, String username, String password, String role, 
                String realName, String className, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.realName = realName;
        this.className = className;
        this.createdAt = createdAt;
    }
    
    // 用于注册的构造方法
    public User(String username, String password, String role, 
                String realName, String className) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.realName = realName;
        this.className = className;
    }
    
    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", realName='" + realName + '\'' +
                ", className='" + className + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
