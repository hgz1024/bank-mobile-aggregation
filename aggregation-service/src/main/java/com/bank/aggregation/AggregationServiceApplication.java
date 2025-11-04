package com.bank.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 聚合服务启动类
 * 负责聚合多个基础服务的数据，提供统一的数据接口
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bank.feign.client")
public class AggregationServiceApplication {
    /**
     * 主函数，启动聚合服务应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AggregationServiceApplication.class, args);
    }
}