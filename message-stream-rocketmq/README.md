# message-stream-rocketmq

基于Rocketmq实现的消息流

## 启用

需要启用请在SpringBoot配置文件中添加配置 

``` yaml
message:
  stream:
    rocketmq:
      enable: true
      name-server: 命名服务器地址
      topic: topic名称
      groupId: 组ID
```

## Aliyun

支持阿里云的Rocketmq社区版

``` yaml
message:
  stream:
    rocketmq:
      enable: true
      name-server: 命名服务器地址
      topic: topic名称
      groupId: 组ID
      aliyun:
        enable: true
        access-key: AccessKey
        secret-key: SecretKey
```

## 配置

配置说明请参见源码注释

* [基础配置: message.stream.rocketmq](./src/main/java/tech/guyi/component/message/stream/rocketmq/configuration/RocketmqConfiguration.java)
* [阿里云服务配置: message.stream.rocketmq.aliyun](./src/main/java/tech/guyi/component/message/stream/rocketmq/configuration/RocketmqAliyunConfiguration.java)