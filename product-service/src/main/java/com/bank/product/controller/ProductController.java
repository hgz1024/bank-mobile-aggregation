package com.bank.product.controller;

import com.bank.common.model.Product;
import com.bank.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品服务控制器
 * 提供银行产品信息相关的API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    // 模拟产品数据
    private final List<Product> productDatabase = new ArrayList<>();
    
    /**
     * 构造函数
     * 初始化测试产品数据
     */
    public ProductController() {
        // 初始化测试数据
        Product product1 = new Product();
        product1.setId(1L);
        product1.setProductCode("FUND001");
        product1.setProductName("稳健增长基金");
        product1.setProductType("FUND");
        product1.setExpectedRate(new BigDecimal("0.0385"));
        product1.setMinAmount(new BigDecimal("1000.00"));
        product1.setRiskLevel(2);
        product1.setStatus(1);
        product1.setDescription("中低风险，适合稳健型投资者");
        product1.setCreateTime(LocalDateTime.now().minusDays(30));
        
        Product product2 = new Product();
        product2.setId(2L);
        product2.setProductCode("INS001");
        product2.setProductName("终身寿险");
        product2.setProductType("INSURANCE");
        product2.setExpectedRate(new BigDecimal("0.0250"));
        product2.setMinAmount(new BigDecimal("5000.00"));
        product2.setRiskLevel(1);
        product2.setStatus(1);
        product2.setDescription("保障终身，兼顾理财功能");
        product2.setCreateTime(LocalDateTime.now().minusDays(15));
        
        Product product3 = new Product();
        product3.setId(3L);
        product3.setProductCode("LOAN001");
        product3.setProductName("个人消费贷款");
        product3.setProductType("LOAN");
        product3.setExpectedRate(new BigDecimal("0.0650"));
        product3.setMinAmount(new BigDecimal("10000.00"));
        product3.setRiskLevel(3);
        product3.setStatus(1);
        product3.setDescription("快速审批，额度灵活");
        product3.setCreateTime(LocalDateTime.now().minusDays(7));
        
        productDatabase.add(product1);
        productDatabase.add(product2);
        productDatabase.add(product3);
    }
    
    /**
     * 获取推荐产品列表
     * @param userId 用户ID（可选）
     * @return 推荐产品列表
     */
    @GetMapping("/recommended")
    public ApiResponse<List<Product>> getRecommendedProducts(@RequestParam(required = false) Long userId) {
        log.info("获取推荐产品列表，用户ID: {}", userId);
        
        // 模拟基于用户等级的推荐逻辑
        List<Product> recommended = new ArrayList<>();
        
        // 默认返回所有在售产品
        for (Product product : productDatabase) {
            if (product.getStatus() == 1) { // 在售状态
                recommended.add(product);
            }
        }
        
        // 模拟处理时间
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return ApiResponse.success(recommended);
    }
    
    /**
     * 根据产品ID获取产品详情
     * @param productId 产品ID
     * @return 产品详情
     * @throws RuntimeException 当产品不存在时抛出异常
     */
    @GetMapping("/{productId}")
    public ApiResponse<Product> getProductById(@PathVariable Long productId) {
        log.info("查询产品详情，产品ID: {}", productId);
        
        Product product = productDatabase.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("产品不存在: " + productId));
        
        return ApiResponse.success(product);
    }
    
    /**
     * 健康检查接口
     * @return 服务状态信息
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("Product Service is UP");
    }
}