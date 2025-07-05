# 问卷调查系统 (QuestionaireSurvey)

一个基于 Spring Boot + MyBatis + MySQL 的现代化问卷调查系统后端，支持多种题型、用户管理、数据统计和文件上传等功能。

## 📋 项目概述

本项目是一个功能完整的问卷调查系统后端，提供了从问卷创建、发布、答题到数据统计的完整流程。系统采用前后端分离架构，支持多用户、权限管理和数据导出等企业级功能。

## 🚀 核心功能

### 用户管理
- **用户注册/登录**: 基于JWT的身份认证
  - **角色权限**: 支持普通用户(USER)和管理员(ADMIN)角色
  - **个人资料**: 用户可更新个人信息、密码和头像
  - **用户状态管理**: 管理员可管理用户状态

### 问卷管理
- **问卷创建**: 支持创建多种类型的问卷
  - **问卷类型**:
      - 公开问卷(PUBLIC): 任何人都可以访问
      - 私有问卷(PRIVATE): 需要访问码
  - **问卷状态**: 草稿(0)、发布(1)、结束(2)、删除(3)
  - **问卷设置**: 支持开始/结束时间、访问控制等

### 题目类型
- **单选题(RADIO)**: 单项选择
  - **多选题(CHECKBOX)**: 多项选择
  - **文本题(TEXT)**: 文本输入
  - **评分题(RATING)**: 评分选择
  - **文件题(FILE)**: 文件上传

### 答题功能
- **匿名答题**: 支持匿名用户参与
  - **登录答题**: 注册用户可登录后答题
  - **访问控制**: 私有问卷需要访问码
  - **设备信息**: 记录答题者IP和设备信息

### 数据统计
- **统计分析**: 自动生成问卷统计报告
  - **选择题统计**: 选项分布统计
  - **原始数据**: 查看每个问题的原始答案
  - **数据导出**: 支持CSV格式数据导出

### 文件管理
- **阿里云OSS**: 集成阿里云对象存储
  - **文件上传**: 支持10MB以内文件上传
  - **文件组织**: 按日期自动分类存储

## 🛠 技术栈

### 后端框架
- **Spring Boot 3.4.6**: 主框架
  - **Spring Security**: 安全认证
  - **MyBatis 3.0.4**: 数据持久化
  - **PageHelper**: 分页插件

### 数据库
- **MySQL**: 主数据库
  - **Redis**: 缓存(配置中)
  - **Druid**: 数据库连接池

### 认证授权
- **JWT (JSON Web Token)**: 无状态认证
  - **BCrypt**: 密码加密

### 文件存储
- **阿里云OSS**: 对象存储服务

### 开发工具
- **Lombok**: 简化Java代码
  - **Jackson**: JSON处理

## 📁 项目结构

```
src/main/java/com/example/survey/
├── QuestionaireSurveyApplication.java    # 主启动类
├── common/                               # 通用组件
│   ├── GlobalExceptionHandler.java       # 全局异常处理
│   └── Result.java                       # 统一响应格式
├── config/                               # 配置类
│   ├── SecurityConfig.java               # Spring Security配置
│   ├── JwtRequestFilter.java             # JWT过滤器
│   └── JwtAuthenticationEntryPoint.java  # JWT认证入口
├── controller/                           # 控制器层
│   ├── AuthController.java               # 认证控制器
│   ├── UserController.java               # 用户管理
│   ├── SurveyController.java             # 问卷管理
│   ├── QuestionController.java           # 题目管理
│   ├── SubmissionController.java         # 答题提交
│   ├── StatsController.java              # 数据统计
│   ├── AdminController.java              # 管理员功能
│   ├── FileUploadController.java         # 文件上传
│   └── dto/                              # 数据传输对象
├── entity/                               # 实体类
│   ├── User.java                         # 用户实体
│   ├── Survey.java                       # 问卷实体
│   ├── Question.java                     # 题目实体
│   ├── ResponseHeader.java               # 答卷头实体
│   ├── ResponseDetail.java               # 答卷详情实体
│   └── AliyunOssProperity.java          # OSS配置实体
├── mapper/                               # MyBatis映射接口
│   ├── UserMapper.java
│   ├── SurveyMapper.java
│   ├── QuestionMapper.java
│   ├── ResponseHeaderMapper.java
│   └── ResponseDetailMapper.java
├── service/                              # 服务层
│   ├── UserService.java
│   ├── SurveyService.java
│   ├── QuestionService.java
│   ├── SubmissionService.java
│   ├── StatsService.java
│   ├── AdminService.java
│   ├── StorageService.java
│   └── ServiceImpl/                      # 服务实现
├── utils/                                # 工具类
│   ├── JwtTokenUtil.java                 # JWT工具
│   └── AliyunOSSOperator.java           # OSS操作工具
└── exception/                            # 自定义异常
    ├── ResourceNotFoundException.java
    ├── ForbiddenException.java
    ├── InvalidRequestException.java
    ├── InvalidSubmissionDataException.java
    └── TooManyRequestsException.java
```

## 🗄 数据库设计

### 核心表结构

#### 用户表 (user)
- `id`: 主键
  - `username`: 用户名
  - `password`: 加密密码
  - `email`: 邮箱
  - `avatar`: 头像URL
  - `role`: 角色(USER/ADMIN)
  - `status`: 状态(0-正常/1-受限)
  - `created_at/updated_at`: 时间戳

#### 问卷表 (survey)
- `id`: 主键
  - `title`: 问卷标题
  - `description`: 问卷描述
  - `creator_id`: 创建者ID
  - `type`: 类型(PUBLIC/PRIVATE)
  - `access_code`: 访问码
  - `status`: 状态(0-草稿/1-发布/2-结束/3-删除)
  - `start_time/end_time`: 有效时间
  - `settings`: JSON配置
  - `created_at/updated_at`: 时间戳

#### 题目表 (question)
- `id`: 主键
  - `survey_id`: 所属问卷ID
  - `content`: 题目内容(JSON)
  - `type`: 题目类型(RADIO/CHECKBOX/TEXT/RATING/FILE)
  - `is_required`: 是否必答
  - `options`: 选项(JSON)
  - `logic`: 逻辑配置(JSON)
  - `sort_order`: 排序
  - `created_at`: 创建时间

#### 答卷头表 (response_header)
- `id`: 主键
  - `survey_id`: 问卷ID
  - `session_id`: 会话ID
  - `user_id`: 用户ID(可为空)
  - `ip_address`: IP地址
  - `device_info`: 设备信息
  - `submitted_at`: 提交时间

#### 答卷详情表 (response_detail)
- `id`: 主键
  - `response_id`: 答卷头ID
  - `question_id`: 题目ID
  - `answer_data`: 答案数据(JSON)
  - `created_at`: 创建时间

## 🔌 API 接口

### 认证接口 (/api/auth)
- `POST /register` - 用户注册
  - `POST /login` - 用户登录

### 用户接口 (/api/users)
- `GET /me` - 获取当前用户信息
  - `PUT /me` - 更新用户资料
  - `PUT /me/password` - 修改密码
  - `PUT /me/username` - 修改用户名

### 问卷接口 (/api/surveys)
- `POST /` - 创建问卷
  - `GET /user` - 获取当前用户的问卷列表
  - `GET /public` - 获取公开问卷列表
  - `GET /{id}` - 获取问卷详情
  - `GET /{id}/details` - 获取公开问卷详情(供答题)
  - `GET /{surveyId}/responses` - 获取问卷回答列表

### 题目接口 (/api/surveys/{surveyId}/questions)
- `POST /batch` - 批量添加题目
  - `PUT /{questionId}` - 更新题目
  - `DELETE /{questionId}` - 删除题目

### 答题接口 (/api/submit)
- `POST /{surveyId}` - 提交问卷答案

### 统计接口 (/api/stats)
- `GET /survey/{surveyId}` - 获取问卷统计
  - `GET /survey/{surveyId}/export` - 导出问卷数据
  - `GET /survey/{surveyId}/question/{questionId}/answers` - 获取题目原始答案

### 管理员接口 (/api/admin)
- `GET /users` - 获取用户列表
  - `PATCH /users/{id}/status` - 更新用户状态
  - `GET /surveys` - 获取所有问卷
  - `DELETE /surveys/{id}` - 删除问卷

### 文件上传接口 (/api/upload)
- `POST /` - 上传文件

## ⚙️ 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/web
    username: [数据库用户名]
    password: [数据库密码]
```

### JWT配置
```yaml
jwt:
  secret: [JWT密钥]
  expiration: 360000  # 过期时间(毫秒)
```

### 阿里云OSS配置
```yaml
aliyun:
  oss:
    endpoint: [OSS端点]
    bucketname: [存储桶名称]
    region: [区域]
```

### Redis配置
```yaml
redis:
  host: localhost
  port: 6379
```

## 🔒 安全特性

- **JWT认证**: 无状态的用户认证
  - **密码加密**: 使用BCrypt加密存储
  - **CORS支持**: 跨域资源共享配置
  - **权限控制**: 基于角色的访问控制
  - **异常处理**: 全局异常处理机制

## 📊 特色功能

### 1. 灵活的题目系统
- 支持多种题目类型
  - JSON格式存储题目内容和选项
  - 支持题目逻辑配置

### 2. 完善的统计分析
- 实时统计数据
  - 选择题自动统计选项分布
  - 支持原始数据查看和导出

### 3. 多租户支持
- 用户隔离的问卷管理
  - 公开/私有问卷访问控制
  - 管理员全局管理功能

### 4. 文件处理
- 集成阿里云OSS
  - 自动文件分类存储
  - 支持多种文件格式

## 🚀 快速开始

### 环境要求
- Java 17+
  - MySQL 8.0+
  - Maven 3.6+
  - Redis (可选)

### 运行步骤
1. 克隆项目到本地
   2. 创建MySQL数据库 `web`
   3. 配置 `application.yml` 中的数据库连接
   4. 配置JWT密钥和阿里云OSS参数
   5. 运行 `mvn spring-boot:run`

### 默认端口
- 应用端口: 8080
  - 数据库端口: 3306
  - Redis端口: 6379

## 📝 开发说明

### 代码规范
- 使用Lombok简化代码
  - 统一的Result响应格式
  - 完善的异常处理机制
  - 详细的接口文档注释

### 测试
- 包含单元测试用例
  - 使用Spring Boot Test框架
  - 数据库操作测试覆盖

## 🤝 贡献指南

1. Fork 项目
   2. 创建特性分支
   3. 提交更改
   4. 推送到分支
   5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
  - 发送邮件
  - 创建 Pull Request

---

**注意**: 在生产环境中使用前，请确保：
1. 修改默认的JWT密钥
   2. 配置正确的数据库连接
   3. 设置阿里云OSS访问凭证
   4. 启用HTTPS
   5. 配置适当的日志级别
