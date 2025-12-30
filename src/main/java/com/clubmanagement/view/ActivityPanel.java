package com.clubmanagement.view;

import com.clubmanagement.model.User;
import com.clubmanagement.model.Activity;
import com.clubmanagement.service.IActivityService;
import com.clubmanagement.service.impl.ActivityServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 活动大厅面板
 * 显示活动列表，学生可以报名参加活动
 */
public class ActivityPanel extends JPanel {
    private User currentUser;
    private IActivityService activityService;
    private JTable activityTable;
    private DefaultTableModel tableModel;
    
    public ActivityPanel(User user) {
        this.currentUser = user;
        this.activityService = new ActivityServiceImpl();
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
        
        // 如果是管理员，显示管理功能按钮
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
            
            toolbarPanel.add(Box.createHorizontalStrut(10));
            JButton viewButton = new JButton("查看详情");
            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewActivityDetails();
                }
            });
            toolbarPanel.add(viewButton);
            
            toolbarPanel.add(Box.createHorizontalStrut(10));
            JButton deleteButton = new JButton("删除活动");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteSelectedActivity();
                }
            });
            toolbarPanel.add(deleteButton);
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
            List<Activity> activities = activityService.getAllActivities();
            
            // 填充表格
            for (Activity activity : activities) {
                Object[] rowData = {
                    activity.getId(),
                    activity.getTitle(),
                    activity.getClubName() != null ? activity.getClubName() : "社团" + activity.getClubId(),
                    activity.getStartTime(),
                    activity.getLocation(),
                    activity.getStatus(),
                    activity.getCreatedAt()
                };
                tableModel.addRow(rowData);
            }
            
            // 更新状态
            JOptionPane.showMessageDialog(this, 
                "加载完成，共 " + activities.size() + " 个活动", 
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
            try {
                // 调用Service层处理报名逻辑
                boolean success = activityService.registerActivity(activityId, currentUser.getId());
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "报名活动 \"" + activityName + "\" 成功！", 
                        "报名成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadActivityData(); // 刷新数据
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "报名失败，请稍后重试", 
                        "报名失败", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "报名失败: " + e.getMessage(), 
                    "报名失败", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 显示创建活动对话框（管理员功能）
     */
    private void showCreateActivityDialog() {
        JDialog createDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(this), "创建活动", Dialog.ModalityType.APPLICATION_MODAL);
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
                    int maxParticipants = maxParticipantsStr.isEmpty() ? 50 : Integer.parseInt(maxParticipantsStr);
                    
                    // 创建Activity对象
                    Activity newActivity = new Activity();
                    newActivity.setTitle(title);
                    newActivity.setDescription(description);
                    newActivity.setClubId(clubId);
                    // 解析时间字符串，这里简化处理，实际应该使用日期选择器
                    java.util.Date startTime = new java.util.Date(System.currentTimeMillis() + 86400000); // 明天
                    java.util.Date endTime = new java.util.Date(System.currentTimeMillis() + 90000000); // 明天+1小时
                    newActivity.setStartTime(startTime);
                    newActivity.setEndTime(endTime);
                    newActivity.setLocation(location);
                    newActivity.setMaxParticipants(maxParticipants);
                    newActivity.setCurrentParticipants(0);
                    newActivity.setStatus("已发布");
                    newActivity.setCreatedAt(new java.util.Date());
                    newActivity.setUpdatedAt(new java.util.Date());
                    
                    // 调用Service层创建活动
                    boolean success = activityService.createActivity(newActivity, currentUser.getId());
                    
                    if (success) {
                        JOptionPane.showMessageDialog(createDialog, 
                            "活动创建成功！", 
                            "成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                        createDialog.dispose();
                        loadActivityData(); // 刷新数据
                    } else {
                        JOptionPane.showMessageDialog(createDialog, 
                            "活动创建失败", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                    
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
     * 查看选中的活动详情（管理员功能）
     */
    private void viewActivityDetails() {
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
        String clubName = (String) tableModel.getValueAt(selectedRow, 2);
        String time = tableModel.getValueAt(selectedRow, 3).toString();
        String location = (String) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        String createdAt = tableModel.getValueAt(selectedRow, 6).toString();
        
        // 显示活动详情对话框
        JDialog detailsDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(this), "活动详情", Dialog.ModalityType.APPLICATION_MODAL);
        detailsDialog.setSize(400, 350);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setLayout(new BorderLayout(10, 10));
        
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(new JLabel("活动ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        detailsPanel.add(new JLabel(String.valueOf(activityId)), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        detailsPanel.add(new JLabel("活动名称:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        detailsPanel.add(new JLabel(activityName), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        detailsPanel.add(new JLabel("所属社团:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        detailsPanel.add(new JLabel(clubName), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        detailsPanel.add(new JLabel("活动时间:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        detailsPanel.add(new JLabel(time), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        detailsPanel.add(new JLabel("活动地点:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        detailsPanel.add(new JLabel(location), gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        detailsPanel.add(new JLabel("活动状态:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        detailsPanel.add(new JLabel(status), gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        detailsPanel.add(new JLabel("创建时间:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        detailsPanel.add(new JLabel(createdAt), gbc);
        
        detailsDialog.add(detailsPanel, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsDialog.dispose();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        detailsDialog.setVisible(true);
    }
    
    /**
     * 删除选中的活动（管理员功能）
     */
    private void deleteSelectedActivity() {
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
            "确定要删除活动 \"" + activityName + "\" 吗？\n此操作不可恢复！",
            "确认删除",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = activityService.deleteActivity(activityId, currentUser.getId());
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "活动 \"" + activityName + "\" 删除成功！", 
                        "删除成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadActivityData(); // 刷新列表
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "活动删除失败", 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "删除过程中出现错误: " + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
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
