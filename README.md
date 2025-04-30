# SuperBI - AI赋能的数据可视化平台

SuperBI 是一个基于 Java、Spring Boot、LangChain4j 的智能 BI 平台，支持用户上传 Excel 或表格数据，自动完成数据解析、调用大模型生成可视化图表（基于 ECharts），并在前端展示。项目使用 Kafka 实现异步解耦，MySQL 管理数据存储，具备良好的可扩展性与容错机制。

## ✨ 项目功能特性

- ✅ 支持 Excel / CSV 文件上传
- ✅ 基于 EasyExcel 实现高性能数据解析
- ✅ 利用 LangChain4j 调用大语言模型（如 OpenAI、通义千问）生成图表配置
- ✅ Kafka 异步驱动任务流：上传 → 解析 → 图表生成
- ✅ 自动生成符合 ECharts 标准的 JSON 图表配置
- ✅ 提供图表查询接口，供前端渲染调用
- ✅ 错误处理机制完善，支持生成失败、AI幻觉校验与状态跟踪

## 🚀 快速启动

### 环境要求

- Java 21 或更高（推荐 Java 24）
- Maven 3.9+
- Kafka 服务已启动
- MySQL 数据库已创建并配置
- 可选：Redis（用于缓存）和 MinIO（用于文件存储）

### 启动步骤

1. 克隆项目到本地：
   ```bash
   git clone https://github.com/your-org/SuperBI.git
   cd SuperBI
   ```

2. 修改配置文件（如 `application.yml`），填写数据库、Kafka、AI模型等参数。

3. 启动 Spring Boot 项目：
   ```bash
   ./mvnw spring-boot:run
   ```

4. 访问接口：
   - 上传文件：`POST /api/upload`
   - 获取图表：`GET /api/chart/{fileId}`

### 示例

使用 Postman 上传 Excel 文件：
```http
POST /api/upload
Content-Type: multipart/form-data
Body: file=your_excel_file.xlsx
```

返回内容中包含 fileId，可用于查询图表：

```http
GET /api/chart/{fileId}
```

返回 JSON 格式的 ECharts 配置，可直接渲染。
