package com.bank.feign.client;

import com.bank.common.model.User;
import com.bank.common.response.ApiResponse;
import com.bank.feign.client.fallback.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.bank.feign.config.FeignConfig;

/**
 * 用户服务Feign客户端
 * 用于调用用户服务提供的API接口
 */
@FeignClient(name = "user-service", path = "/api/users", configuration = FeignConfig.class, fallback = UserServiceClientFallback.class)
public interface UserServiceClient {
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    ApiResponse<User> getUserById(@PathVariable("userId") Long userId);
}