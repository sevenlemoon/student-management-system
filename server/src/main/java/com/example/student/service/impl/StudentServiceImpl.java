package com.example.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.student.entity.Student;
import com.example.student.mapper.StudentMapper;
import com.example.student.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * 学生服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，实现 StudentService 接口
 * 封装了对学生表的具体业务操作逻辑
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
