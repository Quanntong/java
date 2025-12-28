-- 社团活动管理系统 - 简化版数据库设计
-- 只包含用户要求的四张表

-- 创建数据库
CREATE DATABASE IF NOT EXISTS club_management 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE club_management;

-- 1. 用户表 (users)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL DEFAULT 'STUDENT',
    real_name VARCHAR(50) NOT NULL,
    class_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 社团表 (clubs)
CREATE TABLE clubs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    president_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (president_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 活动表 (activities)
CREATE TABLE activities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    club_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    activity_time DATETIME NOT NULL,
    location VARCHAR(200) NOT NULL,
    description TEXT,
    status ENUM('PENDING', 'PUBLISHED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 报名记录表 (signups)
CREATE TABLE signups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity_id INT NOT NULL,
    signup_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    UNIQUE KEY unique_signup (user_id, activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入管理员账号
INSERT INTO users (username, password, role, real_name, class_name) VALUES
('admin', 'admin123', 'ADMIN', '系统管理员', '计算机学院');

-- 插入学生账号
INSERT INTO users (username, password, role, real_name, class_name) VALUES
('student1', 'student123', 'STUDENT', '张三', '计算机科学与技术2023级1班');

-- 插入社团数据
INSERT INTO clubs (name, description, president_id) VALUES
('计算机协会', '专注于计算机技术和编程的学术社团', 1);

-- 插入活动数据
INSERT INTO activities (club_id, title, activity_time, location, description, status) VALUES
(1, 'Java编程入门讲座', '2024-06-15 14:00:00', '教学楼A101', '面向初学者的Java编程基础讲座', 'PUBLISHED'),
(1, 'Python数据分析实战', '2024-06-20 15:30:00', '实验楼B201', '使用Python进行数据分析的实践课程', 'PUBLISHED');

-- 插入报名记录
INSERT INTO signups (user_id, activity_id) VALUES
(2, 1),  -- 学生1报名Java讲座
(2, 2);  -- 学生1报名Python课程

-- ============================================
-- 查询测试数据
-- ============================================
SELECT '用户表数据:' AS '';
SELECT * FROM users;

SELECT '社团表数据:' AS '';
SELECT * FROM clubs;

SELECT '活动表数据:' AS '';
SELECT * FROM activities;

SELECT '报名记录:' AS '';
SELECT * FROM signups;
