package com.example.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.student.common.Result;
import com.example.student.entity.Student;
import com.example.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生管理控制器
 * 负责处理与学生信息相关的 HTTP 请求
 */
@RestController
@RequestMapping("/api/student")
@CrossOrigin // 允许跨域
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页查询学生列表
     * @param current 当前页码
     * @param size 每页显示的记录数
     * @param keyword 搜索关键词（学号或姓名）
     * @return 包含学生列表的分页结果
     */
    @GetMapping("/list")
    public Result<Page<Student>> list(@RequestParam(defaultValue = "1") Integer current,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String keyword) {
        Page<Student> page = new Page<>(current, size);
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        // 如果有关键词，则按姓名或学号模糊查询
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Student::getSname, keyword).or().like(Student::getSno, keyword);
        }
        // 默认按创建时间倒序排列
        wrapper.orderByDesc(Student::getCreateTime);
        return Result.success(studentService.page(page, wrapper));
    }

    /**
     * 新增学生
     * @param student 学生实体对象
     * @return 成功状态
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Student student) {
        studentService.save(student);
        return Result.success();
    }

    /**
     * 更新学生信息
     * @param student 包含 ID 的学生实体对象
     * @return 成功状态
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    /**
     * 删除学生（逻辑删除）
     * @param id 学生 ID
     * @return 成功状态
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        studentService.removeById(id);
        return Result.success();
    }

    /**
     * 获取所有学生数据
     * 用于前端备份导出功能，返回系统内所有未被逻辑删除的学生
     * @return 学生列表
     */
    @GetMapping("/export")
    public Result<List<Student>> exportData() {
        return Result.success(studentService.list());
    }

    /**
     * 批量导入学生数据
     * 用于前端数据恢复功能，接收 JSON 格式的学生列表并保存
     * @param studentList 待导入的学生列表
     * @return 成功状态
     */
    @PostMapping("/import")
    public Result<Void> importData(@RequestBody List<Student> studentList) {
        if (studentList != null && !studentList.isEmpty()) {
            // 清空原有 ID 和时间戳，确保是作为全新数据插入，由数据库或 ORM 自动生成新信息
            for (Student s : studentList) {
                s.setId(null);
                s.setCreateTime(null);
                s.setUpdateTime(null);
            }
            studentService.saveBatch(studentList);
        }
        return Result.success();
    }
}
