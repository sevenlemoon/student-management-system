package com.example.student.controller;

import com.example.student.common.Result;
import com.example.student.entity.User;
import com.example.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理控制器
 * 负责处理用户的登录和注册请求
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * @param loginData 包含 username 和 password 的 Map
     * @return 登录成功返回用户信息（不含密码），失败返回错误提示
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        User user = userService.login(username, password);
        if (user != null) {
            user.setPassword(null); // 安全处理：不将密码返回给前端
            return Result.success(user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册接口
     * @param user 包含用户名和密码的用户实体对象
     * @return 注册成功状态
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }
}
