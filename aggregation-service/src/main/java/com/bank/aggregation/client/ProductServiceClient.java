package com.bank.aggregation.client;

import com.bank.common.model.Product;
import com.bank.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bank.aggregation.config.FeignConfig;

import java.util.List;

/**
 * 产品服务Feign客户端
 * 用于调用产品服务提供的API接口
 */
@FeignClient(name = "product-service", path = "/api/products", configuration = FeignConfig.class)
public interface ProductServiceClient {
    
    /**
     * 获取推荐产品列表
     * @param userId 用户ID
     * @return 推荐产品列表
     */
    @GetMapping("/recommended")
    ApiResponse<List<Product>> getRecommendedProducts(@RequestParam("userId") Long userId);
}