## OucFly-Rest

> OucFly的Rest调用方式，用法跟原生OucFly差不多，只是启动了一个服务器，详细用法见：https://github.com/zqx123321/OucFly

### 使用方式

```java
java -jar oucfly-rest.jar
```

### 属性配置

此服务器基于SpringBoot，也就是说SpringBoot的所有配置方式均支持，最简单的方式就是在 java -jar oucfly-rest.jar 后面直接加参数，也可以在同目录下建立一个application.properties或者application.yml

配置项如下：

```properties
#服务器端口，默认8080
server.port=8080
#服务器上下文路径，默认/
server.servlet.context-path=/
#token超时时间，默认30分钟
oucfly.limit.time=1800000
#工作线程数，默认4
oucfly.thread=4
#工作模式，默认全功率模式
oucfly.mode=all
```

可以这样修改：

```
java -jar oucfly-rest.jar --server.port=888 --oucfly.thread=6
```

### 文档查看

启动服务之后，输入docs.html即可查看接口文档，如下图所示：

![img](/img.png)