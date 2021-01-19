# message-stream-rabbitmq

基于Rabbitmq实现的消息流

## 启用

需要启用请在SpringBoot配置文件中添加配置 

``` yaml
message:
  stream:
    rabbitmq:
      enable: true
      host: <Rabbitmq服务器地址: 127.0.0.1>
      port: <Rabbitmq服务器端口: 5672>
      username: <用户名: guest>
      password: <密码: guest>
      queue: <队列名称: message.stream>
      exchange: <交换机名称: amq.topic>
      virtual-host: <虚拟主机: />
```

## 配置

配置说明请参见源码注释

[基础配置: message.stream.rabbitmq](./src/main/java/tech/guyi/component/message/stream/rabbitmq/RabbitmqAutoConfiguration.java)