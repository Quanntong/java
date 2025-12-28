package com.clubmanagement;

import com.clubmanagement.view.LoginFrame;

import javax.swing.*;

/**
 * 社团活动管理系统 - 主应用程序
 * 启动Swing图形界面
 */
public class App {
    /**
     * 主方法，启动应用程序
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        System.out.println("=== 社团活动管理系统启动 ===");
        System.out.println("作者: Java课程大作业");
        System.out.println("版本: 1.0.0");
        System.out.println("==========================");
        
        // 使用SwingUtilities确保线程安全
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置系统外观
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
                    // 启动登录窗口
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    
                    System.out.println("登录窗口已启动");
                } catch (Exception e) {
                    System.err.println("启动应用程序时出现错误:");
                    e.printStackTrace();
                    
                    // 显示错误对话框
                    JOptionPane.showMessageDialog(null,
                        "启动应用程序时出现错误:\n" + e.getMessage(),
                        "启动错误",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
