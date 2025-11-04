package com.bank.common.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品信息实体类
 * 包含银行产品的基本信息、类型、风险等级等属性
 */
@Data
public class Product {
    /**
     * 产品ID
     */
    private Long id;
    
    /**
     * 产品编码
     */
    private String productCode;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品类型：FUND-基金，INSURANCE-保险，LOAN-贷款
     */
    private String productType;
    
    /**
     * 预期收益率
     */
    private BigDecimal expectedRate;
    
    /**
     * 起购金额
     */
    private BigDecimal minAmount;
    
    /**
     * 风险等级：1-低风险，2-中风险，3-高风险
     */
    private Integer riskLevel;
    
    /**
     * 状态：1-在售，2-售罄，3-下架
     */
    private Integer status;
    
    /**
     * 产品描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}