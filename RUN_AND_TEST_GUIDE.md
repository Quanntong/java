# 社团活动管理系统 - 运行和测试指南

## 目录
1. [环境要求](#环境要求)
2. [运行方式](#运行方式)
3. [测试步骤](#测试步骤)
4. [功能测试](#功能测试)
5. [常见问题](#常见问题)

## 环境要求

### 1. Java环境
- **JDK版本**：JDK 8 或更高版本
- **验证方法**：打开命令提示符，运行 `java -version`
- **预期输出**：显示Java版本信息

### 2. 项目结构
确保项目目录包含以下文件：
```
java.design/
├── pom.xml                    # Maven配置文件
├── test_app.bat              # 启动脚本
├── target/classes/           # 编译后的类文件
└── src/                      # 源代码目录
```

## 运行方式

### 方式1：使用批处理文件（推荐）
1. 双击 `test_app.bat` 文件
2. 或命令行运行：
   ```bash
   cd c:\Users\Lenovo\VScodeproject\java.design
   test_app.bat
   ```

### 方式2：直接运行Java程序
```bash
cd c:\Users\Lenovo\VScodeproject\java.design
java -cp "target/classes" com.clubmanagement.App
```

### 方式3：使用Maven运行
```bash
cd c:\Users\Lenovo\VScodeproject\java.design
mvn compile exec:java -Dexec.mainClass="com.clubmanagement.App"
```

## 测试步骤

### 步骤1：验证Java环境
```bash
java -version
```
预期输出：
```
java version "1.8.0_XXX"
Java(TM) SE Runtime Environment (build 1.8.0_XXX-XXX)
Java HotSpot(TM) 64-Bit Server VM (build 25.XXX-bXX, mixed mode)
```

### 步骤2：验证项目编译状态
```bash
cd c:\Users\Lenovo\VScodeproject\java.design
dir target\classes\com\clubmanagement\App.class
```
预期输出：显示App.class文件存在

### 步骤3：运行应用程序
```bash
cd c:\Users\Lenovo\VScodeproject\java.design
java -cp "target/classes" com.clubmanagement.App
```
预期输出：
```
=== 社团活动管理系统启动 ===
作者: Java课程大作业
版本: 1.0.0
==========================
登录窗口已启动
警告: 数据库驱动加载失败，启用演示模式
```

## 功能测试

### 测试1：登录功能
**测试账号**：
1. 管理员：`admin` / `123456`
2. 学生：`student` / `123456`

**测试步骤**：
1. 运行应用程序
2. 在登录窗口输入用户名和密码
3. 点击"登录"按钮
4. 验证登录结果

**预期结果**：
- 成功登录后显示主窗口
- 管理员登录显示管理员功能菜单
- 学生登录显示学生功能菜单

### 测试2：注册功能
**测试步骤**：
1. 在登录窗口点击"注册"按钮
2. 填写注册信息：
   - 用户名：`testuser`
   - 密码：`test123`
   - 确认密码：`test123`
   - 真实姓名：`测试用户`
   - 班级：`测试班级`
   - 角色：选择"学生"
3. 点击"注册"按钮

**预期结果**：
- 显示"注册成功"提示
- 可以使用新账号登录

### 测试3：社团管理功能（管理员）
**前提**：使用管理员账号登录

**测试步骤**：
1. 在主窗口点击"社团管理"菜单
2. 测试功能：
   - 点击"新建社团"按钮
   - 填写社团信息
   - 点击"保存"按钮
   - 在列表中查看新建的社团

### 测试4：活动管理功能
**测试步骤**：
1. 在主窗口点击"活动管理"菜单
2. 测试功能：
   - 查看活动列表
   - 点击"新建活动"按钮
   - 填写活动信息
   - 点击"发布"按钮

### 测试5：用户中心功能
**测试步骤**：
1. 在主窗口点击"用户中心"菜单
2. 测试功能：
   - 查看个人信息
   - 修改密码
   - 查看参加的活动

## 演示模式说明

### 当前状态
由于缺少MySQL驱动，系统运行在**演示模式**：
- 使用模拟数据
- 支持预定义用户登录
- 支持模拟注册功能
- 界面功能完整展示

### 切换到真实数据库模式
如需使用真实数据库：

1. **安装MySQL驱动**：
   ```bash
   # 下载mysql-connector-java-8.0.XX.jar
   # 放到项目lib目录下
   ```

2. **配置数据库**：
   - 修改 `src/main/resources/db.properties`
   - 设置正确的数据库连接信息

3. **创建数据库**：
   - 运行MySQL
   - 执行 `database_schema.sql` 创建表结构

## 常见问题

### 问题1：Java版本不兼容
**症状**：运行时报错 `UnsupportedClassVersionError`
**解决**：安装JDK 8或更高版本

### 问题2：类文件找不到
**症状**：运行时报错 `ClassNotFoundException`
**解决**：
```bash
# 重新编译项目
cd c:\Users\Lenovo\VScodeproject\java.design
mvn clean compile
```

### 问题3：内存不足
**症状**：运行时报错 `OutOfMemoryError`
**解决**：增加JVM内存
```bash
java -Xmx512m -cp "target/classes" com.clubmanagement.App
```

### 问题4：界面显示异常
**症状**：界面布局错乱或显示不正常
**解决**：
1. 检查屏幕分辨率
2. 尝试调整窗口大小
3. 重启应用程序

## 测试报告模板

### 测试记录
| 测试项目 | 测试步骤 | 预期结果 | 实际结果 | 状态 |
|---------|---------|---------|---------|------|
| 登录功能 | 使用admin/123456登录 | 成功登录，显示主窗口 | | |
| 注册功能 | 注册新用户testuser | 注册成功，可登录 | | |
| 社团管理 | 管理员创建新社团 | 社团创建成功 | | |
| 活动管理 | 发布新活动 | 活动发布成功 | | |
| 用户中心 | 查看个人信息 | 正确显示用户信息 | | |

### 问题记录
| 问题描述 | 重现步骤 | 解决方案 | 状态 |
|---------|---------|---------|------|
| | | | |

## 联系方式
如有问题，请参考：
1. 项目文档：`README.md`
2. 项目结构：`PROJECT_STRUCTURE_CURRENT.md`
3. 数据库脚本：`database_schema.sql`

---

**提示**：系统当前运行在演示模式，所有数据均为模拟数据。如需真实数据库功能，请配置MySQL数据库。
