package com.example.mcp.food.repository;

import com.example.mcp.food.model.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 订单数据仓库类 - 负责订单数据的存储和查询
 */
@Repository
public class OrderRepository {

    // 使用 ConcurrentHashMap 存储订单，key 为订单 ID，value 为 Order 对象
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    // 订单 ID 生成器
    private final AtomicLong orderIdGenerator = new AtomicLong(1000);

    /**
     * 生成订单 ID
     *
     * @return 订单 ID
     */
    private String generateOrderId() {
        return "ORD" + orderIdGenerator.getAndIncrement();
    }

    /**
     * 创建订单
     *
     * @param order 订单对象（不包含 ID 和时间）
     * @return 创建后的订单（包含 ID 和时间）
     */
    public Order createOrder(Order order) {
        // 生成订单 ID
        String orderId = generateOrderId();
        order.setId(orderId);

        // 设置订单状态为待处理
        order.setStatus(Order.OrderStatus.PENDING);

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        order.setCreateTime(now);
        order.setUpdateTime(now);

        // 存储订单
        orders.put(orderId, order);
        return order;
    }

    /**
     * 根据订单 ID 查询订单
     *
     * @param orderId 订单 ID
     * @return 订单对象，如果不存在返回 null
     */
    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    /**
     * 查询用户的所有订单
     *
     * @param userId 用户 ID
     * @return 该用户的订单列表，按创建时间倒序排列
     */
    public List<Order> getOrdersByUserId(String userId) {
        return orders.values().stream()
                .filter(order -> userId.equals(order.getUserId()))
                .sorted(Comparator.comparing(Order::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 查询所有订单
     *
     * @return 所有订单列表，按创建时间倒序排列
     */
    public List<Order> getAllOrders() {
        return orders.values().stream()
                .sorted(Comparator.comparing(Order::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 根据订单状态查询订单
     *
     * @param status 订单状态
     * @return 该状态的订单列表
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orders.values().stream()
                .filter(order -> status == order.getStatus())
                .sorted(Comparator.comparing(Order::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 更新订单状态
     *
     * @param orderId 订单 ID
     * @param newStatus 新的订单状态
     * @return 更新后的订单，如果不存在返回 null
     */
    public Order updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            order.setUpdateTime(LocalDateTime.now());
            return order;
        }
        return null;
    }

    /**
     * 更新订单信息
     *
     * @param order 更新后的订单对象
     * @return 更新后的订单，如果不存在返回 null
     */
    public Order updateOrder(Order order) {
        if (orders.containsKey(order.getId())) {
            order.setUpdateTime(LocalDateTime.now());
            orders.put(order.getId(), order);
            return order;
        }
        return null;
    }

    /**
     * 取消订单
     *
     * @param orderId 订单 ID
     * @return 取消后的订单，如果不存在或无法取消返回 null
     */
    public Order cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null && canCancel(order)) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            order.setUpdateTime(LocalDateTime.now());
            return order;
        }
        return null;
    }

    /**
     * 判断订单是否可以取消
     *
     * @param order 订单对象
     * @return 是否可以取消
     */
    private boolean canCancel(Order order) {
        // 只有待处理和已确认状态的订单可以取消
        return order.getStatus() == Order.OrderStatus.PENDING ||
               order.getStatus() == Order.OrderStatus.CONFIRMED;
    }

    /**
     * 删除订单（仅用于测试）
     *
     * @param orderId 订单 ID
     * @return 被删除的订单，如果不存在返回 null
     */
    public Order deleteOrder(String orderId) {
        return orders.remove(orderId);
    }

    /**
     * 获取订单统计信息
     *
     * @return 统计信息 Map
     */
    public Map<String, Object> getOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orders.size());
        stats.put("pendingCount", getOrdersByStatus(Order.OrderStatus.PENDING).size());
        stats.put("confirmedCount", getOrdersByStatus(Order.OrderStatus.CONFIRMED).size());
        stats.put("preparingCount", getOrdersByStatus(Order.OrderStatus.PREPARING).size());
        stats.put("deliveringCount", getOrdersByStatus(Order.OrderStatus.DELIVERING).size());
        stats.put("completedCount", getOrdersByStatus(Order.OrderStatus.COMPLETED).size());
        stats.put("cancelledCount", getOrdersByStatus(Order.OrderStatus.CANCELLED).size());
        return stats;
    }
}
