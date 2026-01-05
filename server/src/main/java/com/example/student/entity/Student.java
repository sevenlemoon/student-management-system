package com.example.student.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类
 * 对应数据库中的 student 表
 */
@TableName("student")
public class Student {
    
    @TableId(type = IdType.AUTO)
    private Long id;            // 主键ID，自动递增
    
    private String sno;         // 学号
    private String sname;       // 姓名
    private String ssex;        // 性别
    private BigDecimal score;   // 成绩
    private LocalDate sbirthday; // 生日
    private String shouji;      // 手机号

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间，插入时自动填充

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间，插入和更新时自动填充

    @TableLogic
    private Integer isDeleted; // 逻辑删除标志（0: 未删除, 1: 已删除）

    // 手动添加 Getter 和 Setter，由于环境限制未使用 Lombok
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSno() { return sno; }
    public void setSno(String sno) { this.sno = sno; }
    public String getSname() { return sname; }
    public void setSname(String sname) { this.sname = sname; }
    public String getSsex() { return ssex; }
    public void setSsex(String ssex) { this.ssex = ssex; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public LocalDate getSbirthday() { return sbirthday; }
    public void setSbirthday(LocalDate sbirthday) { this.sbirthday = sbirthday; }
    public String getShouji() { return shouji; }
    public void setShouji(String shouji) { this.shouji = shouji; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
