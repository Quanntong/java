# 社团活动管理系统 - 项目结构

## 项目概述
Java课程大作业 - 社团活动管理系统，采用分层架构设计，使用Java Swing实现GUI界面。

## 技术栈
- 语言：Java (JDK 8+)
- 界面库：Java Swing
- 数据库：MySQL 8.0 (支持演示模式)
- 连接方式：原生 JDBC
- 开发工具：IntelliJ IDEA

## 项目结构

### 根目录文件
```
java.design/
├── pom.xml                    # Maven配置文件
├── README.md                  # 项目说明文档
├── PROJECT_STRUCTURE.md       # 项目结构文档
├── PROJECT_STRUCTURE_FINAL.md # 最终项目结构文档
├── PROJECT_STRUCTURE_CURRENT.md # 当前项目结构文档
├── database_schema.sql        # 数据库建表脚本
├── database_schema_simple.sql # 简化版数据库脚本
├── test_app.bat              # 应用程序启动脚本
└── lib/                      # 依赖库目录
```

### 源代码结构 (src/main/java/com/clubmanagement/)
```
com/clubmanagement/
├── App.java                  # 应用程序主类
│
├── common/                   # 公共模块
│   ├── Constants.java       # 常量定义
│   └── exception/           # 异常类
│       ├── BusinessException.java  # 业务异常
│       └── SystemException.java    # 系统异常
│
├── model/                   # 实体类
│   ├── User.java           # 用户实体
│   ├── Club.java           # 社团实体
│   └── Activity.java       # 活动实体
│
├── dao/                    # 数据访问层
│   ├── IUserDAO.java      # 用户DAO接口
│   ├── IClubDAO.java      # 社团DAO接口
│   ├── IActivityDAO.java  # 活动DAO接口
│   ├── UserDAO.java       # 旧版用户DAO（兼容）
│   ├── ClubDAO.java       # 旧版社团DAO（兼容）
│   └── impl/              # DAO实现类
│       ├── UserDAOImpl.java
│       ├── ClubDAOImpl.java
│       └── ActivityDAOImpl.java
│
├── service/               # 业务逻辑层
│   ├── IUserService.java     # 用户服务接口
│   ├── IClubService.java     # 社团服务接口
│   ├── IActivityService.java # 活动服务接口
│   ├── UserService.java      # 旧版用户服务（兼容）
│   ├── ClubService.java      # 旧版社团服务（兼容）
│   └── impl/                 # 服务实现类
│       ├── UserServiceImpl.java
│       ├── ClubServiceImpl.java
│       └── ActivityServiceImpl.java
│
├── util/                 # 工具类
│   ├── DBUtil.java      # 数据库工具类（支持连接池和演示模式）
│   ├── SecurityUtil.java # 安全工具类（MD5加密）
│   └── DateUtil.java    # 日期工具类
│
└── view/                # 视图层（Swing界面）
    ├── LoginFrame.java      # 登录窗口
    ├── MainFrame.java       # 主窗口
    ├── ClubListPanel.java   # 社团列表面板
    ├── UserCenterPanel.java # 用户中心面板
    └── ActivityPanel.java   # 活动管理面板
```

### 资源文件 (src/main/resources/)
```
resources/
└── db.properties        # 数据库配置文件
```

### 测试代码 (src/test/java/com/clubmanagement/)
```
test/java/com/clubmanagement/
└── TestDBConnection.java  # 数据库连接测试
```

### 编译输出 (target/)
```
target/
├── classes/             # 编译后的类文件
└── test-classes/       # 测试类文件
```

## 数据库配置
配置文件：`src/main/resources/db.properties`
```properties
# MySQL数据库配置
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/club_management?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
db.username=root
db.password=123456
```

## 运行说明

### 1. 数据库准备
1. 安装MySQL 8.0+
2. 创建数据库：`club_management`
3. 执行建表脚本：`database_schema.sql`

### 2. 运行应用程序
方式1：使用批处理文件
```bash
test_app.bat
```

方式2：直接运行
```bash
cd c:\Users\Lenovo\VScodeproject\java.design
java -cp "target/classes" com.clubmanagement.App
```

### 3. 测试账号
- 管理员：admin / 123456
- 学生：student / 123456

## 演示模式
当MySQL驱动未找到或数据库连接失败时，系统会自动切换到演示模式：
- 使用模拟数据展示界面功能
- 支持预定义用户登录
- 支持模拟用户注册

## 架构特点
1. **分层架构**：清晰的model-dao-service-view分层
2. **接口编程**：使用接口定义契约，便于扩展
3. **连接池**：简单的数据库连接池实现
4. **异常处理**：统一的业务异常和系统异常处理
5. **密码安全**：MD5加密存储用户密码
6. **演示支持**：无数据库环境下仍可运行演示

## 主要功能模块
1. **用户管理**：登录、注册、用户信息管理
2. **社团管理**：社团创建、查询、成员管理
3. **活动管理**：活动发布、报名、审核
4. **权限控制**：管理员和学生不同权限
5. **数据统计**：用户统计、活动统计

## 开发规范
1. 包名：com.clubmanagement.模块名
2. 类名：大驼峰命名法
3. 方法名：小驼峰命名法
4. 常量：全大写，下划线分隔
5. 注释：JavaDoc格式注释

## 后续扩展建议
1. 添加更多社团管理功能
2. 实现活动报名和审核流程
3. 添加数据导出功能
4. 优化界面用户体验
5. 添加单元测试
6. 实现数据缓存机制
