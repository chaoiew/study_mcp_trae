package com.example.mcp.mcp;

import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springframework.stereotype.Component;

/**
 * MCP 提示词类 - 提供预定义的提示词模板
 */
@Component
public class DemoPrompts {

    /**
     * 数学助手提示词
     */
    @McpPrompt(
            name = "math_assistant",
            description = "数学计算助手"
    )
    public String mathAssistant() {
        return "你是一位数学计算助手，可以帮助用户进行各种数学计算。\n" +
               "\n" +
               "你可以使用以下工具：\n" +
               "1. add_numbers(a, b) - 计算两个数的和\n" +
               "2. generate_random_number(min, max) - 生成指定范围内的随机数\n" +
               "3. is_even(number) - 检查数字是否为偶数\n" +
               "\n" +
               "请友好地回答用户的数学问题。";
    }

    /**
     * 个性化问候提示词
     */
    @McpPrompt(
            name = "personalized_greeting",
            description = "生成个性化问候"
    )
    public String personalizedGreeting(
            @McpArg(name = "name", description = "用户姓名", required = true) String name,
            @McpArg(name = "language", description = "语言（中文/英文）", required = false) String language
    ) {
        return "请生成一个个性化的问候语给 " + name + ". "
               + (language != null ? "语言使用：" + language : "默认使用中文") + ". "
               + "你可以使用 generate_greeting 工具来生成问候语。";
    }

    /**
     * 时间助手提示词
     */
    @McpPrompt(
            name = "time_assistant",
            description = "时间信息助手"
    )
    public String timeAssistant() {
        return "你是一位时间信息助手，可以帮助用户获取当前时间信息。\n" +
               "\n" +
               "你可以使用以下工具和资源：\n" +
               "1. get_current_time() - 获取当前时间信息\n" +
               "2. demo://time/current - 查看详细的时间信息\n" +
               "\n" +
               "请友好地回答用户关于时间的问题。";
    }

    /**
     * 通用助手提示词
     */
    @McpPrompt(
            name = "general_assistant",
            description = "通用 MCP 助手"
    )
    public String generalAssistant() {
        return "你是一位通用的 MCP 助手，欢迎使用 Spring AI MCP 服务！\n" +
               "\n" +
               "你可以：\n" +
               "1. 帮助用户进行数学计算\n" +
               "2. 生成问候语\n" +
               "3. 提供时间信息\n" +
               "4. 回答关于服务的问题\n" +
               "\n" +
               "请友好、专业地回答用户的问题。";
    }

}
