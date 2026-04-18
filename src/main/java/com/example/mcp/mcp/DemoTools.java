package com.example.mcp.mcp;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MCP 工具类 - 提供各种工具供 AI 调用
 */
@Component
public class DemoTools {

    private final Random random = new Random();

    /**
     * 生成随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    @McpTool(name = "generate_random_number", description = "生成指定范围内的随机数")
    public Map<String, Object> generateRandomNumber(int min, int max) {
        int randomNumber = random.nextInt(max - min + 1) + min;
        Map<String, Object> result = new HashMap<>();
        result.put("random_number", randomNumber);
        result.put("min", min);
        result.put("max", max);
        return result;
    }

    /**
     * 计算两个数的和
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return 两数之和
     */
    @McpTool(name = "add_numbers", description = "计算两个数的和")
    public Map<String, Object> addNumbers(int a, int b) {
        int sum = a + b;
        Map<String, Object> result = new HashMap<>();
        result.put("sum", sum);
        result.put("a", a);
        result.put("b", b);
        return result;
    }

    /**
     * 生成问候语
     *
     * @param name 名字
     * @param language 语言（中文/英文）
     * @return 问候语
     */
    @McpTool(name = "generate_greeting", description = "生成问候语")
    public Map<String, Object> generateGreeting(String name, String language) {
        String greeting;
        if ("中文".equals(language) || "zh".equals(language)) {
            greeting = "你好，" + name + "！欢迎使用 Spring AI MCP 服务！";
        } else {
            greeting = "Hello, " + name + "! Welcome to Spring AI MCP Service!";
        }
        Map<String, Object> result = new HashMap<>();
        result.put("greeting", greeting);
        result.put("name", name);
        result.put("language", language);
        return result;
    }

    /**
     * 获取当前时间信息
     *
     * @return 当前时间信息
     */
    @McpTool(name = "get_current_time", description = "获取当前时间信息")
    public Map<String, Object> getCurrentTime() {
        Map<String, Object> result = new HashMap<>();
        result.put("current_time", System.currentTimeMillis());
        result.put("current_date", new java.util.Date().toString());
        return result;
    }

    /**
     * 检查数字是否为偶数
     *
     * @param number 数字
     * @return 检查结果
     */
    @McpTool(name = "is_even", description = "检查数字是否为偶数")
    public Map<String, Object> isEven(int number) {
        boolean even = number % 2 == 0;
        Map<String, Object> result = new HashMap<>();
        result.put("number", number);
        result.put("is_even", even);
        result.put("message", even ? number + " 是偶数" : number + " 是奇数");
        return result;
    }

}
