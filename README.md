# message-stream

为消息消费提供统一的API。

让切换不同的消息来源变得更简单。

根据上层业务无需变动代码, 只需修改配置即可做到切换消息的来源。

如： 将Rabbitmq切换为Kafka, 不需要修改业务侧代码, 只需修改配置即可切换。

还可支持多个消息来源的同时消费与发布。

如：同时消费来自Rabbitmq、Kafka、Websocket的消息, 同时向 Rabbitmq、Kafka、Websocket 发送消息。

随着下层消息流实现的增加, 可以实现更多的消息统一消费方式, 如文件、FTP、OSS、UDP、TCP等等, 终极目标是统一所有的IO输出。

[使用文档](./message-stream-api)

# 已支持消息流

* [邮件](./message-stream-email)
* [Kafka](./message-stream-kafka)
* [Rabbitmq](./message-stream-rabbitmq)
* [Redis](./message-stream-redis)
* [Websocket](./message-stream-websocket)