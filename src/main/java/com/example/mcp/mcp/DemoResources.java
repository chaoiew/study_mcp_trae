package com.example.mcp.mcp;

import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MCP 资源类 - 提供各种资源供 AI 读取
 */
@Component
public class DemoResources {

    /**
     * 获取服务信息资源
     *
     * URI: demo://service/info
     */
    @McpResource(
            uri = "demo://service/info",
            name = "服务信息",
            description = "获取 MCP 服务的基本信息",
            mimeType = "text/plain"
    )
    public String getServiceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Spring AI MCP 服务信息 ===\n");
        sb.append("服务名称: Spring AI MCP Demo\n");
        sb.append("版本: 1.0.0\n");
        sb.append("Spring AI 版本: 1.1.4\n");
        sb.append("当前时间: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("可用工具: generate_random_number, add_numbers, generate_greeting, get_current_time, is_even\n");
        sb.append("可用资源: demo://service/info, demo://greeting/{name}, demo://time/current\n");
        return sb.toString();
    }

    /**
     * 获取当前时间资源
     *
     * URI: demo://time/current
     */
    @McpResource(
            uri = "demo://time/current",
            name = "当前时间",
            description = "获取当前的日期和时间",
            mimeType = "text/plain"
    )
    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "当前时间: " + now.format(formatter) + "\n" +
               "年: " + now.getYear() + "\n" +
               "月: " + now.getMonthValue() + "\n" +
               "日: " + now.getDayOfMonth() + "\n" +
               "时: " + now.getHour() + "\n" +
               "分: " + now.getMinute() + "\n" +
               "秒: " + now.getSecond();
    }


    /**
     * 获取帮助信息资源
     *
     * URI: demo://help
     */
    @McpResource(
            uri = "demo://help",
            name = "帮助信息",
            description = "获取 MCP 服务的帮助信息",
            mimeType = "text/plain"
    )
    public String getHelpInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MCP 服务帮助 ===\n\n");
        sb.append("可用工具:\n");
        sb.append("1. generate_random_number(min, max) - 生成随机数\n");
        sb.append("2. add_numbers(a, b) - 计算两数之和\n");
        sb.append("3. generate_greeting(name, language) - 生成问候语\n");
        sb.append("4. get_current_time() - 获取当前时间\n");
        sb.append("5. is_even(number) - 检查数字是否为偶数\n\n");
        sb.append("可用资源:\n");
        sb.append("1. demo://service/info - 服务信息\n");
        sb.append("2. demo://time/current - 当前时间\n");
        sb.append("3. demo://greeting/{name} - 个性化问候\n");
        return sb.toString();
    }

}
