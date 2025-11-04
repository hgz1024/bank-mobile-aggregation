package com.bank.user.controller;

import com.bank.common.model.User;
import com.bank.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
public class UserController {
    
    // 模拟用户数据
    private final Map<Long, User> userDatabase = new HashMap<>();

    @Value("${feature.user-service.new-feature-enabled:false}")
    private boolean newFeatureEnabled;

    @GetMapping("/feature-status")
    public Map<String, Object> getFeatureStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("newFeatureEnabled", newFeatureEnabled);
        result.put("message", newFeatureEnabled ? "新功能已启用" : "新功能已禁用");
        return result;
    }
    
    /**
     * 构造函数
     * 初始化测试用户数据
     */
    public UserController() {
        // 初始化测试数据
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("zhangsan");
        user1.setName("张三");
        user1.setPhone("13800138001");
        user1.setEmail("zhangsan@example.com");
        user1.setUserLevel(2); // 白银用户
        user1.setRegisterTime(LocalDateTime.now().minusDays(180));
        user1.setLastLoginTime(LocalDateTime.now().minusHours(2));
        user1.setStatus(1);
        userDatabase.put(1L, user1);
        
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("lisi");
        user2.setName("李四");
        user2.setPhone("13800138002");
        user2.setEmail("lisi@example.com");
        user2.setUserLevel(1); // 普通用户
        user2.setRegisterTime(LocalDateTime.now().minusDays(90));
        user2.setLastLoginTime(LocalDateTime.now().minusDays(1));
        user2.setStatus(1);
        userDatabase.put(2L, user2);
    }
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     * @throws RuntimeException 当用户不存在时抛出异常
     */
    @GetMapping("/{userId}")
    public ApiResponse<User> getUserById(@PathVariable Long userId) {
        log.info("查询用户信息，用户ID: {}", userId);
        //让当前程序休眠3秒
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        User user = userDatabase.get(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在: " + userId);
        }
        
        // 模拟处理时间
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
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