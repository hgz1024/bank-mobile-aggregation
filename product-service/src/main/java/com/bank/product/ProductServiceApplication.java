package com.bank.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 产品服务启动类
 * 负责银行产品管理相关功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {
    /**
     * 主函数，启动产品服务应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}