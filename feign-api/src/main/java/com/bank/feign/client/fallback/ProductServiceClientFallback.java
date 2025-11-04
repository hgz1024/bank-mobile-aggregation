package com.bank.feign.client.fallback;

import com.bank.common.model.Product;
import com.bank.common.response.ApiResponse;
import com.bank.feign.client.ProductServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品服务Feign客户端降级处理类
 * 当产品服务不可用时，提供降级处理逻辑
 */
@Slf4j
@Component
public class ProductServiceClientFallback implements ProductServiceClient {
    
    @Override
    public ApiResponse<List<Product>> getRecommendedProducts(Long userId) {
        log.error("产品服务调用失败，执行降级逻辑，用户ID: {}", userId);
        // 返回空的产品列表而不是null，避免NPE
        return ApiResponse.success(new ArrayList<>());
    }
}