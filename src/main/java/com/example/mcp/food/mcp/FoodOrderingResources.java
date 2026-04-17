package com.example.mcp.food.mcp;

import com.example.mcp.food.model.MenuItem;
import com.example.mcp.food.model.Order;
import com.example.mcp.food.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.server.McpResource;
import org.springframework.ai.mcp.server.McpResourceTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订餐服务 MCP 资源类 - 提供 MCP 资源供 AI 模型读取
 *
 * 知识点（核心）：
 * - @McpResource: Spring AI MCP SDK 注解，标识该方法返回一个 MCP 资源
 *   - uri: 资源的唯一标识符 URI
 *   - name: 资源名称
 *   - description: 资源描述
 *   - mimeType: 资源的 MIME 类型（如 text/plain, application/json 等）
 * - @McpResourceTemplate: 用于定义资源模板，支持通配符 URI
 */
@Component
@RequiredArgsConstructor
public class FoodOrderingResources {

    private final FoodOrderService foodOrderService;

    /**
     * 获取菜单概览资源
     *
     * URI: menu://overview
     * 这个资源返回整个菜单的概览信息
     */
    @McpResource(
            uri = "menu://overview",
            name = "菜单概览",
            description = "餐厅菜单概览，包含所有可用菜品的简要信息",
            mimeType = "text/plain"
    )
    public String getMenuOverview() {
        List<MenuItem> menuItems = foodOrderService.getAvailableMenu();

        StringBuilder sb = new StringBuilder();
        sb.append("=== 餐厅菜单概览 ===\n\n");

        // 按分类分组
        var groupedByCategory = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));

        for (var entry : groupedByCategory.entrySet()) {
            sb.append("【").append(entry.getKey()).append("】\n");
            for (MenuItem item : entry.getValue()) {
                sb.append(String.format("- %s: ¥%.2f\n", item.getName(), item.getPrice()));
                sb.append(String.format("  %s\n", item.getDescription()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * 获取指定菜品的详细信息资源（使用模板 URI）
     *
     * URI: menu://item/{itemId}
     * @McpResourceTemplate 注解表示这是一个资源模板，支持通配符
     */
    @McpResourceTemplate(
            uriTemplate = "menu://item/{itemId}",
            name = "菜品详情",
            description = "获取指定菜品的详细信息",
            mimeType = "text/plain"
    )
    public String getMenuItemResource(String itemId) {
        try {
            MenuItem item = foodOrderService.getMenuItemById(itemId);
            StringBuilder sb = new StringBuilder();
            sb.append("=== 菜品详情 ===\n");
            sb.append("菜品 ID: ").append(item.getId()).append("\n");
            sb.append("菜品名称: ").append(item.getName()).append("\n");
            sb.append("价格: ¥").append(String.format("%.2f", item.getPrice())).append("\n");
            sb.append("分类: ").append(item.getCategory()).append("\n");
            sb.append("状态: ").append(item.isAvailable() ? "可点" : "暂不可用").append("\n");
            sb.append("描述: ").append(item.getDescription()).append("\n");
            return sb.toString();
        } catch (Exception e) {
            return "未找到菜品: " + itemId + "\n错误信息: " + e.getMessage();
        }
    }

    /**
     * 获取订单统计资源
     */
    @McpResource(
            uri = "orders://statistics",
            name = "订单统计",
            description = "订单统计信息概览",
            mimeType = "text/plain"
    )
    public String getOrderStatisticsResource() {
        var stats = foodOrderService.getOrderStatistics();

        StringBuilder sb = new StringBuilder();
        sb.append("=== 订单统计 ===\n\n");
        sb.append("总订单数: ").append(stats.get("totalOrders")).append("\n");
        sb.append("待处理: ").append(stats.get("pendingCount")).append("\n");
        sb.append("已确认: ").append(stats.get("confirmedCount")).append("\n");
        sb.append("制作中: ").append(stats.get("preparingCount")).append("\n");
        sb.append("配送中: ").append(stats.get("deliveringCount")).append("\n");
        sb.append("已完成: ").append(stats.get("completedCount")).append("\n");
        sb.append("已取消: ").append(stats.get("cancelledCount")).append("\n");

        return sb.toString();
    }

    /**
     * 获取指定订单的详细信息资源（使用模板 URI）
     *
     * URI: orders://detail/{orderId}
     */
    @McpResourceTemplate(
            uriTemplate = "orders://detail/{orderId}",
            name = "订单详情",
            description = "获取指定订单的详细信息",
            mimeType = "text/plain"
    )
    public String getOrderDetailResource(String orderId) {
        try {
            Order order = foodOrderService.getOrderById(orderId);

            StringBuilder sb = new StringBuilder();
            sb.append("=== 订单详情 ===\n");
            sb.append("订单 ID: ").append(order.getId()).append("\n");
            sb.append("用户 ID: ").append(order.getUserId()).append("\n");
            sb.append("状态: ").append(order.getStatus()).append("\n");
            sb.append("总金额: ¥").append(String.format("%.2f", order.getTotalAmount())).append("\n");
            sb.append("配送地址: ").append(order.getDeliveryAddress()).append("\n");
            sb.append("联系电话: ").append(order.getPhone()).append("\n");
            if (order.getRemarks() != null && !order.getRemarks().isEmpty()) {
                sb.append("备注: ").append(order.getRemarks()).append("\n");
            }
            sb.append("下单时间: ").append(order.getCreateTime()).append("\n");
            sb.append("\n--- 订单项 ---\n");

            for (var item : order.getItems()) {
                sb.append(String.format("- %s x%d: ¥%.2f (小计: ¥%.2f)\n",
                        item.getMenuItemName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal()));
            }

            return sb.toString();
        } catch (Exception e) {
            return "未找到订单: " + orderId + "\n错误信息: " + e.getMessage();
        }
    }

    /**
     * 获取今日推荐菜品资源
     */
    @McpResource(
            uri = "menu://today-special",
            name = "今日推荐",
            description = "今日推荐菜品",
            mimeType = "text/plain"
    )
    public String getTodaySpecialResource() {
        List<MenuItem> menuItems = foodOrderService.getAvailableMenu();

        StringBuilder sb = new StringBuilder();
        sb.append("=== 今日推荐 ===\n\n");
        sb.append("精选美食，不容错过！\n\n");

        // 简单的推荐逻辑：前3个可用菜品
        List<MenuItem> recommendations = menuItems.stream()
                .limit(3)
                .collect(Collectors.toList());

        for (MenuItem item : recommendations) {
            sb.append("🌟 ").append(item.getName()).append("\n");
            sb.append("   价格: ¥").append(String.format("%.2f", item.getPrice())).append("\n");
            sb.append("   ").append(item.getDescription()).append("\n\n");
        }

        return sb.toString();
    }
}
