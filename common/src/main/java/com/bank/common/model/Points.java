package com.bank.common.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户积分信息实体类
 * 包含用户的总积分、可用积分、冻结积分等信息
 */
@Data
public class Points {
    /**
     * 积分记录ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 总积分
     */
    private BigDecimal totalPoints;
    
    /**
     * 可用积分
     */
    private BigDecimal availablePoints;
    
    /**
     * 冻结积分
     */
    private BigDecimal frozenPoints;
    
    /**
     * 积分等级
     */
    private Integer pointsLevel;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}