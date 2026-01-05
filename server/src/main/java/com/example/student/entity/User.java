package com.example.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的 user 表，用于管理系统登录用户
 */
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;            // 用户唯一标识 ID
    private String username;    // 登录用户名
    private String password;    // 登录密码（存储或传输时需注意安全）
    private String phone;       // 联系电话
    private String sex;         // 性别
    private String avatar;      // 头像链接或路径

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 账号创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 账号信息最后更新时间

    @TableLogic
    private Integer isDeleted; // 逻辑删除标志

    // 手动添加 Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
