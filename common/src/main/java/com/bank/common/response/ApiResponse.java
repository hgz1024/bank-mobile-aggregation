package com.bank.common.response;

import lombok.Data;

/**
 * API统一响应实体类
 * 用于统一返回API响应的格式
 * @param <T> 响应数据类型
 */
@Data
public class ApiResponse<T> {
    /**
     * 响应码 0表示成功，非0表示失败
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 无参构造函数
     */
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应（无数据）
     * @param <T> 响应数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 0;
        response.message = "success";
        return response;
    }
    
    /**
     * 成功响应（有数据）
     * @param data 响应数据
     * @param <T> 响应数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 0;
        response.message = "success";
        response.data = data;
        return response;
    }
    
    /**
     * 失败响应
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 响应数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = code;
        response.message = message;
        return response;
    }
}