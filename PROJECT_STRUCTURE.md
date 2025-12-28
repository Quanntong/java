# 社团活动管理系统 - 项目结构

## 技术栈
- 语言：Java (JDK 8+)
- 界面库：Java Swing (GUI)
- 数据库：MySQL 8.0
- 连接方式：原生 JDBC
- 开发工具：IntelliJ IDEA

## 系统角色
1. 管理员 (Admin)：管理社团、审批活动、管理用户
2. 学生 (Student)：注册/登录、查看社团、申请加入社团、报名参加活动

## 架构模式
分层架构 (Layered Architecture)

## 项目包结构
```
src/main/java/com/clubmanagement/
├── model/          # 实体类
│   ├── User.java          # 用户实体
│   ├── Club.java          # 社团实体
│   ├── Activity.java      # 活动实体
│   ├── Membership.java    # 社团成员关系
│   └── Enrollment.java    # 活动报名
├── dao/           # 数据访问层
│   ├── UserDAO.java
│   ├── ClubDAO.java
│   ├── ActivityDAO.java
│   ├── MembershipDAO.java
│   └── EnrollmentDAO.java
├── service/       # 业务逻辑层
│   ├── UserService.java
│   ├── ClubService.java
│   ├── ActivityService.java
│   ├── MembershipService.java
│   └── EnrollmentService.java
├── view/          # Swing图形界面层
│   ├── LoginFrame.java        # 登录界面
│   ├── AdminDashboard.java    # 管理员面板
│   ├── StudentDashboard.java  # 学生面板
│   ├── ClubManagementFrame.java
│   └── ActivityManagementFrame.java
└── util/          # 工具类
    └── DBUtil.java            # 数据库连接工具

src/main/resources/
└── db.properties  # 数据库配置文件
```

## 数据库配置
配置文件：`src/main/resources/db.properties`
```properties
# 数据库连接配置
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/club_management?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
db.username=root
db.password=123456

# 连接池配置
db.initialSize=5
db.maxActive=20
db.maxWait=3000
db.minIdle=3
```

## 数据库设计

已创建完整的数据库SQL脚本，包含以下四张核心表：

### 核心表结构
```sql
-- 1. 用户表 (users)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL DEFAULT 'STUDENT',
    real_name VARCHAR(50) NOT NULL,
    class_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 社团表 (clubs)
CREATE TABLE clubs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    president_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (president_id) REFERENCES users(id) ON DELETE SET NULL
);

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
);

-- 4. 报名记录表 (signups)
CREATE TABLE signups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity_id INT NOT NULL,
    signup_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    UNIQUE KEY unique_signup (user_id, activity_id)
);
```

### 测试数据
```sql
-- 管理员账号
INSERT INTO users (username, password, role, real_name, class_name) VALUES
('admin', 'admin123', 'ADMIN', '系统管理员', '计算机学院');

-- 学生账号
INSERT INTO users (username, password, role, real_name, class_name) VALUES
('student1', 'student123', 'STUDENT', '张三', '计算机科学与技术2023级1班');

-- 社团数据
INSERT INTO clubs (name, description, president_id) VALUES
('计算机协会', '专注于计算机技术和编程的学术社团', 1);

-- 活动数据
INSERT INTO activities (club_id, title, activity_time, location, description, status) VALUES
(1, 'Java编程入门讲座', '2024-06-15 14:00:00', '教学楼A101', '面向初学者的Java编程基础讲座', 'PUBLISHED'),
(1, 'Python数据分析实战', '2024-06-20 15:30:00', '实验楼B201', '使用Python进行数据分析的实践课程', 'PUBLISHED');

-- 报名记录
INSERT INTO signups (user_id, activity_id) VALUES
(2, 1),  -- 学生1报名Java讲座
(2, 2);  -- 学生1报名Python课程
```

### 扩展表（可选）
还提供了扩展版本，包含社团成员表等额外功能，详见 `database_schema.sql` 文件。

### SQL文件
项目包含两个SQL文件：
1. `database_schema_simple.sql` - 简化版，只包含四张核心表
2. `database_schema.sql` - 完整版，包含所有表和更多功能

## 下一步开发建议
1. 创建实体类 (model包)
2. 实现DAO层 (dao包)
3. 实现Service层 (service包)
4. 设计Swing界面 (view包)
5. 编写业务逻辑
6. 测试系统功能

## 运行要求
1. 安装MySQL 8.0并创建数据库 `club_management`
2. 导入上述SQL表结构
3. 修改`db.properties`中的数据库连接信息
4. 添加MySQL JDBC驱动到项目依赖
5. 使用IntelliJ IDEA打开项目并运行
