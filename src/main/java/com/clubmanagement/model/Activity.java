package com.clubmanagement.model;

import com.clubmanagement.common.Constants;
import com.clubmanagement.util.DateUtil;

import java.util.Date;

/**
 * 活动实体类
 * 对应数据库中的activities表
 */
public class Activity {
    private int id;
    private String title;
    private String description;
    private int clubId;
    private String clubName; // 非数据库字段，用于显示
    private Date startTime;
    private Date endTime;
    private String location;
    private int maxParticipants;
    private int currentParticipants;
    private String status;
    private Date registrationDeadline;
    private Date createdAt;
    private Date updatedAt;
    
    // 无参构造方法
    public Activity() {
        this.status = Constants.ACTIVITY_STATUS_PLANNING;
        this.currentParticipants = 0;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    // 带参数的构造方法
    public Activity(String title, String description, int clubId, Date startTime, 
                   Date endTime, String location, int maxParticipants) {
        this();
        this.title = title;
        this.description = description;
        this.clubId = clubId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
    }
    
    // 全参数构造方法
    public Activity(int id, String title, String description, int clubId, 
                   Date startTime, Date endTime, String location, 
                   int maxParticipants, int currentParticipants, String status,
                   Date registrationDeadline, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.clubId = clubId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.status = status;
        this.registrationDeadline = registrationDeadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getter和Setter方法
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public String getClubName() {
        return clubName;
    }
    
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public int getMaxParticipants() {
        return maxParticipants;
    }
    
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    
    public int getCurrentParticipants() {
        return currentParticipants;
    }
    
    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }
    
    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 业务方法
    
    /**
     * 检查活动是否可报名
     * @return 如果可报名返回true，否则返回false
     */
    public boolean isRegisterable() {
        Date now = new Date();
        return Constants.ACTIVITY_STATUS_REGISTERING.equals(status) &&
               (registrationDeadline == null || now.before(registrationDeadline)) &&
               currentParticipants < maxParticipants;
    }
    
    /**
     * 检查活动是否已满
     * @return 如果已满返回true，否则返回false
     */
    public boolean isFull() {
        return currentParticipants >= maxParticipants;
    }
    
    /**
     * 检查活动是否已开始
     * @return 如果已开始返回true，否则返回false
     */
    public boolean isStarted() {
        Date now = new Date();
        return startTime != null && now.after(startTime);
    }
    
    /**
     * 检查活动是否已结束
     * @return 如果已结束返回true，否则返回false
     */
    public boolean isEnded() {
        Date now = new Date();
        return endTime != null && now.after(endTime);
    }
    
    /**
     * 获取活动剩余名额
     * @return 剩余名额
     */
    public int getRemainingSlots() {
        return Math.max(0, maxParticipants - currentParticipants);
    }
    
    /**
     * 增加参与者数量
     * @return 如果增加成功返回true，否则返回false
     */
    public boolean addParticipant() {
        if (currentParticipants < maxParticipants) {
            currentParticipants++;
            return true;
        }
        return false;
    }
    
    /**
     * 减少参与者数量
     * @return 如果减少成功返回true，否则返回false
     */
    public boolean removeParticipant() {
        if (currentParticipants > 0) {
            currentParticipants--;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", clubId=" + clubId +
                ", startTime=" + DateUtil.format(startTime) +
                ", endTime=" + DateUtil.format(endTime) +
                ", location='" + location + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", currentParticipants=" + currentParticipants +
                ", status='" + status + '\'' +
                '}';
    }
}
