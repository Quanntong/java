package com.clubmanagement.common.exception;

/**
 * 系统异常类
 * 用于处理系统级别的异常情况，如数据库连接失败、IO异常等
 */
public class SystemException extends RuntimeException {
    
    /**
     * 构造系统异常
     * @param message 错误消息
     */
    public SystemException(String message) {
        super(message);
    }
    
    /**
     * 构造系统异常
     * @param message 错误消息
     * @param cause 原始异常
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
