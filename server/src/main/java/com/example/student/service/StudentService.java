package com.example.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.student.entity.Student;

/**
 * 学生服务接口
 * 继承 MyBatis Plus 的 IService 接口，提供基础的 CRUD 功能
 */
public interface StudentService extends IService<Student> {
}
