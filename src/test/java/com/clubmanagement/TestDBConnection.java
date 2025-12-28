package com.clubmanagement;

import com.clubmanagement.util.DBUtil;
import java.sql.Connection;

/**
 * 测试数据库连接
 */
public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("=== 测试数据库连接 ===");
        
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ 数据库连接成功！");
                System.out.println("数据库URL: " + conn.getMetaData().getURL());
                System.out.println("数据库产品: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("数据库版本: " + conn.getMetaData().getDatabaseProductVersion());
            } else {
                System.out.println("✗ 数据库连接失败！");
            }
        } catch (Exception e) {
            System.err.println("✗ 数据库连接异常:");
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, null, null);
        }
        
        System.out.println("=== 测试完成 ===");
    }
}
