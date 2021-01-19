# message-stream-websocket

基于Websocket实现的消息流

## 启用

需要启用请在SpringBoot配置文件中添加配置 

``` yaml
message:
  stream:
    websocket:
      enable: true
```

## 配置

配置说明请参见源码注释

[基础配置: message.stream.websocket](./src/main/java/tech/guyi/component/message/stream/websocket/WebSocketConfiguration.java)

## 连接地址表达式

连接地址配置 <code>message.stream.websocket.server</code> 支持使用表达式, 表达式的值可以使用 [WebSocketServerExecutor](./src/main/java/tech/guyi/component/message/stream/websocket/executor/WebSocketServerExecutor.java) 进行设置。

表达式的格式为 <code>{*}</code>, 如：<code>ws://127.0.0.1/websocket?token={token}</code>

## Topic的提取与设置

Websocket中并没有Topic及相关概念, 实现中默认消息格式为JSON, 且存在topic字段。

如果实际业务中不支持此格式, 可以实现 [TopicHandler](./src/main/java/tech/guyi/component/message/stream/websocket/topic/TopicHandler.java) 完成自定义的Topic提取与设置逻辑。