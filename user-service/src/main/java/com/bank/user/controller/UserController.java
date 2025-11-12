package com.bank.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.bank.common.model.User;
import com.bank.common.response.ApiResponse;
import com.bank.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务控制器
 * 提供用户信息相关的API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RefreshScope
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @Value("${feature.user-service.new-feature-enabled:false}")
    private boolean newFeatureEnabled;

    @GetMapping("/basic")
    public ApiResponse<String> basicService() {
        return ApiResponse.success("基础服务已访问");
    }

    @GetMapping("/premium")
    public ApiResponse<String> premiumService() {
        return ApiResponse.success("高级服务已访问");
    }

    // 添加一个用于测试的接口
    @GetMapping("/test")
    public ApiResponse<String> testEndpoint() {
        return ApiResponse.success("测试接口调用成功");
    }

    @GetMapping("/feature-status")
    public Map<String, Object> getFeatureStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("newFeatureEnabled", newFeatureEnabled);
        result.put("message", newFeatureEnabled ? "新功能已启用" : "新功能已禁用");
        return result;
    }
    
    /**
     * 根据用户ID获取用户信息（用于聚合服务调用）
     * 这是主要的用户查询入口，具有较高优先级
     * @param userId 用户ID
     * @return 用户信息
     */
    @SentinelResource("hotParam")
    @GetMapping("/{userId}")
    public ApiResponse<User> getUserById(@PathVariable Long userId) {
        /*log.info("获取用户信息，用户ID: {}", userId);
        if (userId == 1){
            throw new RuntimeException("用户不存在");
        }*/
        User user = userService.getUserById(userId);
        return ApiResponse.success(user);
    }
    
    /**
     * 根据用户ID获取用户信息（用于后台管理查询）
     * 这是后台管理的用户查询入口，具有较低优先级
     * 用于演示Sentinel链路流控模式
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/admin/{userId}")
    public ApiResponse<User> getUserByIdForAdmin(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ApiResponse.success(user);
    }

    /**
     * 健康检查接口
     * @param request HTTP请求对象
     * @return 服务状态信息
     */
    @GetMapping("/health")
    public ApiResponse<String> health(HttpServletRequest request) {
        return ApiResponse.success("User Service is UP on port: " + request.getServerPort());
    }
}