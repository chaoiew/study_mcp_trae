package com.example.mcp.food.mcp;

import com.example.mcp.food.model.MenuItem;
import com.example.mcp.food.model.Order;
import com.example.mcp.food.model.OrderItem;
import com.example.mcp.food.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.server.McpTool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订餐服务 MCP 工具类 - 提供 MCP 工具供 AI 模型调用
 *
 * 知识点（核心）：
 * - @Component: Spring 组件注解，将该类注册为 Spring Bean
 * - @McpTool: Spring AI MCP SDK 的核心注解，标识该方法是一个 MCP 工具
 *   - name: 工具名称（可选，默认使用方法名）
 *   - description: 工具描述，告诉 AI 这个工具的作用
 *   - inputSchema: 输入参数的 JSON Schema（可选，Spring 会自动根据方法参数生成）
 */
@Component
@RequiredArgsConstructor
public class FoodOrderingTools {

    // 注入订餐服务
    private final FoodOrderService foodOrderService;

    /**
     * 获取所有可用的菜单
     *
     * MCP 工具说明：查询当前所有可用的菜品列表
     *
     * @return 可用菜单列表
     */
    @McpTool(name = "get_available_menu", description = "获取餐厅所有可用的菜品列表，包含菜品名称、价格、分类等信息")
    public List<Map<String, Object>> getAvailableMenu() {
        List<MenuItem> menuItems = foodOrderService.getAvailableMenu();
        return menuItems.stream()
                .map(this::menuItemToMap)
                .collect(Collectors.toList());
    }

    /**
     * 根据分类查询菜品
     *
     * @param category 菜品分类（如：川菜、粤菜、主食、甜点）
     * @return 该分类下的菜品列表
     */
    @McpTool(name = "get_menu_by_category", description = "根据分类查询菜品，支持的分类有：川菜、粤菜、主食、甜点等")
    public List<Map<String, Object>> getMenuByCategory(String category) {
        List<MenuItem> menuItems = foodOrderService.getMenuItemsByCategory(category);
        return menuItems.stream()
                .map(this::menuItemToMap)
                .collect(Collectors.toList());
    }

    /**
     * 搜索菜品
     *
     * @param keyword 搜索关键词（菜品名称）
     * @return 匹配的菜品列表
     */
    @McpTool(name = "search_menu", description = "根据关键词搜索菜品，支持模糊匹配菜品名称")
    public List<Map<String, Object>> searchMenu(String keyword) {
        List<MenuItem> menuItems = foodOrderService.searchMenuItems(keyword);
        return menuItems.stream()
                .map(this::menuItemToMap)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有菜品分类
     *
     * @return 分类列表
     */
    @McpTool(name = "get_all_categories", description = "获取餐厅所有的菜品分类")
    public Set<String> getAllCategories() {
        return foodOrderService.getAllCategories();
    }

    /**
     * 获取菜品详情
     *
     * @param menuItemId 菜品 ID
     * @return 菜品详情
     */
    @McpTool(name = "get_menu_item_detail", description = "根据菜品 ID 获取菜品的详细信息")
    public Map<String, Object> getMenuItemDetail(String menuItemId) {
        try {
            MenuItem menuItem = foodOrderService.getMenuItemById(menuItemId);
            return menuItemToMap(menuItem);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * 创建订单
     *
     * @param userId 用户 ID（必填，用于标识下单用户）
     * @param menuItemIds 菜品 ID 列表（必填，格式：[{"menuItemId":"M001","quantity":2},...]）
     * @param deliveryAddress 配送地址（必填）
     * @param phone 联系电话（必填）
     * @param remarks 备注信息（选填，如：不要辣、少盐等）
     * @return 创建的订单信息
     */
    @McpTool(name = "create_order", description = "创建订餐订单，需要提供用户 ID、菜品列表、配送地址和联系电话")
    public Map<String, Object> createOrder(String userId, List<Map<String, Object>> menuItemIds,
                                            String deliveryAddress, String phone, String remarks) {
        try {
            // 将 Map 转换为 OrderItem 列表
            List<OrderItem> orderItems = menuItemIds.stream()
                    .map(item -> OrderItem.builder()
                            .menuItemId((String) item.get("menuItemId"))
                            .quantity((Integer) item.getOrDefault("quantity", 1))
                            .build())
                    .collect(Collectors.toList());

            // 创建订单
            Order order = foodOrderService.createOrder(userId, orderItems, deliveryAddress, phone, remarks);

            return orderToMap(order);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单 ID
     * @return 订单详情
     */
    @McpTool(name = "get_order_detail", description = "根据订单 ID 查询订单的详细信息")
    public Map<String, Object> getOrderDetail(String orderId) {
        try {
            Order order = foodOrderService.getOrderById(orderId);
            return orderToMap(order);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * 查询用户的订单列表
     *
     * @param userId 用户 ID
     * @return 订单列表
     */
    @McpTool(name = "get_user_orders", description = "查询指定用户的所有订单列表")
    public List<Map<String, Object>> getUserOrders(String userId) {
        List<Order> orders = foodOrderService.getUserOrders(userId);
        return orders.stream()
                .map(this::orderToMap)
                .collect(Collectors.toList());
    }

    /**
     * 取消订单
     *
     * @param orderId 订单 ID
     * @return 取消结果
     */
    @McpTool(name = "cancel_order", description = "取消指定订单，只有待处理和已确认状态的订单可以取消")
    public Map<String, Object> cancelOrder(String orderId) {
        try {
            Order order = foodOrderService.cancelOrder(orderId);
            Map<String, Object> result = orderToMap(order);
            result.put("message", "订单已成功取消");
            return result;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * 更新订单状态（管理员功能）
     *
     * @param orderId 订单 ID
     * @param status 新状态（PENDING, CONFIRMED, PREPARING, DELIVERING, COMPLETED, CANCELLED）
     * @return 更新后的订单
     */
    @McpTool(name = "update_order_status", description = "更新订单状态（管理员功能），可选状态：PENDING, CONFIRMED, PREPARING, DELIVERING, COMPLETED, CANCELLED")
    public Map<String, Object> updateOrderStatus(String orderId, String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            Order order = foodOrderService.updateOrderStatus(orderId, orderStatus);
            return orderToMap(order);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * 获取订单统计信息
     *
     * @return 统计信息
     */
    @McpTool(name = "get_order_statistics", description = "获取订单统计信息，包括总订单数、各状态订单数等")
    public Map<String, Object> getOrderStatistics() {
        return foodOrderService.getOrderStatistics();
    }

    // ==================== 辅助方法 ====================

    /**
     * 将 MenuItem 对象转换为 Map，便于返回给 MCP 客户端
     */
    private Map<String, Object> menuItemToMap(MenuItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", item.getId());
        map.put("name", item.getName());
        map.put("description", item.getDescription());
        map.put("price", item.getPrice());
        map.put("category", item.getCategory());
        map.put("available", item.isAvailable());
        map.put("imageUrl", item.getImageUrl());
        return map;
    }

    /**
     * 将 Order 对象转换为 Map，便于返回给 MCP 客户端
     */
    private Map<String, Object> orderToMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("userId", order.getUserId());
        map.put("items", order.getItems().stream()
                .map(this::orderItemToMap)
                .collect(Collectors.toList()));
        map.put("totalAmount", order.getTotalAmount());
        map.put("status", order.getStatus().name());
        map.put("deliveryAddress", order.getDeliveryAddress());
        map.put("phone", order.getPhone());
        map.put("remarks", order.getRemarks());
        map.put("createTime", order.getCreateTime().toString());
        map.put("updateTime", order.getUpdateTime().toString());
        return map;
    }

    /**
     * 将 OrderItem 对象转换为 Map
     */
    private Map<String, Object> orderItemToMap(OrderItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuItemId", item.getMenuItemId());
        map.put("menuItemName", item.getMenuItemName());
        map.put("price", item.getPrice());
        map.put("quantity", item.getQuantity());
        map.put("subtotal", item.getSubtotal());
        return map;
    }
}
