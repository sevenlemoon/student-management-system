package com.example.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.student.entity.User;
import com.example.student.mapper.UserMapper;
import com.example.student.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 实现具体的登录验证逻辑
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 实现登录功能
     * 使用 MyBatis Plus 的 LambdaQueryWrapper 构建查询条件
     */
    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 匹配用户名和密码
        wrapper.eq(User::getUsername, username).eq(User::getPassword, password);
        return this.getOne(wrapper);
    }
}
