# message-stream
统一消息流API

## API

### 消息消费者接口

实现接口 [MessageConsumer](./message-stream-api/src/main/java/tech/guyi/component/message/stream/api/consumer/MessageConsumer.java) , 并将其注册到Spring容器中，即可监听消息。

``` java
@Component
public class MessageReceiver implements MessageConsumer {

    @Override
    public List<String> getTopic() {
        return Collections.singletonList("/test/123");
    }

    @Override
    public void accept(Message message) {
        System.out.println(new String(message.getContent()));
    }

}
```

### 基于注解监听消息

在Bean的公共方法上添加注解 [MessageListener](./message-stream-api/src/main/java/tech/guyi/component/message/stream/api/annotation/MessageListener.java) 即可监听消息。

``` java
@MessageListener(topic = "/test/*")
public void onMessage(Message message){
    System.out.println(message.getTopic());
    System.out.println(new String(message.getContent()));
}
```

## 自定义消息流

实现接口 [MessageStream](./message-stream-api/src/main/java/tech/guyi/component/message/stream/api/MessageStream.java) , 即可实现自己的消息流。

详细请参见实现 [RabbitmqMessageStream](./message-stream-rabbitmq/src/main/java/tech/guyi/component/message/stream/rabbitmq/RabbitmqMessageStream.java)