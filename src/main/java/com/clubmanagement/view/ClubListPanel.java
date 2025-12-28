package com.clubmanagement.view;

import com.clubmanagement.model.User;
import com.clubmanagement.model.Club;
import com.clubmanagement.service.ClubService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 社团列表面板
 * 显示所有社团信息，提供创建社团和申请加入功能
 */
public class ClubListPanel extends JPanel {
    private User currentUser;
    private ClubService clubService;
    private JTable clubTable;
    private DefaultTableModel tableModel;
    
    public ClubListPanel(User user) {
        this.currentUser = user;
        this.clubService = new ClubService();
        initComponents();
        setupLayout();
        loadClubData();
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
        
        JLabel titleLabel = new JLabel("社团列表");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        toolbarPanel.add(titleLabel);
        
        toolbarPanel.add(Box.createHorizontalStrut(20));
        
        // 刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadClubData();
            }
        });
        toolbarPanel.add(refreshButton);
        
        // 如果是管理员，显示创建社团按钮
        if ("ADMIN".equals(currentUser.getRole())) {
            toolbarPanel.add(Box.createHorizontalStrut(20));
            JButton createButton = new JButton("创建社团");
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showCreateClubDialog();
                }
            });
            toolbarPanel.add(createButton);
        }
        
        // 如果是学生，显示申请加入按钮
        if ("STUDENT".equals(currentUser.getRole())) {
            toolbarPanel.add(Box.createHorizontalStrut(20));
            JButton joinButton = new JButton("申请加入");
            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    joinSelectedClub();
                }
            });
            toolbarPanel.add(joinButton);
        }
        
        add(toolbarPanel, BorderLayout.NORTH);
        
        // 中间表格
        String[] columnNames = {"ID", "社团名称", "描述", "社长ID", "创建时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        clubTable = new JTable(tableModel);
        clubTable.setRowHeight(30);
        clubTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        clubTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        
        // 设置列宽
        clubTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        clubTable.getColumnModel().getColumn(1).setPreferredWidth(150); // 名称
        clubTable.getColumnModel().getColumn(2).setPreferredWidth(300); // 描述
        clubTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // 社长ID
        clubTable.getColumnModel().getColumn(4).setPreferredWidth(150); // 创建时间
        
        JScrollPane scrollPane = new JScrollPane(clubTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);
        
        // 底部状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("双击行查看详情");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 加载社团数据
     */
    private void loadClubData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        try {
            // 获取所有社团
            List<Club> clubs = clubService.getAllClubs();
            
            // 填充表格
            for (Club club : clubs) {
                Object[] rowData = {
                    club.getId(),
                    club.getName(),
                    club.getDescription(),
                    club.getPresidentId(),
                    club.getCreatedAt()
                };
                tableModel.addRow(rowData);
            }
            
            // 更新状态
            JOptionPane.showMessageDialog(this, 
                "加载完成，共 " + clubs.size() + " 个社团", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "加载社团数据失败: " + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * 显示创建社团对话框（管理员功能）
     */
    private void showCreateClubDialog() {
        JDialog createDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "创建社团", true);
        createDialog.setSize(400, 300);
        createDialog.setLocationRelativeTo(this);
        createDialog.setLayout(new BorderLayout(10, 10));
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 社团名称
        JTextField nameField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("社团名称*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(nameField, gbc);
        
        // 社团描述
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("社团描述:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(descriptionScroll, gbc);
        
        // 社长ID（默认为当前用户）
        JTextField presidentIdField = new JTextField(String.valueOf(currentUser.getId()), 10);
        presidentIdField.setEditable(false);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("社长ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(presidentIdField, gbc);
        
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
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                int presidentId = currentUser.getId();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(createDialog, 
                        "社团名称不能为空", 
                        "输入错误", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    Club newClub = new Club(name, description, presidentId);
                    boolean success = clubService.createClub(newClub);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(createDialog, 
                            "社团创建成功！", 
                            "成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                        createDialog.dispose();
                        loadClubData(); // 刷新列表
                    } else {
                        JOptionPane.showMessageDialog(createDialog, 
                            "社团创建失败", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    }
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
     * 申请加入选中的社团（学生功能）
     */
    private void joinSelectedClub() {
        int selectedRow = clubTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择一个社团", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int clubId = (int) tableModel.getValueAt(selectedRow, 0);
        String clubName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "确定要申请加入社团 \"" + clubName + "\" 吗？",
            "确认申请",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 这里应该调用Service层处理加入社团的逻辑
            // 由于数据库设计中包含了club_members表，但相关Service/DAO还未实现
            // 这里先显示提示信息
            
            JOptionPane.showMessageDialog(this, 
                "申请加入社团 \"" + clubName + "\" 已提交，等待社长审核。", 
                "申请成功", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // TODO: 实际项目中需要实现加入社团的业务逻辑
            // clubService.joinClub(currentUser.getId(), clubId);
        }
    }
    
    /**
     * 获取选中的社团ID
     * @return 选中的社团ID，如果没有选中返回-1
     */
    public int getSelectedClubId() {
        int selectedRow = clubTable.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        return (int) tableModel.getValueAt(selectedRow, 0);
    }
}
