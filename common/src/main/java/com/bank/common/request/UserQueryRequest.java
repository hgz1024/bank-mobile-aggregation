package com.bank.common.request;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 用户查询请求对象
 * 用于封装用户查询请求参数
 */
@Data
public class UserQueryRequest {
    /**
     * 用户ID，不能为空且必须大于0
     */
    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;
}