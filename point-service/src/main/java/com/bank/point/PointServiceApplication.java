package com.bank.point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 积分服务启动类
 * 负责用户积分管理相关功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PointServiceApplication {
    /**
     * 主函数，启动积分服务应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(PointServiceApplication.class, args);
    }
}