# Spring AI MCP 订餐服务 Demo

这是一个基于 Spring AI MCP SDK 的完整订餐服务示例项目，用于学习 Spring AI MCP 的各项功能。

## 项目简介

本项目实现了一个完整的订餐 MCP 服务，包括：
- 菜单查询与管理
- 订单创建与查询
- 菜品推荐
- 订单状态管理

## 技术栈

- Java 21
- Spring Boot 3.4.0
- Spring AI 1.0.0-M6
- Lombok
- Maven

## 项目结构

```
src/main/java/com/example/mcp/food/
├── McpFoodOrderingApplication.java    # Spring Boot 主类
├── config/
│   └── McpServerConfig.java           # MCP Server 配置
├── model/
│   ├── MenuItem.java                   # 菜单项模型
│   ├── Order.java                      # 订单模型
│   └── OrderItem.java                  # 订单项模型
├── repository/
│   ├── MenuRepository.java             # 菜单数据仓库
│   └── OrderRepository.java            # 订单数据仓库
├── service/
│   └── FoodOrderService.java           # 订餐业务服务
└── mcp/
    ├── FoodOrderingTools.java          # MCP 工具类（核心）
    ├── FoodOrderingResources.java      # MCP 资源类
    └── FoodOrderingPrompts.java        # MCP 提示词模板类
```

## Spring AI MCP SDK 核心知识点

### 1. MCP 工具 (@McpTool)

位置：`FoodOrderingTools.java`

`@McpTool` 注解用于标记一个方法为 MCP 工具，AI 模型可以调用这些工具。

```java
@McpTool(name = "get_available_menu", description = "获取餐厅所有可用的菜品列表")
public List<Map<String, Object>> getAvailableMenu() {
    // 实现代码
}
```

主要工具包括：
- `get_available_menu` - 获取可用菜单
- `search_menu` - 搜索菜品
- `create_order` - 创建订单
- `get_order_detail` - 查询订单详情
- `cancel_order` - 取消订单

### 2. MCP 资源 (@McpResource, @McpResourceTemplate)

位置：`FoodOrderingResources.java`

`@McpResource` 注解用于提供只读资源给 AI 模型。

```java
@McpResource(
    uri = "menu://overview",
    name = "菜单概览",
    description = "餐厅菜单概览",
    mimeType = "text/plain"
)
public String getMenuOverview() {
    // 返回菜单概览内容
}
```

`@McpResourceTemplate` 用于支持带参数的资源 URI：

```java
@McpResourceTemplate(
    uriTemplate = "menu://item/{itemId}",
    name = "菜品详情"
)
public String getMenuItemResource(String itemId) {
    // 根据 itemId 返回对应的菜品详情
}
```

### 3. MCP 提示词 (@McpPrompt)

位置：`FoodOrderingPrompts.java`

`@McpPrompt` 注解用于提供预定义的提示词模板。

```java
@McpPrompt(name = "recommend_dishes", description = "根据用户的口味偏好推荐合适的菜品")
public String recommendDishes(
        @McpPromptArgument(name = "taste", description = "口味偏好") String taste) {
    // 返回提示词内容
}
```

### 4. MCP Server 配置

位置：`McpServerConfig.java`

配置 MCP Server 和传输方式：

```java
@Bean
public McpServer mcpServer(McpServerTools tools,
                            McpServerResources resources,
                            McpServerPrompts prompts) {
    return McpServer.builder("food-ordering-server", "1.0.0")
            .tools(tools)
            .resources(resources)
            .prompts(prompts)
            .build();
}

@Bean
public StdioServerTransport stdioServerTransport(McpServer mcpServer) {
    return new StdioServerTransport(mcpServer);
}
```

## 如何运行

### 前置条件

- JDK 21+
- Maven 3.6+

### 1. 编译项目

```bash
mvn clean package
```

### 2. 运行项目

```bash
mvn spring-boot:run
```

或者直接运行打包后的 jar：

```bash
java -jar target/mcp-food-ordering-server-1.0.0.jar
```

## 如何与 OpenCode 集成

由于这是一个使用 STDIO 传输的 MCP Server，你需要将其打包为可执行的 JAR，然后配置到 OpenCode 的 MCP 配置文件中。

### 1. 打包为可执行 JAR

```bash
mvn clean package
```

### 2. 配置 OpenCode 的 MCP 客户端

在 OpenCode 的 MCP 配置文件中添加：

```json
{
  "mcpServers": {
    "food-ordering": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/mcp-food-ordering-server-1.0.0.jar"
      ]
    }
  }
}
```

## 示例用法

### 1. 查询菜单

可以让 AI 调用 `get_available_menu` 工具获取所有可用菜品。

### 2. 创建订单

```
帮我订一份宫保鸡丁和一份扬州炒饭，送到北京市朝阳区xxx路xxx号，电话13800138000
```

AI 会调用 `create_order` 工具创建订单。

### 3. 查询订单

```
查询我的订单
```

AI 会调用 `get_user_orders` 工具查询订单列表。

## 学习要点

1. **@McpTool** - 定义 AI 可调用的工具
2. **@McpResource** - 提供只读资源给 AI
3. **@McpResourceTemplate** - 支持参数化的资源 URI
4. **@McpPrompt** - 提供预定义提示词模板
5. **McpServer** - MCP 服务端的核心配置
6. **StdioServerTransport** - 标准输入输出传输方式

## 扩展建议

- 添加数据库持久化（使用 JPA/H2/MySQL）
- 添加用户认证
- 添加更多菜品和订单管理功能
- 添加实时通知功能
- 添加支付接口集成
