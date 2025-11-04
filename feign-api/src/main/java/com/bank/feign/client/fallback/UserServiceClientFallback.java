package com.bank.feign.client.fallback;

import com.bank.common.model.User;
import com.bank.common.response.ApiResponse;
import com.bank.feign.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户服务Feign客户端降级处理类
 * 当用户服务不可用时，提供降级处理逻辑
 */
@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {
    
    @Override
    public ApiResponse<User> getUserById(Long userId) {
        log.error("用户服务调用失败，执行降级逻辑，用户ID: {}", userId);
        User defaultUser = new User();
        defaultUser.setId(userId);
        defaultUser.setUsername("默认用户");
        defaultUser.setName("用户信息获取失败");
        defaultUser.setPhone("未知");
        defaultUser.setEmail("未知");
        defaultUser.setUserLevel(1);
        defaultUser.setStatus(1);
        return ApiResponse.success(defaultUser);
    }
}