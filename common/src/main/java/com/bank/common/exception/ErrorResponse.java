package com.bank.common.exception;

import lombok.Data;

/**
 * 错误响应实体类
 * 用于统一返回错误信息的格式
 */
@Data
public class ErrorResponse {
    /**
     * 错误码
     */
    private String code;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 无参构造函数
     */
    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 有参构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public ErrorResponse(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
}