# RainbowBlog


## 项目介绍
欢迎来到我的个人博客项目，这是一个基于SpringBoot+Vue2搭建的个人博客项目（前后台）。该项目旨在提供一个简洁、易用且可定制的平台，用于分享我关于科技、编程以及其他领域的见解和知识。

### 主要特性
- **API驱动**：后端通过RESTful API为前端提供数据支持，实现清晰的数据交互逻辑。
- **文章管理后台**：管理员可以登录后台进行文章创建、编辑、删除等操作，并支持Markdown格式内容编写。
- **分类与标签**：对文章进行分类管理和标签关联，便于用户查找和浏览相关主题内容。
- **评论系统**：读者可以针对每篇文章发表评论，实现互动交流。
- **响应式设计**：适应不同设备屏幕尺寸，确保在桌面端和移动端都有良好的阅读体验。

### 技术栈与工具
- **后端框架**：SpringBoot2.7+SpringSecurity+MyBatis-Plus
- **数据库**：MySQL8+Redis（存储个人信息、博文浏览次数并定时更新数据）
- **前端框架/库**：Vue2+ElementUI

### 快速开始
#### 安装依赖
```bash
git clone https://github.com/yourusername/personal-blog.git
cd blog    #前台展示博文
cd admin   #后台博文管理
npm install
```
#### 运行开发环境
```bash
npm run dev 或 yarn dev
```

#### 数据库
- 导入SQL脚本
- 修改后端**blog**和**admin**模块的"`application.yml`中的数据库连接信息
```yaml
spring:
  # 数据库连接信息
  datasource:
    url: jdbc:mysql://localhost:3306/sg_blog?characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: 112233
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### 生产环境部署
请参阅`DEPLOYMENT.md`文档了解如何将项目部署到生产环境（如Heroku、AWS或VPS等）。

### 贡献指南
欢迎任何形式的贡献，包括但不限于代码提交、问题反馈以及功能建议。请遵循项目的`CONTRIBUTING.md`文件中的指导原则。

### 许可证
本项目采用MIT许可证开源，详细信息见`LICENSE`文件。

再次感谢您对本项目的支持和关注！如有任何疑问，请随时在GitHub上创建Issue进行讨论。