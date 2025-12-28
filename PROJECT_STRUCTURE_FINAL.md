# 社团活动管理系统 - 项目结构

## 项目根目录
```
java.design/
├── 数据库设计文件/
│   ├── database_schema.sql          # 完整数据库设计
│   └── database_schema_simple.sql   # 简化版数据库设计
├── 项目配置文件/
│   ├── pom.xml                     # Maven项目配置
│   └── PROJECT_STRUCTURE.md        # 项目结构说明
├── 项目文档/
│   └── README.md                   # 完整项目说明和运行指南
└── 源代码目录/
    └── src/
```

## 源代码结构 (src/main/java/com/clubmanagement/)

### 1. 主应用程序入口
- `App.java` - 主应用程序启动类

### 2. 实体类层 (model/)
- `User.java` - 用户实体类，对应数据库users表
- `Club.java` - 社团实体类，对应数据库clubs表

### 3. 数据访问层 (dao/)
- `UserDAO.java` - 用户数据访问对象，负责users表的CRUD操作
- `ClubDAO.java` - 社团数据访问对象，负责clubs表的CRUD操作

### 4. 业务逻辑层 (service/)
- `UserService.java` - 用户业务逻辑，处理登录、注册等业务
- `ClubService.java` - 社团业务逻辑，处理社团创建、查询等业务

### 5. 工具类层 (util/)
- `DBUtil.java` - 数据库连接工具类，管理数据库连接和事务

### 6. 图形界面层 (view/)
- `LoginFrame.java` - 登录窗口，提供用户登录和注册功能
- `MainFrame.java` - 主界面框架，使用CardLayout管理功能面板
- `ClubListPanel.java` - 社团列表面板，显示社团信息
- `ActivityPanel.java` - 活动大厅面板，显示活动信息
- `UserCenterPanel.java` - 个人中心面板，管理个人信息

## 资源文件 (src/main/resources/)
- `db.properties` - 数据库连接配置文件

## 编译输出 (target/)
- `target/classes/` - 编译后的Java类文件
- `target/test-classes/` - 测试类文件目录

## 文件统计

### Java源文件 (15个)
```
1.  App.java
2.  model/User.java
3.  model/Club.java
4.  dao/UserDAO.java
5.  dao/ClubDAO.java
6.  service/UserService.java
7.  service/ClubService.java
8.  util/DBUtil.java
9.  view/LoginFrame.java
10. view/MainFrame.java
11. view/ClubListPanel.java
12. view/ActivityPanel.java
13. view/UserCenterPanel.java
```

### 配置文件 (5个)
```
1. pom.xml
2. db.properties
3. database_schema.sql
4. database_schema_simple.sql
5. PROJECT_STRUCTURE.md
```

### 文档文件 (1个)
```
1. README.md
```

## 分层架构说明

### 第一层: 实体类层 (Model)
- 职责: 定义数据模型，对应数据库表结构
- 文件: User.java, Club.java
- 特点: 纯数据对象，包含属性和Getter/Setter方法

### 第二层: 数据访问层 (DAO)
- 职责: 封装数据库操作，提供CRUD接口
- 文件: UserDAO.java, ClubDAO.java
- 特点: 使用PreparedStatement防止SQL注入，统一资源管理

### 第三层: 业务逻辑层 (Service)
- 职责: 处理业务规则，协调多个DAO操作
- 文件: UserService.java, ClubService.java
- 特点: 参数验证，业务规则检查，异常处理

### 第四层: 图形界面层 (View)
- 职责: 提供用户界面，处理用户交互
- 文件: LoginFrame.java, MainFrame.java等
- 特点: Swing组件，事件驱动，线程安全

### 工具层 (Util)
- 职责: 提供通用工具功能
- 文件: DBUtil.java
- 特点: 数据库连接管理，事务支持

## 数据库设计文件

### 完整版 (database_schema.sql)
- 包含所有表的完整定义
- 包含外键约束和索引
- 包含示例数据

### 简化版 (database_schema_simple.sql)
- 只包含核心表结构
- 适合快速部署
- 包含基础测试数据

## 项目配置

### Maven配置 (pom.xml)
- Java版本: 1.8+
- 依赖: MySQL Connector/J
- 构建配置: 编译和打包设置

### 数据库配置 (db.properties)
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/club_management
db.username=your_username
db.password=your_password
```

## 运行说明

### 环境要求
1. Java JDK 8或更高版本
2. MySQL 8.0数据库
3. Maven构建工具 (可选)

### 部署步骤
1. 创建MySQL数据库: `club_management`
2. 导入SQL脚本: `database_schema_simple.sql`
3. 修改数据库配置: `db.properties`
4. 编译项目: `mvn compile` 或使用IDE
5. 运行应用程序: `java -cp target/classes com.clubmanagement.App`

## 项目特点总结

1. **架构清晰**: 严格的分层架构设计
2. **代码规范**: 完整的Javadoc注释和命名规范
3. **安全性高**: SQL注入防护和输入验证
4. **功能完整**: 用户管理、社团管理、活动管理
5. **界面友好**: Swing图形用户界面
6. **文档齐全**: 完整的项目文档和运行指南

项目已准备好作为Java课程大作业提交，所有无关文件已清理，只保留核心功能代码。
