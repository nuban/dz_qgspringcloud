## 第一课 Nacos
## Nacos服务注册
### 版本
https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

### 父级项目
```xml
<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>2020.0.1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2021.1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
        </dependency>
    </dependencies>
```

### 服务注册
```xml
<dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

```java
@EnableDiscoveryClient
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

```
### 错误解决
```
java.lang.IllegalArgumentException: Param 'serviceName' is illegal, serviceName is blank
```

加上依赖：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>

```

### 启动微服务
> nacos registry, DEFAULT_GROUP service-order 192.168.3.39:8000 register finished

```
-Dserver.port=8002
```

配置多个启动方式，观察nacos注册中心面板的变化

## Nacos服务治理

> java.net.UnknownHostException: service-store

在微服务调用的时候出现的，意思是找不到服务

添加loadBalance：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

注册 RestTemplate:
```java
@LoadBalanced
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

注册失败错误
> com.alibaba.nacos.api.exception.NacosException: failed to req API:/nacos/v1/ns/instance after all servers

重启nacos即可

## Nacos配置中心

> java.lang.IllegalArgumentException: Could not resolve placeholder 'env.ip' in value "${env.ip}"

出现这个错误是因为配置项不存在，那么我们应该给与一个默认值,在配置后面输入一个 : 类似于 ${env:ip:},那么久摩尔恩了一个空的字符串

![img.png](files/img.png)
### 基本配置
数据库配置：nacos/conf/application.properties  可以进行数据库的配置
- spring.datasource.platform=mysql
- db.num=1
- db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
- db.user.0=root
- db.password.0=123456

配置完成之后，新建数据库nacos，导入数据文件 nacos-mysql.sql

重启nacos

### 动态刷新
在Controller上面加上@RefreshScope

### dataID格式
${prefix}-${spring.profiles.active}.${file-extension}

### Group分组


### Namespace

namespace里面可以写多个dataID的文件，通过 spring.profile.active 可以区分不同的配置文件

`namespace - group - active` 可以组合出很多种配置

> 注意：在固定的服务里面， prefix必须是当前服务的名称，比如 service-goods，如果prefix改成其他的服务名称，则读取不到配置值

### 共享配置 shared-configs[0]
### extension-configs
