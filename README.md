# 管理系统作业

本项目是对原 Java Swing 版学生管理系统的全栈重构。

## 技术栈
- **后端**: Java 8 + Spring Boot 2.7 + MyBatis Plus + MySQL
- **前端**: Vue 3 (Composition API) + Vite + Element Plus + Tailwind CSS

## 目录结构
- `/server`: Spring Boot 后端源码
- `/web`: Vue 3 前端源码
- `db_init.sql`: 数据库初始化脚本

## 快速开始
1. **数据库**: 执行 `db_init.sql`。
2. **后端**: 
   - 修改 `server/src/main/resources/application.yml` 中的数据库配置。
   - 运行 `mvn spring-boot:run`。
3. **前端**:
   - `cd web`
   - `npm install`
   - `npm run dev`

## 现代特性
- [x] 卡片式、毛玻璃 UI 设计
- [x] 响应式布局，适配多种屏幕
- [x] 动画交互反馈 (Blob 动画、Hover 缩放)
- [x] 逻辑删除与数据审计
- [x] Axios 统一封装与错误处理
