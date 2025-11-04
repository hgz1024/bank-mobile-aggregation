package com.bank.common.exception;

import lombok.Data;
import java.util.List;

/**
 * 参数校验错误响应实体类
 * 用于返回参数校验失败的详细信息
 */
@Data
public class ValidationErrorResponse {
    /**
     * 错误码
     */
    private String code = "400";
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 详细错误信息列表
     */
    private List<String> details;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 无参构造函数
     */
    public ValidationErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 有参构造函数
     * @param message 错误消息
     * @param details 详细错误信息
     */
    public ValidationErrorResponse(String message, List<String> details) {
        this();
        this.message = message;
        this.details = details;
    }
}