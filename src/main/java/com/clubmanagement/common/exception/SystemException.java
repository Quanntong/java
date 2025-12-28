package com.clubmanagement.common.exception;

/**
 * 系统异常类
 * 用于处理系统级别的异常情况，如数据库连接失败、IO异常等
 */
public class SystemException extends RuntimeException {
    
    private String errorCode;
    private String errorMessage;
    
    /**
     * 构造系统异常
     * @param errorMessage 错误消息
     */
    public SystemException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    
    /**
     * 构造系统异常
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     */
    public SystemException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * 构造系统异常
     * @param errorMessage 错误消息
     * @param cause 原始异常
     */
    public SystemException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
    }
    
    /**
     * 构造系统异常
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     * @param cause 原始异常
     */
    public SystemException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * 获取错误代码
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * 获取错误消息
     * @return 错误消息
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 设置错误代码
     * @param errorCode 错误代码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * 设置错误消息
     * @param errorMessage 错误消息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SystemException{");
        if (errorCode != null) {
            sb.append("errorCode='").append(errorCode).append('\'');
        }
        if (errorMessage != null) {
            if (errorCode != null) {
                sb.append(", ");
            }
            sb.append("errorMessage='").append(errorMessage).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
