package com.bank.aggregation.controller;

import com.bank.aggregation.client.PointServiceClient;
import com.bank.aggregation.client.ProductServiceClient;
import com.bank.aggregation.client.UserServiceClient;
import com.bank.common.model.HomePageData;
import com.bank.common.model.Points;
import com.bank.common.model.Product;
import com.bank.common.model.User;
import com.bank.common.request.UserQueryRequest;
import com.bank.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聚合服务控制器
 * 负责聚合用户、积分、产品等信息，提供统一的首页数据接口
 */
@Slf4j
@RestController
@RequestMapping("/api/homepage")
@RequiredArgsConstructor
@Validated
public class AggregationController {
    
    private final UserServiceClient userServiceClient;
    private final PointServiceClient pointServiceClient;
    private final ProductServiceClient productServiceClient;
    
    // 使用固定线程池进行并行调用
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    
    /**
     * 获取首页聚合数据
     * 并行调用用户、积分、产品服务，聚合返回首页所需数据
     * @param userId 用户ID
     * @return 首页聚合数据
     */
    @GetMapping("/{userId}")
    public ApiResponse<HomePageData> getHomePageData(
            @PathVariable @Min(value = 1, message = "用户ID必须大于0") Long userId) {
        log.info("开始聚合首页数据，用户ID: {}", userId);
        long startTime = System.currentTimeMillis();
        
        HomePageData homePageData = new HomePageData();
        
        try {
            // 并行调用三个服务
            CompletableFuture<Void> userFuture = CompletableFuture.runAsync(() -> {
                try {
                    ApiResponse<User> userResponse = userServiceClient.getUserById(userId);
                    if (userResponse != null && userResponse.getCode() == 0) {
                        homePageData.setUser(userResponse.getData());
                        log.debug("用户信息获取成功");
                    } else {
                        log.error("获取用户信息失败: {}", userResponse != null ? userResponse.getMessage() : "响应为空");
                        homePageData.setDisplayMessage("部分数据加载失败");
                    }
                } catch (Exception e) {
                    log.error("获取用户信息失败: {}", e.getMessage());
                    homePageData.setDisplayMessage("部分数据加载失败");
                }
            }, executorService);
            
            CompletableFuture<Void> pointsFuture = CompletableFuture.runAsync(() -> {
                try {
                    ApiResponse<Points> pointsResponse = pointServiceClient.getUserPoints(userId);
                    if (pointsResponse != null && pointsResponse.getCode() == 0) {
                        homePageData.setPoints(pointsResponse.getData());
                        log.debug("积分信息获取成功");
                    } else {
                        log.error("获取积分信息失败: {}", pointsResponse != null ? pointsResponse.getMessage() : "响应为空");
                        homePageData.setDisplayMessage("部分数据加载失败");
                    }
                } catch (Exception e) {
                    log.error("获取积分信息失败: {}", e.getMessage());
                    homePageData.setDisplayMessage("部分数据加载失败");
                }
            }, executorService);
            
            CompletableFuture<Void> productsFuture = CompletableFuture.runAsync(() -> {
                try {
                    ApiResponse<List<Product>> productsResponse = productServiceClient.getRecommendedProducts(userId);
                    if (productsResponse != null && productsResponse.getCode() == 0) {
                        homePageData.setRecommendedProducts(productsResponse.getData());
                        log.debug("推荐产品获取成功");
                    } else {
                        log.error("获取推荐产品失败: {}", productsResponse != null ? productsResponse.getMessage() : "响应为空");
                        homePageData.setDisplayMessage("部分数据加载失败");
                    }
                } catch (Exception e) {
                    log.error("获取推荐产品失败: {}", e.getMessage());
                    homePageData.setDisplayMessage("部分数据加载失败");
                }
            }, executorService);
            
            // 等待所有任务完成
            CompletableFuture.allOf(userFuture, pointsFuture, productsFuture).join();
            
            long endTime = System.currentTimeMillis();
            log.info("首页数据聚合完成，用户ID: {}, 耗时: {}ms", userId, endTime - startTime);
            
            return ApiResponse.success(homePageData);
            
        } catch (Exception e) {
            log.error("首页数据聚合异常: {}", e.getMessage(), e);
            homePageData.setDisplayMessage("数据加载失败，请稍后重试");
            return ApiResponse.success(homePageData);
        }
    }
    
    /**
     * 健康检查接口
     * @return 服务状态信息
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("Aggregation Service is UP");
    }
}