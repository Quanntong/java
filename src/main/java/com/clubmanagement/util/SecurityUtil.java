package com.clubmanagement.util;

import com.clubmanagement.common.Constants;
import com.clubmanagement.common.exception.SystemException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 安全工具类
 * 提供密码加密、验证等安全相关功能
 */
public class SecurityUtil {
    
    /**
     * 私有构造方法，防止实例化
     */
    private SecurityUtil() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }
    
    /**
     * 使用MD5算法加密字符串
     * @param input 要加密的字符串
     * @return MD5加密后的字符串（32位小写）
     */
    public static String md5(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("输入字符串不能为空");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance(Constants.MD5_ALGORITHM);
            byte[] digest = md.digest(input.getBytes(Constants.CHARSET_UTF8));
            
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException("MD5算法不可用", e);
        } catch (Exception e) {
            throw new SystemException("MD5加密失败", e);
        }
    }
    
    /**
     * 使用MD5算法加密字符串，并返回Base64编码
     * @param input 要加密的字符串
     * @return Base64编码的MD5加密结果
     */
    public static String md5Base64(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("输入字符串不能为空");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance(Constants.MD5_ALGORITHM);
            byte[] digest = md.digest(input.getBytes(Constants.CHARSET_UTF8));
            
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException("MD5算法不可用", e);
        } catch (Exception e) {
            throw new SystemException("MD5加密失败", e);
        }
    }
    
    /**
     * 验证MD5加密的密码
     * @param input 用户输入的原始密码
     * @param encrypted 数据库中存储的加密密码
     * @return 如果匹配返回true，否则返回false
     */
    public static boolean verifyMd5(String input, String encrypted) {
        if (input == null || encrypted == null) {
            return false;
        }
        
        String inputEncrypted = md5(input);
        return inputEncrypted.equals(encrypted);
    }
    
    /**
     * 生成随机盐值
     * @param length 盐值长度
     * @return 随机盐值字符串
     */
    public static String generateSalt(int length) {
        if (length <= 0) {
            length = 8; // 默认长度
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder salt = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            salt.append(chars.charAt(index));
        }
        
        return salt.toString();
    }
    
    /**
     * 使用盐值进行MD5加密（盐值加在密码前面）
     * @param password 密码
     * @param salt 盐值
     * @return 加密后的字符串
     */
    public static String md5WithSalt(String password, String salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("密码和盐值不能为空");
        }
        
        String saltedPassword = salt + password;
        return md5(saltedPassword);
    }
    
    /**
     * 验证带盐值的MD5密码
     * @param input 用户输入的原始密码
     * @param salt 盐值
     * @param encrypted 数据库中存储的加密密码
     * @return 如果匹配返回true，否则返回false
     */
    public static boolean verifyMd5WithSalt(String input, String salt, String encrypted) {
        if (input == null || salt == null || encrypted == null) {
            return false;
        }
        
        String inputEncrypted = md5WithSalt(input, salt);
        return inputEncrypted.equals(encrypted);
    }
    
    /**
     * 简单的密码强度检查
     * @param password 密码
     * @return 密码强度等级：0-弱，1-中，2-强
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 0; // 弱
        }
        
        int strength = 0;
        
        // 检查长度
        if (password.length() >= 8) {
            strength++;
        }
        
        // 检查是否包含数字
        if (password.matches(".*\\d.*")) {
            strength++;
        }
        
        // 检查是否包含大写字母
        if (password.matches(".*[A-Z].*")) {
            strength++;
        }
        
        // 检查是否包含小写字母
        if (password.matches(".*[a-z].*")) {
            strength++;
        }
        
        // 检查是否包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            strength++;
        }
        
        // 根据强度值返回等级
        if (strength <= 2) {
            return 0; // 弱
        } else if (strength <= 4) {
            return 1; // 中
        } else {
            return 2; // 强
        }
    }
    
    /**
     * 获取密码强度描述
     * @param password 密码
     * @return 密码强度描述
     */
    public static String getPasswordStrengthDescription(String password) {
        int strength = checkPasswordStrength(password);
        
        switch (strength) {
            case 0:
                return "弱 - 建议使用至少8位字符，包含数字和字母";
            case 1:
                return "中 - 建议添加大写字母或特殊字符";
            case 2:
                return "强 - 密码安全性良好";
            default:
                return "未知";
        }
    }
    
    /**
     * 简单的SQL注入检查
     * @param input 用户输入
     * @return 如果包含SQL注入关键词返回true，否则返回false
     */
    public static boolean containsSqlInjection(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        String lowerInput = input.toLowerCase();
        
        // 检查常见的SQL注入关键词
        String[] sqlKeywords = {
            "select", "insert", "update", "delete", "drop", "truncate",
            "union", "join", "where", "or", "and", "exec", "execute",
            "script", "<script>", "--", "/*", "*/", "'", "\"", ";"
        };
        
        for (String keyword : sqlKeywords) {
            if (lowerInput.contains(keyword)) {
                // 进一步检查是否是正常的SQL语句的一部分
                if (!isValidSqlContext(lowerInput, keyword)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 检查SQL关键词是否在合法的上下文中
     * @param input 用户输入
     * @param keyword SQL关键词
     * @return 如果是合法上下文返回true，否则返回false
     */
    private static boolean isValidSqlContext(String input, String keyword) {
        // 这里可以实现更复杂的逻辑来检查关键词是否在合法的SQL上下文中
        // 目前只做简单的检查
        return false;
    }
    
    /**
     * 过滤SQL注入字符
     * @param input 用户输入
     * @return 过滤后的安全字符串
     */
    public static String filterSqlInjection(String input) {
        if (input == null) {
            return null;
        }
        
        // 替换常见的SQL注入字符
        return input.replace("'", "''")
                   .replace(";", "")
                   .replace("--", "")
                   .replace("/*", "")
                   .replace("*/", "");
    }
}
