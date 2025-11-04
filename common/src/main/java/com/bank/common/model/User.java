package com.bank.common.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户信息实体类
 * 包含用户的基本信息、等级和状态等属性
 */
@Data
public class User {
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户姓名
     */
    private String name;
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 用户等级：1-普通，2-白银，3-黄金，4-钻石
     */
    private Integer userLevel;
    
    /**
     * 注册时间
     */
    private LocalDateTime registerTime;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 状态：1-正常，2-冻结
     */
    private Integer status;
}