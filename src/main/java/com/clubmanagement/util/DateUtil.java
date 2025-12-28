package com.clubmanagement.util;

import com.clubmanagement.common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 提供日期格式化和解析功能
 */
public class DateUtil {
    
    private static final SimpleDateFormat DEFAULT_FORMATTER = 
        new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
    
    /**
     * 私有构造方法，防止实例化
     */
    private DateUtil() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }
    
    /**
     * 将日期格式化为默认格式的字符串
     * @param date 日期对象
     * @return 格式化后的字符串，如果date为null则返回null
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        synchronized (DEFAULT_FORMATTER) {
            return DEFAULT_FORMATTER.format(date);
        }
    }
    
    /**
     * 将日期格式化为指定格式的字符串
     * @param date 日期对象
     * @param pattern 日期格式模式
     * @return 格式化后的字符串，如果date为null则返回null
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
    
    /**
     * 将默认格式的字符串解析为日期对象
     * @param dateStr 日期字符串
     * @return 解析后的日期对象，如果解析失败则返回null
     */
    public static Date parse(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            synchronized (DEFAULT_FORMATTER) {
                return DEFAULT_FORMATTER.parse(dateStr);
            }
        } catch (ParseException e) {
            // 记录日志或进行其他处理
            System.err.println("日期解析失败: " + dateStr + ", 格式应为: " + Constants.DEFAULT_DATE_FORMAT);
            return null;
        }
    }
    
    /**
     * 将指定格式的字符串解析为日期对象
     * @param dateStr 日期字符串
     * @param pattern 日期格式模式
     * @return 解析后的日期对象，如果解析失败则返回null
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            // 记录日志或进行其他处理
            System.err.println("日期解析失败: " + dateStr + ", 格式应为: " + pattern);
            return null;
        }
    }
    
    /**
     * 获取当前时间的默认格式字符串
     * @return 当前时间的格式化字符串
     */
    public static String getCurrentTimeString() {
        return format(new Date());
    }
    
    /**
     * 获取当前时间的指定格式字符串
     * @param pattern 日期格式模式
     * @return 当前时间的格式化字符串
     */
    public static String getCurrentTimeString(String pattern) {
        return format(new Date(), pattern);
    }
    
    /**
     * 获取当前日期对象
     * @return 当前日期对象
     */
    public static Date getCurrentDate() {
        return new Date();
    }
    
    /**
     * 检查日期字符串是否符合默认格式
     * @param dateStr 日期字符串
     * @return 如果符合格式返回true，否则返回false
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        try {
            synchronized (DEFAULT_FORMATTER) {
                DEFAULT_FORMATTER.parse(dateStr);
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * 检查日期字符串是否符合指定格式
     * @param dateStr 日期字符串
     * @param pattern 日期格式模式
     * @return 如果符合格式返回true，否则返回false
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            formatter.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * 比较两个日期是否相等（忽略时间部分）
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果日期部分相等返回true，否则返回false
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dayFormatter.format(date1).equals(dayFormatter.format(date2));
    }
    
    /**
     * 计算两个日期之间的天数差
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 天数差（date2 - date1）
     */
    public static long getDaysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        long diff = date2.getTime() - date1.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * 检查日期是否在指定范围内
     * @param date 要检查的日期
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 如果在范围内返回true，否则返回false
     */
    public static boolean isDateInRange(Date date, Date startDate, Date endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.before(startDate) && !date.after(endDate);
    }
}
