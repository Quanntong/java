package com.clubmanagement.view;

import com.clubmanagement.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主界面框架
 * 登录成功后显示的主窗口
 */
public class MainFrame extends JFrame {
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    
    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        setTitle("社团活动管理系统 - 主界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // 居中显示
        
        // 设置图标
        // ImageIcon icon = new ImageIcon("icon.png");
        // setIconImage(icon.getImage());
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        // 使用BorderLayout作为主布局
        setLayout(new BorderLayout());
        
        // 顶部欢迎面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.setBackground(new Color(240, 240, 240));
        
        welcomeLabel = new JLabel("欢迎你，" + currentUser.getRealName() + 
                                 " (" + (currentUser.getRole().equals("ADMIN") ? "管理员" : "学生") + ")");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // 退出按钮
        JButton logoutButton = new JButton("退出登录");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 左侧导航面板
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        leftPanel.setPreferredSize(new Dimension(150, 0));
        leftPanel.setBackground(new Color(245, 245, 245));
        
        // 导航按钮
        String[] navButtons = {"社团列表", "活动大厅", "个人中心"};
        for (String buttonText : navButtons) {
            JButton navButton = new JButton(buttonText);
            navButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(140, 40));
            navButton.setMargin(new Insets(10, 5, 10, 5));
            navButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchPanel(buttonText);
                }
            });
            leftPanel.add(navButton);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // 添加弹性空间
        leftPanel.add(Box.createVerticalGlue());
        
        add(leftPanel, BorderLayout.WEST);
        
        // 中间主面板（使用CardLayout）
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // 创建各个功能面板
        ClubListPanel clubListPanel = new ClubListPanel(currentUser);
        ActivityPanel activityPanel = new ActivityPanel(currentUser);
        UserCenterPanel userCenterPanel = new UserCenterPanel(currentUser);
        
        mainPanel.add(clubListPanel, "社团列表");
        mainPanel.add(activityPanel, "活动大厅");
        mainPanel.add(userCenterPanel, "个人中心");
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 默认显示社团列表
        cardLayout.show(mainPanel, "社团列表");
    }
    
    /**
     * 设置事件监听器
     */
    private void setupListeners() {
        // 窗口关闭事件
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
            }
        });
    }
    
    /**
     * 切换面板
     * @param panelName 面板名称
     */
    private void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    /**
     * 退出登录
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "确定要退出登录吗？",
            "确认退出",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 返回登录界面
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginFrame().setVisible(true);
                }
            });
            dispose(); // 关闭主窗口
        }
    }
    
    /**
     * 获取当前用户
     * @return 当前登录用户
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * 更新欢迎信息
     */
    public void updateWelcomeMessage() {
        welcomeLabel.setText("欢迎你，" + currentUser.getRealName() + 
                           " (" + (currentUser.getRole().equals("ADMIN") ? "管理员" : "学生") + ")");
    }
}
