package com.example.mcp.food.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单模型类 - 表示一个完整的订餐订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * 订单唯一标识 ID
     */
    private String id;

    /**
     * 用户 ID - 标识下单的用户
     */
    private String userId;

    /**
     * 订单项列表 - 订单中包含的所有菜品及数量
     */
    private List<OrderItem> items;

    /**
     * 订单总金额
     */
    private double totalAmount;

    /**
     * 订单状态 - PENDING(待处理)/CONFIRMED(已确认)/PREPARING(制作中)/DELIVERING(配送中)/COMPLETED(已完成)/CANCELLED(已取消)
     */
    private OrderStatus status;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING,      // 待处理
        CONFIRMED,    // 已确认
        PREPARING,    // 制作中
        DELIVERING,   // 配送中
        COMPLETED,    // 已完成
        CANCELLED     // 已取消
    }
}
