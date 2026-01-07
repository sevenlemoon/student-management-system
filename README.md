# SpringBoot+Vue3 完成学生信息管理系统

## 一、技术选型
本项目采用了当前最流行的前后端分离架构，相比传统的 JSP 模式，具有更好的用户体验和开发效率。
*   **后端**: SpringBoot + MyBatis Plus + MySQL (负责数据处理与业务逻辑)
*   **前端**: Vue 3 + Vite + Element Plus + Tailwind CSS (负责页面展示与交互)
*   **连接方式**: 通过 RESTful API 接口进行数据交换

---

## 二、环境搭建

### 1. 快速创建 SpringBoot 项目
使用 Maven 进行项目管理，主要依赖包括 `spring-boot-starter-web`、`mybatis-plus-boot-starter` 和 `mysql-connector-java`。

### 2. 导入 Maven 依赖
在 [pom.xml](file:///Users/apple/Desktop/gaoxingzhan_副本/server/pom.xml) 中配置如下核心依赖：
```xml
<dependencies>
    <!-- Web 核心 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- MyBatis Plus 简化数据库操作 -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.2</version>
    </dependency>
    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.31</version>
    </dependency>
</dependencies>
```

### 3. 创建数据库并插入相关测试数据
在 MySQL 中执行 [db_init.sql](file:///Users/apple/Desktop/gaoxingzhan_副本/db_init.sql)，创建学生表：
```sql
CREATE TABLE `student` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `sno` VARCHAR(50) NOT NULL UNIQUE COMMENT '学号',
  `name` VARCHAR(100) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(10) COMMENT '性别',
  `age` INT COMMENT '年龄',
  `major` VARCHAR(100) COMMENT '专业',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## 三、代码编写

### 1. 全局配置文件编写
在 [application.yml](file:///Users/apple/Desktop/gaoxingzhan_副本/server/src/main/resources/application.yml) 中配置数据库连接和端口：
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_management
    username: root
    password: yourpassword
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 2. 全查询与分页功能实现

#### (1) 封装实体类
在 [Student.java](file:///Users/apple/Desktop/gaoxingzhan_副本/server/src/main/java/com/example/student/entity/Student.java) 中定义学生信息：
```java
public class Student {
    private Long id;
    private String sno;
    private String name;
    private String gender;
    private Integer age;
    private String major;
}
```

#### (2) 书写 DAO 接口 (Mapper)
在 [StudentMapper.java](file:///Users/apple/Desktop/gaoxingzhan_副本/server/src/main/java/com/example/student/mapper/StudentMapper.java) 中继承 BaseMapper：
```java
@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    // MyBatis Plus 会自动实现基础增删改查
}
```

#### (3) Service 接口与实现类
在 [StudentService.java](file:///Users/apple/Desktop/gaoxingzhan_副本/server/src/main/java/com/example/student/service/StudentService.java) 及其 [实现类](file:///Users/apple/Desktop/gaoxingzhan_副本/server/src/main/java/com/example/student/service/impl/StudentServiceImpl.java) 中编写逻辑：
```java
// 实现类片段
@Override
public IPage<Student> getStudentPage(Page<Student> page, String keyword) {
    LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.hasText(keyword)) {
        wrapper.like(Student::getName, keyword).or().like(Student::getSno, keyword);
    }
    return this.page(page, wrapper);
}
```

#### (4) Controller 接口
在 [StudentController.java](file:///Users/apple/Desktop/gaoxingzhan_副本/server/src/main/java/com/example/student/controller/StudentController.java) 中暴露接口：
```java
@GetMapping("/list")
public Result<IPage<Student>> list(@RequestParam(defaultValue = "1") Integer current,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   String keyword) {
    Page<Student> page = new Page<>(current, size);
    return Result.success(studentService.getStudentPage(page, keyword));
}
```

#### (5) Vue 页面显示 (代替 JSP)
在 [StudentManage.vue](file:///Users/apple/Desktop/gaoxingzhan_副本/web/src/views/StudentManage.vue) 中使用 Element Plus 表格展示数据：
```html
<el-table :data="studentList" style="width: 100%">
  <el-table-column prop="sno" label="学号" />
  <el-table-column prop="name" label="姓名" />
  <el-table-column prop="major" label="专业" />
  <el-table-column label="操作">
    <template #default="scope">
      <el-button @click="handleEdit(scope.row)">编辑</el-button>
      <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
    </template>
  </el-table-column>
</el-table>
```

### 3. 根据 ID 删除功能实现

#### (1) 后端 Controller 实现
```java
@DeleteMapping("/{id}")
public Result<Void> delete(@PathVariable Long id) {
    studentService.removeById(id);
    return Result.success();
}
```

#### (2) 前端交互实现
```javascript
const handleDelete = async (id) => {
    await ElMessageBox.confirm('确定要删除该学生信息吗？');
    await deleteStudent(id);
    ElMessage.success('删除成功');
    fetchData(); // 刷新列表
};
```

---

## 四、项目亮点：备份与恢复 (JSON 导入导出)
本项目额外实现了 PDF 中没有的高级功能：
1.  **数据导出**: 将学生信息一键导出为 JSON 文件保存在本地。
2.  **数据恢复**: 通过上传 JSON 文件，后端自动识别“学号”进行“存在即更新，不存在即插入”的智能恢复逻辑。

---

## 五、测试
1.  启动 MySQL 数据库。
2.  运行 SpringBoot 后端主类。
3.  运行前端 `npm run dev`。
4.  在浏览器中访问页面，测试增删改查及搜索功能，确保一切正常。
