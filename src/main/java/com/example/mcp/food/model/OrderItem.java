package com.example.mcp.food.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单项模型类 - 表示订单中的一道菜品及其数量
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    /**
     * 菜品 ID - 关联到 MenuItem
     */
    private String menuItemId;

    /**
     * 菜品名称 - 冗余存储，方便查询
     */
    private String menuItemName;

    /**
     * 菜品单价
     */
    private double price;

    /**
     * 订购数量
     */
    private int quantity;

    /**
     * 小计金额（price * quantity）
     */
    private double subtotal;

}
