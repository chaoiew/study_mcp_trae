package com.example.mcp.food.mcp;

import org.springframework.ai.mcp.server.McpPrompt;
import org.springframework.ai.mcp.server.McpPromptTemplate;
import org.springframework.ai.mcp.server.McpPromptArgument;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订餐服务 MCP 提示词模板类 - 提供预定义的提示词模板
 *
 * 知识点（核心）：
 * - @McpPrompt: Spring AI MCP SDK 注解，标识该方法返回一个 MCP 提示词
 *   - name: 提示词名称
 *   - description: 提示词描述
 * - @McpPromptArgument: 定义提示词的参数
 *   - name: 参数名称
 *   - description: 参数描述
 *   - required: 是否必填
 */
@Component
public class FoodOrderingPrompts {

    /**
     * 推荐菜品提示词
     */
    @McpPrompt(
            name = "recommend_dishes",
            description = "根据用户的口味偏好推荐合适的菜品"
    )
    public String recommendDishes(
            @McpPromptArgument(name = "taste", description = "口味偏好，如：辣、清淡、甜等", required = true) String taste,
            @McpPromptArgument(name = "budget", description = "预算范围，如：人均50元", required = false) String budget,
            @McpPromptArgument(name = "category", description = "菜品类别的偏好，如：川菜、粤菜", required = false) String category
    ) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位专业的美食推荐助手。请根据以下用户的需求推荐合适的菜品。\n\n");
        prompt.append("用户需求：\n");
        prompt.append("- 口味偏好：").append(taste).append("\n");
        if (budget != null && !budget.isEmpty()) {
            prompt.append("- 预算：").append(budget).append("\n");
        }
        if (category != null && !category.isEmpty()) {
            prompt.append("- 菜品偏好：").append(category).append("\n");
        }
        prompt.append("\n请先使用 get_available_menu 工具获取可用菜单，然后根据用户需求推荐3-5道菜品。");
        prompt.append("每道菜品请说明推荐理由。");
        return prompt.toString();
    }

    /**
     * 帮助用户下单提示词
     */
    @McpPrompt(
            name = "help_order",
            description = "帮助用户完成订餐流程"
    )
    public String helpOrder(
            @McpPromptArgument(name = "userId", description = "用户 ID", required = true) String userId
    ) {
        return """
               你是一位专业的订餐助手。请帮助用户完成订餐。
               用户 ID: """ + userId + """
               请按以下步骤操作：
               1. 先询问用户想吃什么，或者使用 get_available_menu 展示菜单
               2. 确认用户选择的菜品和数量
               3. 询问配送地址、联系电话
               4. 确认订单信息
               5. 使用 create_order 工具创建订单
               
               请友好地引导用户完成整个订餐流程。
               """;
    }

    /**
     * 订单查询提示词
     */
    @McpPrompt(
            name = "query_order",
            description = "帮助用户查询订单状态"
    )
    public String queryOrder(
            @McpPromptArgument(name = "userId", description = "用户 ID", required = true) String userId
    ) {
        return """
               你是一位专业的订单查询助手。请帮助用户查询订单信息。
               用户 ID: """ + userId + """
               
               请先使用 get_user_orders 获取用户的所有订单，然后根据用户需求提供帮助。
               如果用户想了解某个具体订单，请使用 get_order_detail 获取详细信息。
               如果用户想取消订单，请先确认订单状态后使用 cancel_order。
               """;
    }

    /**
     * 通用助手提示词
     */
    @McpPrompt(
            name = "food_assistant",
            description = "通用的订餐服务助手"
    )
    public String foodAssistant() {
        return """
               你是一位专业的订餐服务小助手，欢迎来到我们的餐厅！
               
               你可以帮助用户：
               1. 查询菜单（使用 get_available_menu, search_menu, get_menu_by_category
               2. 推荐菜品（使用 get_menu_item_detail 查看详情
               3. 创建订单（使用 create_order）
               4. 查询订单（使用 get_order_detail, get_user_orders
               5. 取消订单（使用 cancel_order）
               
               请友好、专业地回答用户的问题。
               """;
    }
}
