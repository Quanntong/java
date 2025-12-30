package com.clubmanagement.common.exception;

/**
 * 业务异常类
 * 用于处理业务逻辑相关的异常情况
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 构造业务异常
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * 构造业务异常
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
