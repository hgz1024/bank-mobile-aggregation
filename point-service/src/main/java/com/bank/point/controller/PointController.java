package com.bank.point.controller;

import com.bank.common.model.Points;
import com.bank.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 积分服务控制器
 * 提供用户积分信息相关的API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/points")
public class PointController {
    
    // 模拟积分数据
    private final Map<Long, Points> pointsDatabase = new HashMap<>();
    
    /**
     * 构造函数
     * 初始化测试积分数据
     */
    public PointController() {
        // 初始化测试数据
        Points points1 = new Points();
        points1.setId(1L);
        points1.setUserId(1L);
        points1.setTotalPoints(new BigDecimal("15800.50"));
        points1.setAvailablePoints(new BigDecimal("12500.00"));
        points1.setFrozenPoints(new BigDecimal("3300.50"));
        points1.setPointsLevel(3);
        points1.setUpdateTime(LocalDateTime.now().minusHours(1));
        pointsDatabase.put(1L, points1);
        
        Points points2 = new Points();
        points2.setId(2L);
        points2.setUserId(2L);
        points2.setTotalPoints(new BigDecimal("5800.00"));
        points2.setAvailablePoints(new BigDecimal("5800.00"));
        points2.setFrozenPoints(BigDecimal.ZERO);
        points2.setPointsLevel(2);
        points2.setUpdateTime(LocalDateTime.now().minusDays(1));
        pointsDatabase.put(2L, points2);
    }
    
    /**
     * 根据用户ID获取用户积分信息
     * @param userId 用户ID
     * @return 用户积分信息
     * @throws RuntimeException 当用户积分信息不存在时抛出异常
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<Points> getUserPoints(@PathVariable Long userId) {
        log.info("查询用户积分，用户ID: {}", userId);
        
        Points points = pointsDatabase.get(userId);
        if (points == null) {
            throw new RuntimeException("用户积分信息不存在: " + userId);
        }
        
        // 模拟处理时间
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return ApiResponse.success(points);
    }
    
    /**
     * 健康检查接口
     * @return 服务状态信息
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("Point Service is UP");
    }
}