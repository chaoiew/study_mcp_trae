package com.example.mcp.food.config;

import org.springframework.ai.mcp.server.McpServer;
import org.springframework.ai.mcp.server.McpServerTools;
import org.springframework.ai.mcp.server.McpServerResources;
import org.springframework.ai.mcp.server.McpServerPrompts;
import org.springframework.ai.mcp.server.transport.StdioServerTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MCP Server 配置类 - 配置 MCP 服务端
 *
 * 知识点（核心配置）：
 * - @Configuration: Spring 配置类注解
 * - McpServer: MCP 服务端核心类，用于注册和管理工具、资源、提示词
 * - StdioServerTransport: 标准输入输出传输方式，适用于 CLI 工具集成
 */
@Configuration
public class McpServerConfig {

    /**
     * 配置 MCP Server
     *
     * @param tools MCP 工具列表（由 Spring 自动注入所有 @McpTool 注解的方法）
     * @param resources MCP 资源列表（由 Spring 自动注入所有 @McpResource 和 @McpResourceTemplate 注解的方法）
     * @param prompts MCP 提示词列表（由 Spring 自动注入所有 @McpPrompt 注解的方法）
     * @return MCP Server 实例
     */
    @Bean
    public McpServer mcpServer(McpServerTools tools,
                                McpServerResources resources,
                                McpServerPrompts prompts) {
        // 创建 MCP Server 并配置服务器信息
        return McpServer.builder("food-ordering-server", "1.0.0")
                // 注册所有工具（通过 @McpTool 注解）
                .tools(tools)
                // 注册所有资源（通过 @McpResource 和 @McpResourceTemplate 注解）
                .resources(resources)
                // 注册所有提示词（通过 @McpPrompt 注解）
                .prompts(prompts)
                .build();
    }

    /**
     * 配置标准输入输出传输方式
     * 这是 MCP 最常用的传输方式，适用于与 OpenCode、Claude Desktop 等集成
     *
     * @param mcpServer MCP Server 实例
     * @return StdioServerTransport 实例
     */
    @Bean
    public StdioServerTransport stdioServerTransport(McpServer mcpServer) {
        return new StdioServerTransport(mcpServer);
    }

}
