# SkyWalking 集成指南

本文档详细说明如何在当前银行移动端首页数据聚合项目中集成 Apache SkyWalking，实现微服务架构下的全链路监控功能。

## 项目概述

本项目基于 Spring Cloud 微服务架构，采用 Maven 多模块结构，包含以下核心模块：
- api-gateway: API 网关服务
- user-service: 用户服务
- point-service: 积分服务
- product-service: 产品服务
- aggregation-service: 数据聚合服务
- common: 公共模块
- feign-api: Feign 客户端模块

## 技术栈信息

- Spring Boot: 3.0.8
- Spring Cloud: 2022.0.0
- Spring Cloud Alibaba: 2022.0.0.0
- Java 版本: 17

## 第一阶段集成目标

实现各微服务接入 SkyWalking，达到链路追踪效果，具体包括：
1. 部署 SkyWalking 服务
2. 各微服务无侵入式接入 SkyWalking
3. 实现完整的链路追踪可视化

## 集成步骤

### 1. 下载并部署 SkyWalking

#### 1.1 下载 SkyWalking APM

访问 SkyWalking 官方下载地址获取 9.0.0 版本：

```
https://archive.apache.org/dist/skywalking/9.0.0/apache-skywalking-apm-9.0.0.tar.gz
```

#### 1.2 解压并启动

```bash
# 解压文件
tar -zxvf apache-skywalking-apm-9.0.0.tar.gz

# 进入 bin 目录并启动（Windows 环境使用 startup.bat）
cd apache-skywalking-apm-bin/bin
./startup.sh
```

#### 1.3 访问 SkyWalking UI

SkyWalking UI 默认端口为 8080：

```
http://localhost:8080
```

### 2. 下载 SkyWalking Java Agent

#### 2.1 下载 Java Agent

访问以下地址下载 Java Agent：

```
https://archive.apache.org/dist/skywalking/java-agent/9.0.0/apache-skywalking-java-agent-9.0.0.tgz
```

#### 2.2 解压 Agent

```bash
tar -zxvf apache-skywalking-java-agent-9.0.0.tgz
```

### 3. 微服务接入 SkyWalking

#### 3.1 准备 Agent 文件

将解压后的 SkyWalking Agent 目录放置在合适的位置，例如：

```
/opt/skywalking-agent  # Linux
C:\skywalking-agent    # Windows
```

#### 3.2 配置 Spring Cloud Gateway 插件

由于 Spring Cloud Gateway 基于 WebFlux 构建，需要启用特定插件：

1. 进入 SkyWalking Agent 目录
2. 将以下插件从 `optional-plugins` 目录复制到 `plugins` 目录：
   - `apm-spring-cloud-gateway-3.x-plugin-9.0.0.jar`
   - `apm-spring-webflux-5.x-plugin-9.0.0.jar`

#### 3.3 启动参数配置

各微服务启动时需要添加 Java Agent 参数，示例如下：

##### user-service 启动命令

```bash
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=user-service \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar user-service.jar
```

##### 给每个服务启动设置虚拟机选项
-javaagent:D:/dev/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=user-service -Dskywalking.collector.backend_service=localhost:11800

##### point-service 启动命令

```bash
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=point-service \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar point-service.jar
```
-javaagent:D:/dev/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=point-service -Dskywalking.collector.backend_service=localhost:11800

##### product-service 启动命令

```bash
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=product-service \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar product-service.jar
```
-javaagent:D:/dev/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=product-service -Dskywalking.collector.backend_service=localhost:11800

##### aggregation-service 启动命令

```bash
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=aggregation-service \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar aggregation-service.jar
```

-javaagent:D:/dev/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=aggregation-service -Dskywalking.collector.backend_service=localhost:11800

##### api-gateway 启动命令

```bash
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=api-gateway \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar api-gateway.jar
```

-javaagent:D:/dev/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=api-gateway -Dskywalking.collector.backend_service=localhost:11800

注意事项：
- 将 `/path/to/skywalking-agent` 替换为实际的 Agent 路径
- 确保 SkyWalking OAP 服务运行在 `localhost:11800`

### 4. 验证集成效果

#### 4.1 启动服务

按照以下顺序启动各服务：
1. SkyWalking OAP Server
2. Nacos Server
3. user-service
4. point-service
5. product-service
6. aggregation-service
7. api-gateway

#### 4.2 访问应用

通过 API 网关访问应用接口，例如：

```
http://localhost:8080/api/homepage/1
```

#### 4.3 查看追踪效果

访问 SkyWalking UI (`http://localhost:8080`)，查看以下内容：
- 服务拓扑图
- 链路追踪详情
- 服务指标数据

### 5. 故障排除

#### 5.1 服务未出现在拓扑图中

1. 检查 SkyWalking Agent 是否正确配置
2. 确认服务启动参数是否正确
3. 检查 Spring Cloud Gateway 插件是否已复制到 plugins 目录

#### 5.2 链路不完整

1. 确保所有服务都已接入 SkyWalking
2. 检查网络连接是否正常
3. 查看服务日志确认是否有错误信息

#### 5.3 数据未上报

1. 检查 SkyWalking OAP 服务是否正常运行
2. 确认 backend_service 地址配置是否正确
3. 检查防火墙设置是否阻止了通信

#### 5.4 Spring Cloud Gateway 服务不显示

这是一个常见问题，因为 Spring Cloud Gateway 是基于 WebFlux 构建的响应式网关，默认情况下 SkyWalking 不会自动监控这类应用。解决方法如下：

1. 确保已将以下插件从 SkyWalking Agent 的 `optional-plugins` 目录复制到 `plugins` 主目录：
   - `apm-spring-cloud-gateway-3.x-plugin-<version>.jar`（适用于 Spring Boot 3.x）
   - `apm-spring-webflux-5.x-plugin-<version>.jar`

2. 在某些版本中，还需要在 `agent/config/agent.config` 文件中添加以下配置：
   ```
   plugin.spring.cloud.gateway.enabled=true
   ```

3. 重启网关服务，然后刷新 SkyWalking UI 查看是否能正常显示网关服务

## 注意事项

1. SkyWalking 采用无侵入式设计，无需修改任何业务代码
2. 各服务启动后会自动注册到 SkyWalking，无需额外配置
3. 生产环境中建议配置持久化存储（如 Elasticsearch）
4. 建议合理设置采样率以平衡监控效果和性能开销

## 后续阶段规划

第一阶段完成后，可以考虑以下增强功能：
- 配置告警策略
- 集成日志收集功能
- 自定义业务指标监控
- 性能优化分析