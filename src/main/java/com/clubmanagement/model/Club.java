package com.clubmanagement.model;

import com.clubmanagement.common.Constants;
import com.clubmanagement.util.DateUtil;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 社团实体类
 * 对应数据库 clubs 表
 */
public class Club {
    private int id;
    private String name;
    private String description;
    private int creatorId; // 创建者ID，关联用户表
    private int memberCount; // 成员数量
    private int status; // 社团状态：0-禁用，1-正常，2-审核中
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // 无参构造方法
    public Club() {
        this.status = Constants.STATUS_ACTIVE;
        this.memberCount = 0;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    // 带参数构造方法
    public Club(String name, String description, int creatorId) {
        this();
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
    }
    
    // 全参数构造方法
    public Club(int id, String name, String description, int creatorId, 
                int memberCount, int status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.memberCount = memberCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
    
    // 兼容性方法：getPresidentId 等同于 getCreatorId
    public int getPresidentId() {
        return creatorId;
    }
    
    public void setPresidentId(int presidentId) {
        this.creatorId = presidentId;
    }
    
    public int getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 业务方法
    
    /**
     * 检查社团是否活跃
     * @return 如果社团状态为活跃返回true，否则返回false
     */
    public boolean isActive() {
        return this.status == Constants.STATUS_ACTIVE;
    }
    
    /**
     * 检查社团是否待审核
     * @return 如果社团状态为待审核返回true，否则返回false
     */
    public boolean isPending() {
        return this.status == Constants.STATUS_PENDING;
    }
    
    /**
     * 检查社团是否已禁用
     * @return 如果社团状态为禁用返回true，否则返回false
     */
    public boolean isInactive() {
        return this.status == Constants.STATUS_INACTIVE;
    }
    
    /**
     * 增加成员数量
     * @return 如果增加成功返回true，否则返回false
     */
    public boolean addMember() {
        this.memberCount++;
        return true;
    }
    
    /**
     * 减少成员数量
     * @return 如果减少成功返回true，否则返回false
     */
    public boolean removeMember() {
        if (this.memberCount > 0) {
            this.memberCount--;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId=" + creatorId +
                ", memberCount=" + memberCount +
                ", status=" + status +
                ", createdAt=" + DateUtil.format(new Date(createdAt.getTime())) +
                ", updatedAt=" + DateUtil.format(new Date(updatedAt.getTime())) +
                '}';
    }
}
