package com.bank.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API网关启动类
 * 作为统一入口，负责请求路由和转发
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    /**
     * 主函数，启动API网关应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}