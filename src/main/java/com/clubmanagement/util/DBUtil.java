package com.clubmanagement.util;

import com.clubmanagement.common.Constants;
import com.clubmanagement.common.exception.SystemException;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 数据库工具类（增强版）
 * 负责数据库连接和资源管理，支持简单的连接池
 */
public class DBUtil {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    
    // 连接池配置
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 20;
    private static final long CONNECTION_TIMEOUT = 30000; // 30秒
    
    // 简单的连接池实现
    private static ConcurrentLinkedQueue<Connection> connectionPool;
    private static int activeConnections = 0;
    
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
                        // 初始化连接池
                        initializeConnectionPool();
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
     * 初始化连接池
     */
    private static void initializeConnectionPool() {
        connectionPool = new ConcurrentLinkedQueue<>();
        
        try {
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                Connection conn = createNewConnection();
                if (conn != null) {
                    connectionPool.offer(conn);
                }
            }
        } catch (SQLException e) {
            throw new SystemException("连接池初始化失败", e);
        }
    }
    
    /**
     * 创建新的数据库连接
     * @return 新的数据库连接
     * @throws SQLException 如果创建连接失败
     */
    private static Connection createNewConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        activeConnections++;
        return conn;
    }
    
    /**
     * 获取数据库连接（从连接池获取）
     * @return Connection对象
     */
    public static Connection getConnection() {
        if (demoMode) {
            // 演示模式：返回null，让DAO层处理
            System.out.println("演示模式: 返回null连接");
            return null;
        }
        
        try {
            // 首先尝试从连接池获取
            Connection conn = connectionPool.poll();
            
            if (conn != null) {
                // 检查连接是否有效
                if (isConnectionValid(conn)) {
                    return conn;
                } else {
                    // 连接无效，关闭并创建新的
                    closeQuietly(conn);
                    activeConnections--;
                }
            }
            
            // 如果连接池为空但未达到最大连接数，创建新连接
            if (activeConnections < MAX_POOL_SIZE) {
                return createNewConnection();
            }
            
            // 等待连接释放（简单实现）
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < CONNECTION_TIMEOUT) {
                conn = connectionPool.poll();
                if (conn != null && isConnectionValid(conn)) {
                    return conn;
                }
                try {
                    Thread.sleep(100); // 等待100毫秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SystemException("获取数据库连接时被中断", e);
                }
            }
            
            throw new SystemException("获取数据库连接超时，请稍后重试");
            
        } catch (SQLException e) {
            throw new SystemException("获取数据库连接失败", e);
        }
    }
    
    /**
     * 检查连接是否有效
     * @param conn 数据库连接
     * @return 如果连接有效返回true，否则返回false
     */
    private static boolean isConnectionValid(Connection conn) {
        if (conn == null) {
            return false;
        }
        
        try {
            return conn.isValid(2); // 2秒超时
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * 安静地关闭连接（不抛出异常）
     * @param conn 数据库连接
     */
    private static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // 忽略关闭异常
            }
        }
    }
    
    /**
     * 释放连接（将连接返回到连接池）
     * @param conn 数据库连接
     */
    public static void releaseConnection(Connection conn) {
        if (conn != null && isConnectionValid(conn)) {
            // 将连接返回到连接池
            connectionPool.offer(conn);
        } else {
            // 连接无效，关闭它
            closeQuietly(conn);
            activeConnections--;
        }
    }
    
    /**
     * 关闭连接（不返回到连接池）
     * @param conn 数据库连接
     */
    public static void close(Connection conn) {
        closeQuietly(conn);
        activeConnections--;
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
     * 关闭所有资源（使用连接池）
     * @param conn 数据库连接
     * @param stmt Statement对象
     * @param rs ResultSet对象
     */
    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        releaseConnection(conn);
    }
    
    /**
     * 关闭所有资源（使用PreparedStatement和连接池）
     * @param conn 数据库连接
     * @param pstmt PreparedStatement对象
     * @param rs ResultSet对象
     */
    public static void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        close(rs);
        close(pstmt);
        releaseConnection(conn);
    }
    
    /**
     * 关闭所有资源（不返回到连接池）
     * @param conn 数据库连接
     * @param stmt Statement对象
     * @param rs ResultSet对象
     */
    public static void closeAllDirect(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }
    
    /**
     * 关闭所有资源（使用PreparedStatement，不返回到连接池）
     * @param conn 数据库连接
     * @param pstmt PreparedStatement对象
     * @param rs ResultSet对象
     */
    public static void closeAllDirect(Connection conn, PreparedStatement pstmt, ResultSet rs) {
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
     * 获取连接池统计信息
     * @return 连接池统计信息字符串
     */
    public static String getPoolStatistics() {
        return String.format("连接池统计: 活动连接数=%d, 空闲连接数=%d, 最大连接数=%d",
                activeConnections, connectionPool.size(), MAX_POOL_SIZE);
    }
    
    /**
     * 关闭所有连接池连接
     */
    public static void shutdown() {
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            closeQuietly(conn);
            activeConnections--;
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
            releaseConnection(conn);
        }
    }
    
    /**
     * 执行SQL查询（用于测试）
     * @param sql SQL语句
     * @return 查询结果的第一行第一列
     */
    public static Object executeScalar(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getObject(1);
            }
            
            return null;
        } catch (SQLException e) {
            throw new SystemException("执行SQL查询失败: " + sql, e);
        } finally {
            closeAll(conn, stmt, rs);
        }
    }
}
