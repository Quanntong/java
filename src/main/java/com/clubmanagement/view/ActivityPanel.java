package com.clubmanagement.view;

import com.clubmanagement.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 活动大厅面板
 * 显示活动列表，学生可以报名参加活动
 */
public class ActivityPanel extends JPanel {
    private User currentUser;
    private JTable activityTable;
    private DefaultTableModel tableModel;
    
    public ActivityPanel(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        loadActivityData();
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
        // 顶部工具栏
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel titleLabel = new JLabel("活动大厅");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        toolbarPanel.add(titleLabel);
        
        toolbarPanel.add(Box.createHorizontalStrut(20));
        
        // 刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadActivityData();
            }
        });
        toolbarPanel.add(refreshButton);
        
        // 如果是学生，显示报名按钮
        if ("STUDENT".equals(currentUser.getRole())) {
            toolbarPanel.add(Box.createHorizontalStrut(20));
            JButton signupButton = new JButton("报名参加");
            signupButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    signupForActivity();
                }
            });
            toolbarPanel.add(signupButton);
        }
        
        // 如果是管理员，显示创建活动按钮
        if ("ADMIN".equals(currentUser.getRole())) {
            toolbarPanel.add(Box.createHorizontalStrut(20));
            JButton createButton = new JButton("创建活动");
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showCreateActivityDialog();
                }
            });
            toolbarPanel.add(createButton);
        }
        
        add(toolbarPanel, BorderLayout.NORTH);
        
        // 中间表格
        String[] columnNames = {"ID", "活动名称", "所属社团", "时间", "地点", "状态", "创建时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        activityTable = new JTable(tableModel);
        activityTable.setRowHeight(30);
        activityTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        activityTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        
        // 设置列宽
        activityTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        activityTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // 活动名称
        activityTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // 所属社团
        activityTable.getColumnModel().getColumn(3).setPreferredWidth(150);  // 时间
        activityTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // 地点
        activityTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // 状态
        activityTable.getColumnModel().getColumn(6).setPreferredWidth(150);  // 创建时间
        
        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);
        
        // 底部状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("双击行查看详情，学生可以报名参加活动");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 加载活动数据
     */
    private void loadActivityData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        try {
            // 获取所有活动
            // 注意：这里需要ActivityService和Activity实体类
            // 由于Activity相关类还未完全实现，这里使用模拟数据
            
            // TODO: 实际项目中需要实现ActivityService
            // List<Activity> activities = activityService.getAllActivities();
            
            // 模拟数据
            Object[][] mockData = {
                {1, "Java编程入门讲座", "计算机协会", "2024-06-15 14:00", "教学楼A101", "已发布", "2024-06-10 09:00"},
                {2, "Python数据分析实战", "计算机协会", "2024-06-20 15:30", "实验楼B201", "已发布", "2024-06-12 10:30"},
                {3, "校园篮球友谊赛", "篮球社", "2024-06-18 16:00", "体育馆篮球场", "已发布", "2024-06-11 14:20"}
            };
            
            for (Object[] rowData : mockData) {
                tableModel.addRow(rowData);
            }
            
            // 更新状态
            JOptionPane.showMessageDialog(this, 
                "加载完成，共 " + mockData.length + " 个活动", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "加载活动数据失败: " + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 报名参加选中的活动（学生功能）
     */
    private void signupForActivity() {
        int selectedRow = activityTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择一个活动", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int activityId = (int) tableModel.getValueAt(selectedRow, 0);
        String activityName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "确定要报名参加活动 \"" + activityName + "\" 吗？",
            "确认报名",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 这里应该调用Service层处理报名逻辑
            // 由于数据库设计中包含了signups表，但相关Service/DAO还未实现
            // 这里先显示提示信息
            
            // 模拟报名成功
            boolean success = true; // 模拟成功
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "报名活动 \"" + activityName + "\" 成功！", 
                    "报名成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // TODO: 实际项目中需要实现报名逻辑
                // activityService.signupForActivity(currentUser.getId(), activityId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "报名失败，请稍后重试", 
                    "报名失败", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 显示创建活动对话框（管理员功能）
     */
    private void showCreateActivityDialog() {
        JDialog createDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "创建活动", true);
        createDialog.setSize(500, 400);
        createDialog.setLocationRelativeTo(this);
        createDialog.setLayout(new BorderLayout(10, 10));
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 活动名称
        JTextField titleField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("活动名称*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(titleField, gbc);
        
        // 活动描述
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("活动描述:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(descriptionScroll, gbc);
        
        // 活动时间
        JTextField timeField = new JTextField("2024-06-25 14:00", 20);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("活动时间*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(timeField, gbc);
        
        // 活动地点
        JTextField locationField = new JTextField("教学楼A101", 20);
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("活动地点*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(locationField, gbc);
        
        // 所属社团（这里应该从数据库获取社团列表，暂时用文本框）
        JTextField clubField = new JTextField("1", 10);
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("社团ID*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(clubField, gbc);
        
        // 最大参与人数
        JTextField maxParticipantsField = new JTextField("50", 10);
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("最大人数:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(maxParticipantsField, gbc);
        
        createDialog.add(formPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitButton = new JButton("创建");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        createDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();
                String time = timeField.getText().trim();
                String location = locationField.getText().trim();
                String clubIdStr = clubField.getText().trim();
                String maxParticipantsStr = maxParticipantsField.getText().trim();
                
                if (title.isEmpty() || time.isEmpty() || location.isEmpty() || clubIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(createDialog, 
                        "带*的字段不能为空", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    int clubId = Integer.parseInt(clubIdStr);
                    int maxParticipants = maxParticipantsStr.isEmpty() ? 0 : Integer.parseInt(maxParticipantsStr);
                    
                    // 这里应该创建Activity对象并调用Service层
                    // 由于Activity相关类还未完全实现，这里先显示提示
                    
                    JOptionPane.showMessageDialog(createDialog, 
                        "活动创建功能需要Activity实体类和Service层的完整实现", 
                        "功能待实现", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // TODO: 实际项目中需要实现活动创建逻辑
                    // Activity newActivity = new Activity(clubId, title, description, time, location, maxParticipants, "PENDING");
                    // boolean success = activityService.createActivity(newActivity);
                    
                    createDialog.dispose();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(createDialog, 
                        "社团ID和最大人数必须是数字", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(createDialog, 
                        "创建过程中出现错误: " + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDialog.dispose();
            }
        });
        
        createDialog.setVisible(true);
    }
    
    /**
     * 获取选中的活动ID
     * @return 选中的活动ID，如果没有选中返回-1
     */
    public int getSelectedActivityId() {
        int selectedRow = activityTable.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        return (int) tableModel.getValueAt(selectedRow, 0);
    }
}

/**
 * 活动服务类（临时占位）
 * 实际项目中需要完整实现
 */
class ActivityService {
    // 这里只是占位，实际需要完整实现
}
