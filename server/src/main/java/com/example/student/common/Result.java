package com.example.student.common;

/**
 * 统一响应结果类
 * 用于规范后端 API 返回给前端的数据格式
 * @param <T> 数据负载的类型
 */
public class Result<T> {
    private Integer code;    // 状态码 (如 200 表示成功，500 表示失败)
    private String message; // 提示消息
    private T data;         // 实际返回的数据负载

    /**
     * 快捷返回成功结果（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 快捷返回成功结果（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 快捷返回错误结果（默认状态码 500）
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 快捷返回错误结果（自定义状态码）
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 手动添加 Getter 和 Setter
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
