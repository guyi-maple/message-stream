# message-stream-kafka

基于Kafka实现的消息流

## 启用

需要启用请在SpringBoot配置文件中添加配置 

``` yaml
message:
  stream:
    kafka:
      enable: true
      bootstrap-servers: <Kafka服务器地址>
```

## 配置

配置说明请参见源码注释

* [基础配置: message.stream.kafka](./src/main/java/tech/guyi/component/message/stream/kafka/configuration/KafkaConfiguration.java)
* [生产者配置: message.stream.kafka.producer](./src/main/java/tech/guyi/component/message/stream/kafka/configuration/KafkaProducerConfiguration.java)
* [消费者配置: message.stream.kafka.consumer](./src/main/java/tech/guyi/component/message/stream/kafka/configuration/KafkaConsumerConfiguration.java)