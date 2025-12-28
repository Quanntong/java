# 社团活动管理系统

## 项目概述
一个基于Java Swing和MySQL的社团活动管理系统，用于大学社团管理和活动组织。系统采用分层架构设计，包含管理员和学生两种角色。

## 技术栈
- **语言**: Java (JDK 8+)
- **界面库**: Java Swing (GUI)
- **数据库**: MySQL 8.0
- **连接方式**: 原生 JDBC
- **开发工具**: IntelliJ IDEA
- **构建工具**: Maven

## 系统功能

### 用户角色
1. **管理员 (Admin)**
   - 管理用户账号
   - 创建和管理社团
   - 审批活动申请
   - 查看系统数据

2. **学生 (Student)**
   - 注册和登录系统
   - 查看社团列表
   - 申请加入社团
   - 报名参加活动
   - 查看个人中心

### 核心功能模块
1. **用户管理**: 登录、注册、个人信息管理
2. **社团管理**: 社团创建、列表展示、申请加入
3. **活动管理**: 活动创建、列表展示、报名参加
4. **个人中心**: 个人信息查看和修改

## 项目结构

### 分层架构
```
src/main/java/com/clubmanagement/
├── model/          # 实体类层
│   ├── User.java          # 用户实体
│   └── Club.java          # 社团实体
├── dao/           # 数据访问层
│   ├── UserDAO.java       # 用户数据访问
│   └── ClubDAO.java       # 社团数据访问
├── service/       # 业务逻辑层
│   ├── UserService.java   # 用户业务逻辑
│   └── ClubService.java   # 社团业务逻辑
├── view/          # 图形界面层
│   ├── LoginFrame.java        # 登录窗口
│   ├── MainFrame.java         # 主界面
│   ├── ClubListPanel.java     # 社团列表面板
│   ├── ActivityPanel.java     # 活动大厅面板
│   └── UserCenterPanel.java   # 个人中心面板
└── util/          # 工具类
    └── DBUtil.java            # 数据库连接工具
```

### 数据库设计
系统包含以下核心表：
1. **users表**: 用户信息
2. **clubs表**: 社团信息
3. **activities表**: 活动信息
4. **signups表**: 报名记录

详细SQL脚本见 `database_schema.sql` 和 `database_schema_simple.sql`

## 运行指南

### 环境要求
1. **Java环境**: JDK 8或更高版本
2. **数据库**: MySQL 8.0
3. **开发工具**: IntelliJ IDEA (推荐) 或 Eclipse

### 数据库配置
1. 安装MySQL 8.0并启动服务
2. 创建数据库:
   ```sql
   CREATE DATABASE club_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 导入表结构:
   ```bash
   mysql -u root -p club_management < database_schema_simple.sql
   ```
4. 修改数据库连接配置 (`src/main/resources/db.properties`):
   ```properties
   db.url=jdbc:mysql://localhost:3306/club_management
   db.username=your_username
   db.password=your_password
   ```

### 项目导入与运行

#### 方法一: 使用IntelliJ IDEA
1. 打开IntelliJ IDEA
2. 选择 "Open" 并选择项目目录
3. 等待Maven依赖下载完成
4. 运行 `App.java` 中的 `main` 方法

#### 方法二: 命令行运行
```bash
# 编译项目
mvn compile

# 运行应用程序
mvn exec:java -Dexec.mainClass="com.clubmanagement.App"
```

#### 方法三: 直接运行JAR
```bash
# 打包项目
mvn package

# 运行JAR文件
java -jar target/club-management-system-1.0.0.jar
```

### 测试账号
系统预置了测试账号:
- **管理员**: 用户名 `admin`, 密码 `admin123`
- **学生**: 用户名 `student1`, 密码 `student123`

## 使用说明

### 1. 登录系统
- 运行应用程序后显示登录窗口
- 输入用户名和密码登录
- 支持新用户注册

### 2. 主界面导航
- 顶部显示欢迎信息和退出按钮
- 左侧导航栏: 社团列表、活动大厅、个人中心
- 中间区域显示当前功能面板

### 3. 功能使用
- **社团列表**: 查看所有社团，管理员可创建社团，学生可申请加入
- **活动大厅**: 查看所有活动，学生可报名参加，管理员可创建活动
- **个人中心**: 查看和修改个人信息

## 开发说明

### 代码规范
- 使用Java命名规范
- 所有类和方法都有Javadoc注释
- 遵循分层架构设计原则
- 使用PreparedStatement防止SQL注入

### 扩展功能建议
1. **活动管理**: 实现完整的Activity实体类和DAO/Service
2. **报名管理**: 实现signups表的完整CRUD操作
3. **社团成员管理**: 实现club_members表的管理功能
4. **活动审核**: 管理员审核活动发布
5. **数据统计**: 社团和活动的统计报表

### 调试建议
1. **数据库连接问题**: 检查db.properties配置和MySQL服务状态
2. **驱动问题**: 确保mysql-connector-java依赖已正确导入
3. **界面显示问题**: 检查Swing组件布局和事件监听

## 项目文档

### 已生成文档
1. `PROJECT_STRUCTURE.md`: 项目结构详细说明
2. `database_schema.sql`: 完整数据库设计
3. `database_schema_simple.sql`: 简化版数据库设计
4. `pom.xml`: Maven项目配置

### 待完善文档
1. 详细设计文档 (基于代码注释生成)
2. 用户手册
3. 测试报告

## 许可证
本项目为Java课程大作业作品，仅供学习参考。

## 作者
Java课程大作业小组

## 版本历史
- v1.0.0 (2024-06): 初始版本，实现基本功能
- 计划功能: 活动管理、报名管理、数据统计等

---

**提示**: 首次运行前请确保数据库配置正确，并导入SQL脚本创建表结构和测试数据。
