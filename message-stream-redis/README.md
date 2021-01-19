# message-stream-redis

基于Redis实现的消息流

## 启用

需要启用请在SpringBoot配置文件中添加配置 

``` yaml
message:
  stream:
    redis:
      enable: true
```

## 配置

配置说明请参见源码注释

[基础配置: message.stream.redis](./src/main/java/tech/guyi/component/message/stream/redis/RedisConfiguration.java)