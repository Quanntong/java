package com.clubmanagement.common;

/**
 * 全局常量类
 * 定义系统使用的常量值
 */
public class Constants {
    
    // 数据库相关常量
    public static final String DB_CONFIG_FILE = "db.properties";
    public static final String DB_DRIVER_KEY = "db.driver";
    public static final String DB_URL_KEY = "db.url";
    public static final String DB_USERNAME_KEY = "db.username";
    public static final String DB_PASSWORD_KEY = "db.password";
    
    // 用户角色常量
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STUDENT = "STUDENT";
    
    // 系统配置常量
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // 业务状态常量
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;
    public static final int STATUS_PENDING = 2;
    
    // 活动状态常量
    public static final String ACTIVITY_STATUS_PLANNING = "PLANNING";      // 策划中
    public static final String ACTIVITY_STATUS_REGISTERING = "REGISTERING"; // 报名中
    public static final String ACTIVITY_STATUS_ONGOING = "ONGOING";        // 进行中
    public static final String ACTIVITY_STATUS_COMPLETED = "COMPLETED";    // 已结束
    public static final String ACTIVITY_STATUS_CANCELLED = "CANCELLED";    // 已取消
    
    // 社团申请状态
    public static final String CLUB_APPLY_PENDING = "PENDING";    // 待审核
    public static final String CLUB_APPLY_APPROVED = "APPROVED";  // 已通过
    public static final String CLUB_APPLY_REJECTED = "REJECTED";  // 已拒绝
    
    // 密码加密相关
    public static final String MD5_ALGORITHM = "MD5";
    public static final String CHARSET_UTF8 = "UTF-8";
    
    // 错误消息常量
    public static final String ERROR_DB_CONNECTION = "数据库连接失败";
    public static final String ERROR_PARAM_REQUIRED = "参数不能为空";
    public static final String ERROR_USER_NOT_FOUND = "用户不存在";
    public static final String ERROR_CLUB_NOT_FOUND = "社团不存在";
    public static final String ERROR_ACTIVITY_NOT_FOUND = "活动不存在";
    public static final String ERROR_PERMISSION_DENIED = "权限不足";
    
    // 成功消息常量
    public static final String SUCCESS_OPERATION = "操作成功";
    public static final String SUCCESS_LOGIN = "登录成功";
    public static final String SUCCESS_REGISTER = "注册成功";
    public static final String SUCCESS_UPDATE = "更新成功";
    public static final String SUCCESS_DELETE = "删除成功";
    
    private Constants() {
        // 防止实例化
    }
}
