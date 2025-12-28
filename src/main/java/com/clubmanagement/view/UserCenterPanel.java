package com.clubmanagement.view;

import com.clubmanagement.model.User;
import com.clubmanagement.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 个人中心面板
 * 显示用户信息，提供修改个人信息功能
 */
public class UserCenterPanel extends JPanel {
    private User currentUser;
    private UserService userService;
    
    private JLabel usernameLabel;
    private JLabel roleLabel;
    private JLabel realNameLabel;
    private JLabel classNameLabel;
    private JLabel createdAtLabel;
    
    public UserCenterPanel(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        initComponents();
        setupLayout();
        loadUserData();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        // 顶部标题
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("个人中心");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // 中间信息面板
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("个人信息"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 用户名
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        usernameLabel = new JLabel();
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(usernameLabel, gbc);
        
        // 角色
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("角色:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        roleLabel = new JLabel();
        roleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(roleLabel, gbc);
        
        // 真实姓名
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("真实姓名:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        realNameLabel = new JLabel();
        realNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(realNameLabel, gbc);
        
        // 班级
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("班级:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        classNameLabel = new JLabel();
        classNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(classNameLabel, gbc);
        
        // 注册时间
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("注册时间:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        createdAtLabel = new JLabel();
        createdAtLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(createdAtLabel, gbc);
        
        add(infoPanel, BorderLayout.CENTER);
        
        // 底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton editButton = new JButton("修改信息");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditDialog();
            }
        });
        
        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserData();
            }
        });
        
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 加载用户数据
     */
    private void loadUserData() {
        try {
            // 从数据库重新加载用户信息
            User updatedUser = userService.getUserById(currentUser.getId());
            if (updatedUser != null) {
                currentUser = updatedUser;
                
                // 更新显示
                usernameLabel.setText(currentUser.getUsername());
                roleLabel.setText("ADMIN".equals(currentUser.getRole()) ? "管理员" : "学生");
                realNameLabel.setText(currentUser.getRealName());
                classNameLabel.setText(currentUser.getClassName() != null ? currentUser.getClassName() : "未设置");
                createdAtLabel.setText(currentUser.getCreatedAt() != null ? 
                    currentUser.getCreatedAt().toString() : "未知");
                
                JOptionPane.showMessageDialog(this, 
                    "个人信息已刷新", 
                    "提示", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "加载用户信息失败: " + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 显示修改信息对话框
     */
    private void showEditDialog() {
        JDialog editDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "修改个人信息", true);
        editDialog.setSize(400, 350);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout(10, 10));
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 用户名（不可修改）
        JTextField usernameField = new JTextField(currentUser.getUsername(), 15);
        usernameField.setEditable(false);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);
        
        // 密码
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setText(currentUser.getPassword());
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("密码*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);
        
        // 确认密码
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setText(currentUser.getPassword());
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("确认密码*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(confirmPasswordField, gbc);
        
        // 真实姓名
        JTextField realNameField = new JTextField(currentUser.getRealName(), 15);
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("真实姓名*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(realNameField, gbc);
        
        // 班级
        JTextField classNameField = new JTextField(currentUser.getClassName() != null ? currentUser.getClassName() : "", 15);
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("班级:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(classNameField, gbc);
        
        // 角色（不可修改）
        JTextField roleField = new JTextField("ADMIN".equals(currentUser.getRole()) ? "管理员" : "学生", 15);
        roleField.setEditable(false);
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("角色:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(roleField, gbc);
        
        editDialog.add(formPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
                String realName = realNameField.getText().trim();
                String className = classNameField.getText().trim();
                
                // 输入验证
                if (password.isEmpty() || realName.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, 
                        "带*的字段不能为空", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(editDialog, 
                        "两次输入的密码不一致", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    return;
                }
                
                try {
                    // 更新用户信息
                    currentUser.setPassword(password);
                    currentUser.setRealName(realName);
                    currentUser.setClassName(className);
                    
                    boolean success = userService.updateUser(currentUser);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(editDialog, 
                            "个人信息修改成功！", 
                            "成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                        editDialog.dispose();
                        loadUserData(); // 刷新显示
                    } else {
                        JOptionPane.showMessageDialog(editDialog, 
                            "修改失败，请稍后重试", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(editDialog, 
                        ex.getMessage(), 
                        "修改错误", 
                        JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(editDialog, 
                        "修改过程中出现错误: " + ex.getMessage(), 
                        "系统错误", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
        
        editDialog.setVisible(true);
    }
    
    /**
     * 获取当前用户
     * @return 当前用户对象
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * 设置当前用户
     * @param user 用户对象
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserData();
    }
}
