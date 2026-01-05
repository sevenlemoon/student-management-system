package com.example.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.student.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生 Mapper 接口
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
