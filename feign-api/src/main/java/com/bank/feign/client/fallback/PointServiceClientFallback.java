package com.bank.feign.client.fallback;

import com.bank.common.model.Points;
import com.bank.common.response.ApiResponse;
import com.bank.feign.client.PointServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 积分服务Feign客户端降级处理类
 * 当积分服务不可用时，提供降级处理逻辑
 */
@Slf4j
@Component
public class PointServiceClientFallback implements PointServiceClient {
    
    @Override
    public ApiResponse<Points> getUserPoints(Long userId) {
        log.error("积分服务调用失败，执行降级逻辑，用户ID: {}", userId);
        Points defaultPoints = new Points();
        defaultPoints.setUserId(userId);
        defaultPoints.setTotalPoints(BigDecimal.ZERO);
        defaultPoints.setAvailablePoints(BigDecimal.ZERO);
        defaultPoints.setFrozenPoints(BigDecimal.ZERO);
        defaultPoints.setPointsLevel(1);
        return ApiResponse.success(defaultPoints);
    }
}