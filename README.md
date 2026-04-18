# Spring AI 1.0.0-M6 MCP 服务 Demo

这是一个基于 Spring AI 1.0.0-M6 的 MCP（Model Context Protocol）服务示例项目，展示了如何使用 `@McpTool`、`@McpResource` 和 `@McpPrompt` 等注解。

## 技术栈

- Java 21
- Spring Boot 3.4.0
- Spring AI 1.0.0-M6
- Lombok
- Maven

## 项目结构

```
src/main/java/com/example/mcp/
├── McpDemoApplication.java       # Spring Boot 主类
├── config/
│   └── McpServerConfig.java       # MCP 服务器配置
└── mcp/
    ├── DemoTools.java             # MCP 工具类（@McpTool）
    ├── DemoResources.java         # MCP 资源类（@McpResource）
    └── DemoPrompts.java           # MCP 提示词类（@McpPrompt）
```

## 核心功能

### 1. MCP 工具（@McpTool）

在 `DemoTools.java` 中定义了以下工具：

- `generate_random_number(min, max)` - 生成指定范围内的随机数
- `add_numbers(a, b)` - 计算两个数的和
- `generate_greeting(name, language)` - 生成问候语
- `get_current_time()` - 获取当前时间信息
- `is_even(number)` - 检查数字是否为偶数

### 2. MCP 资源（@McpResource）

在 `DemoResources.java` 中定义了以下资源：

- `demo://service/info` - 服务信息
- `demo://time/current` - 当前时间
- `demo://greeting/{name}` - 个性化问候（使用模板 URI）
- `demo://help` - 帮助信息

### 3. MCP 提示词（@McpPrompt）

在 `DemoPrompts.java` 中定义了以下提示词：

- `math_assistant` - 数学计算助手
- `personalized_greeting` - 生成个性化问候
- `time_assistant` - 时间信息助手
- `general_assistant` - 通用 MCP 助手

## 如何运行

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
java -jar target/spring-ai-mcp-demo-1.0.0.jar
```

## 如何与 OpenCode 集成

在 OpenCode 的 MCP 配置文件中添加：

```json
{
  "mcpServers": {
    "spring-ai-mcp-demo": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/spring-ai-mcp-demo-1.0.0.jar"
      ]
    }
  }
}
```

## 示例用法

### 1. 生成随机数

```
帮我生成一个 1 到 100 之间的随机数
```

AI 会调用 `generate_random_number` 工具。

### 2. 计算两数之和

```
计算 42 + 13 的结果
```

AI 会调用 `add_numbers` 工具。

### 3. 生成问候语

```
生成一个中文问候语给张三
```

AI 会调用 `generate_greeting` 工具。

### 4. 获取当前时间

```
现在是什么时间？
```

AI 会调用 `get_current_time` 工具或访问 `demo://time/current` 资源。

### 5. 检查数字是否为偶数

```
123 是偶数吗？
```

AI 会调用 `is_even` 工具。

## 学习要点

1. **@McpTool** - 定义 AI 可调用的工具
2. **@McpResource** - 提供只读资源给 AI
3. **@McpResourceTemplate** - 支持参数化的资源 URI
4. **@McpPrompt** - 提供预定义提示词模板
5. **McpServer** - MCP 服务端的核心配置
6. **StdioServerTransport** - 标准输入输出传输方式

## 注意事项

- Spring AI 1.1.4 版本的 MCP 功能已经比较稳定
- 项目使用标准输入输出传输方式，适用于与 OpenCode、Claude Desktop 等工具集成
- 所有代码都包含了详细的中文注释，便于学习和理解
