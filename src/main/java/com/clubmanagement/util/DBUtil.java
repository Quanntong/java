package com.clubmanagement.util;

import com.clubmanagement.common.Constants;
import com.clubmanagement.common.exception.SystemException;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 数据库工具类
 * 负责数据库连接和资源管理
 */
public class DBUtil {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    
    // 演示模式标志
    private static boolean demoMode = false;
    
    // 使用静态代码块加载配置文件
    static {
        try {
            // 加载配置文件
            Properties props = new Properties();
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream(Constants.DB_CONFIG_FILE);
            if (in == null) {
                System.out.println("警告: 数据库配置文件未找到，启用演示模式");
                demoMode = true;
            } else {
                props.load(in);
                
                // 获取配置信息
                driver = props.getProperty(Constants.DB_DRIVER_KEY);
                url = props.getProperty(Constants.DB_URL_KEY);
                username = props.getProperty(Constants.DB_USERNAME_KEY);
                password = props.getProperty(Constants.DB_PASSWORD_KEY);
                
                // 验证配置
                if (driver == null || url == null || username == null || password == null) {
                    System.out.println("警告: 数据库配置不完整，启用演示模式");
                    demoMode = true;
                } else {
                    // 尝试加载数据库驱动
                    try {
                        Class.forName(driver);
                        System.out.println("数据库驱动加载成功: " + driver);
                    } catch (ClassNotFoundException e) {
                        System.out.println("警告: 数据库驱动加载失败，启用演示模式");
                        demoMode = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("警告: 数据库配置加载失败，启用演示模式: " + e.getMessage());
            demoMode = true;
        }
    }
    
    /**
     * 获取数据库连接
     * @return Connection对象
     */
    public static Connection getConnection() {
        if (demoMode) {
            // 演示模式：返回null，让DAO层处理
            System.out.println("演示模式: 返回null连接");
            return null;
        }
        
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SystemException("获取数据库连接失败", e);
        }
    }
    
    /**
     * 关闭连接
     * @param conn 数据库连接
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // 记录日志但不抛出异常
                System.err.println("关闭Connection时发生错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭Statement
     * @param stmt Statement对象
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // 记录日志但不抛出异常
                System.err.println("关闭Statement时发生错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭PreparedStatement
     * @param pstmt PreparedStatement对象
     */
    public static void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // 记录日志但不抛出异常
                System.err.println("关闭PreparedStatement时发生错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭ResultSet
     * @param rs ResultSet对象
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // 记录日志但不抛出异常
                System.err.println("关闭ResultSet时发生错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭所有资源
     * @param conn 数据库连接
     * @param stmt Statement对象
     * @param rs ResultSet对象
     */
    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }
    
    /**
     * 关闭所有资源（使用PreparedStatement）
     * @param conn 数据库连接
     * @param pstmt PreparedStatement对象
     * @param rs ResultSet对象
     */
    public static void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        close(rs);
        close(pstmt);
        close(conn);
    }
    
    /**
     * 开启事务
     * @param conn 数据库连接
     */
    public static void beginTransaction(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new SystemException("开启事务失败", e);
        }
    }
    
    /**
     * 提交事务
     * @param conn 数据库连接
     */
    public static void commitTransaction(Connection conn) {
        try {
            if (conn != null) {
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new SystemException("提交事务失败", e);
        }
    }
    
    /**
     * 回滚事务
     * @param conn 数据库连接
     */
    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new SystemException("回滚事务失败", e);
        }
    }
    
    /**
     * 测试数据库连接
     * @return 如果连接成功返回true，否则返回false
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        } finally {
            close(conn);
        }
    }
}
