package com.bank.feign.client;

import com.bank.common.model.Points;
import com.bank.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.bank.feign.config.FeignConfig;

/**
 * 积分服务Feign客户端
 * 用于调用积分服务提供的API接口
 */
@FeignClient(name = "point-service", path = "/api/points", configuration = FeignConfig.class)
public interface PointServiceClient {
    
    /**
     * 根据用户ID获取用户积分信息
     * @param userId 用户ID
     * @return 用户积分信息
     */
    @GetMapping("/user/{userId}")
    ApiResponse<Points> getUserPoints(@PathVariable("userId") Long userId);
}