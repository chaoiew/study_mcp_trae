package com.example.mcp.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 主类 - 应用程序入口
 *
 * 知识点：
 * - @SpringBootApplication: Spring Boot 核心注解，组合了 @Configuration、@EnableAutoConfiguration 和 @ComponentScan
 */
@SpringBootApplication
public class McpFoodOrderingApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(McpFoodOrderingApplication.class, args);
    }

}
