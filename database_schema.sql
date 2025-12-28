-- 社团活动管理系统 - 数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS club_management 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE club_management;

-- 1. 用户表 (users)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role ENUM('ADMIN', 'STUDENT') NOT NULL DEFAULT 'STUDENT' COMMENT '角色: ADMIN-管理员, STUDENT-学生',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    class_name VARCHAR(100) COMMENT '班级',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 社团表 (clubs)
CREATE TABLE clubs (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '社团ID',
    name VARCHAR(100) NOT NULL COMMENT '社团名称',
    description TEXT COMMENT '社团描述',
    president_id INT COMMENT '社长ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (president_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_name (name),
    INDEX idx_president (president_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团表';

-- 3. 活动表 (activities)
CREATE TABLE activities (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    club_id INT NOT NULL COMMENT '所属社团ID',
    title VARCHAR(200) NOT NULL COMMENT '活动名称',
    description TEXT COMMENT '活动描述',
    activity_time DATETIME NOT NULL COMMENT '活动时间',
    location VARCHAR(200) NOT NULL COMMENT '活动地点',
    max_participants INT DEFAULT 0 COMMENT '最大参与人数(0表示无限制)',
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'PUBLISHED', 'COMPLETED') 
        DEFAULT 'PENDING' COMMENT '状态: PENDING-审核中, APPROVED-已批准, REJECTED-已拒绝, PUBLISHED-已发布, COMPLETED-已完成',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    INDEX idx_club (club_id),
    INDEX idx_status (status),
    INDEX idx_time (activity_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 4. 报名记录表 (signups)
CREATE TABLE signups (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '报名记录ID',
    user_id INT NOT NULL COMMENT '用户ID',
    activity_id INT NOT NULL COMMENT '活动ID',
    signup_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') 
        DEFAULT 'PENDING' COMMENT '状态: PENDING-待确认, CONFIRMED-已确认, CANCELLED-已取消',
    notes TEXT COMMENT '备注',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    UNIQUE KEY unique_signup (user_id, activity_id),
    INDEX idx_user (user_id),
    INDEX idx_activity (activity_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报名记录表';

-- 5. 社团成员表 (club_members) - 额外添加，用于记录学生加入社团的关系
CREATE TABLE club_members (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '成员关系ID',
    user_id INT NOT NULL COMMENT '用户ID',
    club_id INT NOT NULL COMMENT '社团ID',
    role ENUM('MEMBER', 'OFFICER', 'PRESIDENT') DEFAULT 'MEMBER' COMMENT '角色: MEMBER-普通成员, OFFICER-干部, PRESIDENT-社长',
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '状态: PENDING-申请中, APPROVED-已批准, REJECTED-已拒绝',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    UNIQUE KEY unique_membership (user_id, club_id),
    INDEX idx_club (club_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员表';

-- ============================================
-- 插入测试数据
-- ============================================

-- 1. 插入管理员账号
INSERT INTO users (username, password, role, real_name, class_name, email, phone) VALUES
('admin', 'admin123', 'ADMIN', '系统管理员', '计算机学院', 'admin@example.com', '13800138000');

-- 2. 插入学生账号
INSERT INTO users (username, password, role, real_name, class_name, email, phone) VALUES
('student1', 'student123', 'STUDENT', '张三', '计算机科学与技术2023级1班', 'zhangsan@example.com', '13800138001'),
('student2', 'student123', 'STUDENT', '李四', '软件工程2023级2班', 'lisi@example.com', '13800138002');

-- 3. 插入社团数据
INSERT INTO clubs (name, description, president_id) VALUES
('计算机协会', '专注于计算机技术和编程的学术社团', 1),
('篮球社', '热爱篮球运动的同学组成的体育社团', 1);

-- 4. 插入活动数据
INSERT INTO activities (club_id, title, description, activity_time, location, max_participants, status) VALUES
(1, 'Java编程入门讲座', '面向初学者的Java编程基础讲座', '2024-06-15 14:00:00', '教学楼A101', 50, 'PUBLISHED'),
(1, 'Python数据分析实战', '使用Python进行数据分析的实践课程', '2024-06-20 15:30:00', '实验楼B201', 30, 'PUBLISHED'),
(2, '校园篮球友谊赛', '校内篮球爱好者友谊比赛', '2024-06-18 16:00:00', '体育馆篮球场', 20, 'PUBLISHED');

-- 5. 插入报名记录
INSERT INTO signups (user_id, activity_id, status) VALUES
(2, 1, 'CONFIRMED'),  -- 学生1报名Java讲座
(2, 3, 'PENDING'),    -- 学生1报名篮球赛
(3, 2, 'CONFIRMED'),  -- 学生2报名Python课程
(3, 3, 'CONFIRMED');  -- 学生2报名篮球赛

-- 6. 插入社团成员数据
INSERT INTO club_members (user_id, club_id, role, status) VALUES
(1, 1, 'PRESIDENT', 'APPROVED'),  -- 管理员是计算机协会社长
(1, 2, 'PRESIDENT', 'APPROVED'),  -- 管理员是篮球社社长
(2, 1, 'MEMBER', 'APPROVED'),     -- 学生1加入计算机协会
(3, 2, 'MEMBER', 'APPROVED');     -- 学生2加入篮球社

-- ============================================
-- 查询测试数据
-- ============================================
SELECT '=== 用户表数据 ===' AS '';
SELECT * FROM users;

SELECT '=== 社团表数据 ===' AS '';
SELECT c.*, u.real_name AS president_name FROM clubs c 
LEFT JOIN users u ON c.president_id = u.id;

SELECT '=== 活动表数据 ===' AS '';
SELECT a.*, c.name AS club_name FROM activities a 
JOIN clubs c ON a.club_id = c.id;

SELECT '=== 报名记录 ===' AS '';
SELECT s.*, u.real_name, a.title AS activity_title 
FROM signups s 
JOIN users u ON s.user_id = u.id 
JOIN activities a ON s.activity_id = a.id;

SELECT '=== 社团成员 ===' AS '';
SELECT cm.*, u.real_name, c.name AS club_name 
FROM club_members cm 
JOIN users u ON cm.user_id = u.id 
JOIN clubs c ON cm.club_id = c.id;
