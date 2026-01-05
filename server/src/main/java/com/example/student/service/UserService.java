package com.example.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.student.entity.User;

/**
 * 用户服务接口
 * 提供用户登录、注册等业务逻辑接口
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录逻辑
     * @param username 用户名
     * @param password 密码
     * @return 匹配的用户对象，若不存在则返回 null
     */
    User login(String username, String password);
}
