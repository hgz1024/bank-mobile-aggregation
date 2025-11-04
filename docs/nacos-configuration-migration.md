# Nacos配置中心迁移方案说明文档

## 1. 概述

本文档详细说明了将项目配置迁移到Nacos配置中心的具体步骤和实施方案。通过将配置信息集中管理到Nacos配置中心，可以实现配置的统一管理、动态更新和环境隔离。

## 2. 迁移目标

- 将各服务的业务配置迁移到Nacos配置中心
- 保留Nacos注册中心配置在本地配置文件中
- 实现配置的集中管理和动态刷新能力
- 保证服务启动的可靠性和稳定性

## 3. 迁移步骤

### 3.1 添加Nacos配置依赖

在各微服务的pom.xml文件中添加Nacos配置管理依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

涉及的服务包括：
- user-service
- point-service
- product-service
- aggregation-service
- api-gateway

#### 3.1.1 user-service依赖配置示例

在user-service/pom.xml的dependencies节点中添加：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

### 3.2 创建bootstrap.yml配置文件

在各服务的src/main/resources目录下创建bootstrap.yml文件，配置Nacos配置中心信息：

```yaml
spring:
  application:
    name: {service-name}
  cloud:
    nacos:
      server-addr: localhost:8848
      config:
        namespace: public
        group: BANK_GROUP
        file-extension: yaml
```

其中各配置项的含义如下：
- server-addr: Nacos服务器地址
- namespace: 命名空间ID，用于配置隔离，可区分不同环境（开发、测试、生产）或不同业务
- group: 配置分组，用于区分不同业务领域的配置
- file-extension: 配置文件格式，支持yaml、properties等

#### 3.2.1 Namespace说明

Namespace（命名空间）是Nacos中重要的隔离概念，主要作用包括：

1. **环境隔离**：通过不同的Namespace区分开发环境、测试环境和生产环境
   - 开发环境Namespace ID: dev
   - 测试环境Namespace ID: test
   - 生产环境Namespace ID: prod

2. **业务隔离**：通过不同的Namespace区分不同的业务线或项目

3. **配置管理**：避免不同环境或业务线的配置相互影响

在当前项目中，我们使用的是默认的"public"命名空间，但在实际生产环境中，建议为不同的环境创建独立的Namespace，以实现更好的配置隔离和管理。

#### 3.2.2 user-service示例

以user-service为例，创建bootstrap.yml文件：

文件路径：user-service/src/main/resources/bootstrap.yml

```yaml
spring:
  application:
    name: user-service
  cloud:
    nacos:
      server-addr: localhost:8848
      config:
        namespace: public
        group: BANK_GROUP
        file-extension: yaml
```

### 3.3 调整application.yml配置文件

将各服务的application.yml文件中的业务配置迁移到Nacos配置中心，保留下列配置在本地：
- 服务端口(server.port)
- 应用名称(spring.application.name)
- Nacos注册中心配置(spring.cloud.nacos.discovery)
- 其他基础设施配置

#### 3.3.1 user-service示例

修改前的user-service/application.yml：

```yaml
server:
  port: 8081

spring:
  application:
    name: user-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: public
        group: BANK_GROUP
      config:
        enabled: false  # 本示例不使用配置中心
    sentinel:
      transport:
        # dashboard地址
        dashboard: 127.0.0.1:8080
        port: 8719 #指定一个端口，开启客户端与sentinel控制天进行数据交互
      eager: true
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.bank.user: debug
```

修改后的user-service/application.yml：

```yaml
server:
  port: 8081

spring:
  application:
    name: user-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: public
        group: BANK_GROUP
    sentinel:
      transport:
        dashboard: 127.0.0.1:8080
        port: 8719
      eager: true
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.bank.user: debug
```

其中需要保留在本地的配置说明：
- `server.port`: 服务端口，每个服务必须不同，属于基础设施配置
- `spring.application.name`: 应用名称，用于服务注册和配置定位
- `spring.cloud.nacos.discovery.server-addr`: Nacos注册中心地址，属于基础设施配置
- `spring.cloud.sentinel.*`: Sentinel相关配置，用于流量控制

关于Sentinel配置保留在本地的说明：
Sentinel配置保留在本地是符合业界常见做法的，主要原因如下：
1. **基础设施属性**：Sentinel控制台地址属于基础设施配置，通常在同一个环境中所有服务都使用相同的控制台
2. **启动依赖**：Sentinel需要在应用启动时就进行初始化，保留在本地确保能正常初始化
3. **稳定性考虑**：将Sentinel基础连接配置与业务规则分离，避免因配置中心问题影响限流降级功能

但也可以根据实际需求将Sentinel的配置规则（如流控规则、降级规则等）放在Nacos配置中心进行统一管理，实现动态更新。

- `management.*`: Spring Boot Actuator配置，用于健康检查和监控
- `logging.level`: 日志级别配置，便于问题排查

### 3.4 在Nacos控制台创建配置

登录Nacos控制台，为每个服务创建对应的配置文件：

1. Data ID格式：{service-name}.yaml
2. Group：BANK_GROUP
3. 配置格式：YAML
4. 配置内容：从各服务application.yml中迁移的业务配置

#### 3.4.1 user-service示例

在Nacos配置中心创建Data ID为"user-service.yaml"的配置，内容如下：

```yaml
# user-service业务配置示例
    feature:
      user-service:
        new-feature-enabled: true
```

### 3.5 配置共享（可选）

对于多个服务共用的配置，可以创建共享配置文件：
- Data ID：common.yaml
- Group：BANK_GROUP
- 在各服务bootstrap.yml中引用共享配置

#### 3.5.1 引用共享配置示例

在bootstrap.yml中添加shared-configs配置：

```yaml
spring:
  application:
    name: user-service
  cloud:
    nacos:
      server-addr: localhost:8848
      config:
        namespace: public
        group: BANK_GROUP
        file-extension: yaml
        shared-configs:
          - data-id: common.yaml
            group: BANK_GROUP
            refresh: true
```

### 3.6 启用配置自动刷新（可选）

在需要动态刷新配置的Bean上添加@RefreshScope注解：

```java
@RestController
@RefreshScope
public class ConfigController {
    // ...
}
```

## 4. 验证方式

1. 启动Nacos服务器
2. 在Nacos控制台创建各服务配置
3. 启动各微服务
4. 验证服务是否能正确从Nacos获取配置
5. 修改Nacos中的配置，验证动态刷新功能

### 4.1 业务开关验证示例

为了验证配置中心的作用效果，可以在服务中设计一个业务开关，例如在user-service中添加一个功能开关：

1. 在Nacos配置中心的user-service.yaml中添加配置项：
   ```yaml
   feature:
     user-service:
       new-feature-enabled: true
   ```

2. 在UserService中创建一个使用@Value注解和@RefreshScope的Controller：
   ```java
   @RestController
   @RefreshScope
   @RequestMapping("/users")
   public class UserController {
       
       @Value("${feature.user-service.new-feature-enabled:false}")
       private boolean newFeatureEnabled;
       
       @GetMapping("/feature-status")
       public Map<String, Object> getFeatureStatus() {
           Map<String, Object> result = new HashMap<>();
           result.put("newFeatureEnabled", newFeatureEnabled);
           result.put("message", newFeatureEnabled ? "新功能已启用" : "新功能已禁用");
           return result;
       }
   }
   ```

3. 启动服务后，访问`http://localhost:8081/users/feature-status`查看当前开关状态
4. 在Nacos控制台修改`feature.user-service.new-feature-enabled`的值
5. 再次访问接口，验证配置是否动态更新

## 5. 降级策略

如果Nacos配置中心不可用：
1. 服务将使用本地默认配置启动
2. 配置无法动态更新
3. 需要手动修改本地配置并重启服务

## 6. 后续优化建议

1. 配置加密：对敏感配置进行加密存储
2. 权限控制：为不同环境和用户分配不同的配置访问权限
3. 配置版本管理：利用Nacos的配置版本管理功能
4. 配置审计：记录配置变更历史和操作日志