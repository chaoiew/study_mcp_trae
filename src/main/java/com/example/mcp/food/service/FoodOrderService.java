package com.example.mcp.food.service;

import com.example.mcp.food.model.MenuItem;
import com.example.mcp.food.model.Order;
import com.example.mcp.food.model.OrderItem;
import com.example.mcp.food.repository.MenuRepository;
import com.example.mcp.food.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 订餐服务类 - 处理订餐相关的业务逻辑
 *
 * 知识点：
 * - @Service: Spring 服务层注解，标识这是一个业务逻辑组件
 * - @RequiredArgsConstructor: Lombok 注解，自动生成包含所有 final 字段的构造函数
 * - 依赖注入：通过构造函数注入 Repository 依赖
 */
@Service
@RequiredArgsConstructor
public class FoodOrderService {

    // 菜单数据仓库 - 依赖注入
    private final MenuRepository menuRepository;

    // 订单数据仓库 - 依赖注入
    private final OrderRepository orderRepository;

    /**
     * 获取所有可用的菜单项
     *
     * @return 可用菜单项列表
     */
    public List<MenuItem> getAvailableMenu() {
        return menuRepository.getAvailableMenuItems();
    }

    /**
     * 获取所有菜单项（包括不可用的）
     *
     * @return 所有菜单项列表
     */
    public List<MenuItem> getAllMenuItems() {
        return menuRepository.getAllMenuItems();
    }

    /**
     * 根据菜品 ID 获取菜单项
     *
     * @param menuItemId 菜品 ID
     * @return 菜单项，如果不存在抛出异常
     */
    public MenuItem getMenuItemById(String menuItemId) {
        MenuItem menuItem = menuRepository.getMenuItemById(menuItemId);
        if (menuItem == null) {
            throw new IllegalArgumentException("菜单项不存在: " + menuItemId);
        }
        return menuItem;
    }

    /**
     * 根据分类获取菜单项
     *
     * @param category 分类名称
     * @return 该分类下的菜单项列表
     */
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuRepository.getMenuItemsByCategory(category);
    }

    /**
     * 搜索菜单项
     *
     * @param keyword 搜索关键词
     * @return 匹配的菜单项列表
     */
    public List<MenuItem> searchMenuItems(String keyword) {
        return menuRepository.searchMenuItemsByName(keyword);
    }

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    public Set<String> getAllCategories() {
        return menuRepository.getAllCategories();
    }

    /**
     * 创建订单
     *
     * @param userId 用户 ID
     * @param orderItems 订单项列表
     * @param deliveryAddress 配送地址
     * @param phone 联系电话
     * @param remarks 备注信息
     * @return 创建的订单
     */
    public Order createOrder(String userId, List<OrderItem> orderItems,
                              String deliveryAddress, String phone, String remarks) {
        // 验证订单项
        validateOrderItems(orderItems);

        // 计算订单总金额
        double totalAmount = calculateTotalAmount(orderItems);

        // 构建订单对象
        Order order = Order.builder()
                .userId(userId)
                .items(orderItems)
                .totalAmount(totalAmount)
                .deliveryAddress(deliveryAddress)
                .phone(phone)
                .remarks(remarks)
                .build();

        // 创建订单
        return orderRepository.createOrder(order);
    }

    /**
     * 验证订单项
     *
     * @param orderItems 订单项列表
     */
    private void validateOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("订单项不能为空");
        }

        for (OrderItem item : orderItems) {
            MenuItem menuItem = menuRepository.getMenuItemById(item.getMenuItemId());
            if (menuItem == null) {
                throw new IllegalArgumentException("菜品不存在: " + item.getMenuItemId());
            }
            if (!menuItem.isAvailable()) {
                throw new IllegalArgumentException("菜品已售罄或下架: " + menuItem.getName());
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("菜品数量必须大于0");
            }

            // 补充订单项信息
            item.setMenuItemName(menuItem.getName());
            item.setPrice(menuItem.getPrice());
            item.setSubtotal(menuItem.getPrice() * item.getQuantity());
        }
    }

    /**
     * 计算订单总金额
     *
     * @param orderItems 订单项列表
     * @return 总金额
     */
    private double calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    /**
     * 根据订单 ID 获取订单
     *
     * @param orderId 订单 ID
     * @return 订单对象
     */
    public Order getOrderById(String orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        return order;
    }

    /**
     * 获取用户的所有订单
     *
     * @param userId 用户 ID
     * @return 订单列表
     */
    public List<Order> getUserOrders(String userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    /**
     * 获取所有订单
     *
     * @return 所有订单列表
     */
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    /**
     * 更新订单状态
     *
     * @param orderId 订单 ID
     * @param status 新的订单状态
     * @return 更新后的订单
     */
    public Order updateOrderStatus(String orderId, Order.OrderStatus status) {
        Order order = orderRepository.updateOrderStatus(orderId, status);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        return order;
    }

    /**
     * 取消订单
     *
     * @param orderId 订单 ID
     * @return 取消后的订单
     */
    public Order cancelOrder(String orderId) {
        Order order = orderRepository.cancelOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在或无法取消: " + orderId);
        }
        return order;
    }

    /**
     * 获取订单统计信息
     *
     * @return 统计信息
     */
    public Map<String, Object> getOrderStatistics() {
        return orderRepository.getOrderStatistics();
    }
}
