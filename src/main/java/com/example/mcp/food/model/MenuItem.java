package com.example.mcp.food.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单项模型类 - 表示菜单中的一道菜品
 *
 * 知识点：使用 Lombok 注解简化代码
 * - @Data: 自动生成 getter、setter、toString、equals、hashCode 方法
 * - @Builder: 支持建造者模式创建对象
 * - @NoArgsConstructor: 生成无参构造函数
 * - @AllArgsConstructor: 生成全参构造函数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    /**
     * 菜品唯一标识 ID
     */
    private String id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品描述
     */
    private String description;

    /**
     * 菜品价格（单位：元）
     */
    private double price;

    /**
     * 菜品分类（如：川菜、粤菜、主食、甜点等）
     */
    private String category;

    /**
     * 菜品是否可用（ true 表示可用，false 表示售罄或下架
     */
    private boolean available;

    /**
     * 菜品图片 URL
     */
    private String imageUrl;

}
