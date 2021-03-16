## 消息消费

使用注解 [@Subscribe](./src/main/java/tech/guyi/component/message/stream/api/annotation/listener/Subscribe.java) 修饰方法, 使其接收消息流收到的消息。

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.annotation.listener.Subscribe;

@Component
public class Test {

    @Subscribe
    public void test(String message) {
        System.out.println(message);
    }

}
```

此注解默认监听所有消息流的消息, 如果需要指定消息流, 可以添加`stream`参数。

```
@Subscribe(stream = {"kafka","redis"})
```

#### 设置监听属性

如果需要监听指定Topic的消息, 可以使用 [@Topic](./src/main/java/tech/guyi/component/message/stream/api/annotation/listener/Topic.java)

> 此Topic的含义在每个消息流中可能并不相同, 具体的意义可以参考消息流实现中的说明

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.annotation.listener.Subscribe;
import tech.guyi.component.message.stream.api.annotation.listener.Topic;

@Component
public class Test {

    @Subscribe
    @Topic("/test/message/topic")
    public void test(String message) {
        System.out.println(message);
    }

}
```

某些消息流可能有一些自定义的参数, 比如 `GroupId` 等, 此时可以使用 [@ConsumerAttachProvider](./src/main/java/tech/guyi/component/message/stream/api/annotation/listener/ConsumerAttachProvider.java) 进行传递

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.annotation.listener.ConsumerAttachProvider;
import tech.guyi.component.message.stream.api.annotation.listener.Subscribe;
import tech.guyi.component.message.stream.api.annotation.listener.Topic;

@Component
public class Test {

    @Subscribe
    @Topic("/test/message/topic")
    @ConsumerAttachProvider(key = GroupIdAttachKey.class, value = "test")
    public void test(String message) {
        System.out.println(message);
    }
}
```

默认提供了两个修饰注解 

* [@GroupId](./src/main/java/tech/guyi/component/message/stream/api/annotation/listener/GroupId.java) GroupId
* [@StreamTopic](./src/main/java/tech/guyi/component/message/stream/api/annotation/listener/StreamTopic.java) Kafka、Rocketmq等消息流中的Topic概念

#### 参数绑定

入参默认为消息内容, 且只能有一个入参, 如果需要获取到其他属性, 可以使用 [@MessageBind](./src/main/java/tech/guyi/component/message/stream/api/annotation/receiver/MessageBind.java)

```
@MessageBind(bind = MessageBindType.TOPIC)
```

默认提供了三个快捷使用的修饰注解

* [@AttachBind](./src/main/java/tech/guyi/component/message/stream/api/annotation/receiver/AttachBind.java) 绑定附加参数
* [@ContentBind](./src/main/java/tech/guyi/component/message/stream/api/annotation/receiver/ContentBind.java) 绑定消息内容
* [@TopicBind](./src/main/java/tech/guyi/component/message/stream/api/annotation/receiver/TopicBind.java) 绑定消息Topic