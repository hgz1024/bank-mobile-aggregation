package com.bank.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务启动类
 * 负责用户信息管理相关功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    /**
     * 主函数，启动用户服务应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}