package com.bank.aggregation.config;

import feign.Contract;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

/**
 * Feign客户端配置类
 * 用于优化Feign客户端的性能和行为
 */
@Configuration
public class FeignConfig {
    
    /**
     * 配置请求选项，设置连接超时和读取超时
     * @return 请求选项
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 10000); // 连接超时5秒，读取超时10秒
    }
    
    /**
     * 配置重试策略
     * @return 重试器
     */
    @Bean
    public Retryer feignRetryer() {
        // 自定义重试策略
        return new Retryer.Default(100, 1000, 3); // 初始间隔100ms，最大间隔1000ms，最多重试3次
    }
    
    /**
     * 配置Feign日志级别
     * @return 日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
    
    /**
     * 配置契约，使用Spring MVC契约
     * @return Spring MVC契约
     */
    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }
}