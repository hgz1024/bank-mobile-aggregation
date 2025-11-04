package com.bank.common.model;

import lombok.Data;
import java.util.List;

/**
 * 首页数据聚合实体类
 * 用于封装移动端首页展示的所有数据信息
 */
@Data
public class HomePageData {
    /**
     * 用户信息
     */
    private User user;
    
    /**
     * 用户积分信息
     */
    private Points points;
    
    /**
     * 推荐产品列表
     */
    private List<Product> recommendedProducts;
    
    /**
     * 展示消息
     */
    private String displayMessage;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 默认构造函数
     * 初始化时间戳为当前系统时间
     */
    public HomePageData() {
        this.timestamp = System.currentTimeMillis();
    }
}