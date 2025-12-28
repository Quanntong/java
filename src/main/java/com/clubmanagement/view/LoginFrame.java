package com.clubmanagement.view;

import com.clubmanagement.model.User;
import com.clubmanagement.service.IUserService;
import com.clubmanagement.service.impl.UserServiceImpl;
import com.clubmanagement.common.exception.BusinessException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录窗口
 * 提供用户登录和注册功能
 */
public class LoginFrame extends JFrame {
    private IUserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        this.userService = new UserServiceImpl();
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        setTitle("社团活动管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // 居中显示
        setResizable(false);
        
        // 设置图标
        // ImageIcon icon = new ImageIcon("icon.png");
        // setIconImage(icon.getImage());
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        // 使用BorderLayout作为主布局
        setLayout(new BorderLayout(10, 10));
        
        // 顶部标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("社团活动管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // 中间表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 用户名标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);
        
        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // 底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginButton = new JButton("登录");
        loginButton.setPreferredSize(new Dimension(100, 30));
        JButton registerButton = new JButton("注册");
        registerButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 设置事件监听器
     */
    private void setupListeners() {
        // 登录按钮事件
        JButton loginButton = (JButton) ((JPanel) getContentPane().getComponent(2)).getComponent(0);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // 注册按钮事件
        JButton registerButton = (JButton) ((JPanel) getContentPane().getComponent(2)).getComponent(1);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
        
        // 回车键登录
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    /**
     * 登录处理
     */
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // 输入验证
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "用户名和密码不能为空", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // 调用Service层进行登录验证
            User user = userService.login(username, password);
            
            if (user != null) {
                // 登录成功，打开主界面
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new MainFrame(user).setVisible(true);
                        dispose(); // 关闭登录窗口
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, 
                    "用户名或密码错误", 
                    "登录失败", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText(""); // 清空密码框
                passwordField.requestFocus(); // 焦点回到密码框
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "登录过程中出现错误: " + ex.getMessage(), 
                "系统错误", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * 显示注册对话框
     */
    private void showRegisterDialog() {
        // 创建注册对话框
        JDialog registerDialog = new JDialog(this, "用户注册", true);
        registerDialog.setSize(350, 400);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new BorderLayout(10, 10));
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 用户名
        JTextField regUsernameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("用户名*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(regUsernameField, gbc);
        
        // 密码
        JPasswordField regPasswordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("密码*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(regPasswordField, gbc);
        
        // 确认密码
        JPasswordField regConfirmPasswordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("确认密码*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(regConfirmPasswordField, gbc);
        
        // 真实姓名
        JTextField regRealNameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("真实姓名*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(regRealNameField, gbc);
        
        // 班级
        JTextField regClassNameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("班级:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(regClassNameField, gbc);
        
        // 角色选择（学生/管理员）
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"STUDENT", "ADMIN"});
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("角色:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(roleComboBox, gbc);
        
        registerDialog.add(formPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitButton = new JButton("提交");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        registerDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = regUsernameField.getText().trim();
                String password = new String(regPasswordField.getPassword()).trim();
                String confirmPassword = new String(regConfirmPasswordField.getPassword()).trim();
                String realName = regRealNameField.getText().trim();
                String className = regClassNameField.getText().trim();
                String role = (String) roleComboBox.getSelectedItem();
                
                // 输入验证
                if (username.isEmpty() || password.isEmpty() || realName.isEmpty()) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "带*的字段不能为空", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "两次输入的密码不一致", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                    regPasswordField.setText("");
                    regConfirmPasswordField.setText("");
                    return;
                }
                
                try {
                    // 创建用户对象
                    User newUser = new User(username, password, role, realName, className);
                    
                    // 调用Service层进行注册
                    boolean success = userService.register(newUser);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(registerDialog, 
                            "注册成功！请使用新账号登录。", 
                            "注册成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                        registerDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(registerDialog, 
                            "注册失败，请稍后重试", 
                            "注册失败", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        ex.getMessage(), 
                        "注册错误", 
                        JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "注册过程中出现错误: " + ex.getMessage(), 
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
                registerDialog.dispose();
            }
        });
        
        registerDialog.setVisible(true);
    }
    
    /**
     * 主方法，启动登录窗口
     */
    public static void main(String[] args) {
        // 使用SwingUtilities确保线程安全
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置系统外观
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
