
## 消息消费

### 基于注解

使用注解 ([@StreamSubscribe](./src/main/java/tech/guyi/component/message/stream/api/annotation/StreamSubscribe.java)) 修饰方法, 即可将此方法注册为消息消费者。

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.annotation.StreamSubscribe;

@Component
public class Test {

    @StreamSubscribe(topic = "/test/**")
    public void onMessage(String message) {
        System.out.println("收到消息: " + message);
    }

}
```
被 <code>@StreamSubscribe</code> 修饰的方法只能有一个参数, 如果存在多个, 则需要使用注解修饰参数, 注明内容内型。

使用此注解方式进行消息消费, 请确保你的类已经放入Spring容器中。

#### 获取更详细的信息

除消息内容外, 如果还需要获取消息Topic、消息来源等内容, 可以使用注解修饰参数

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.annotation.MessageContent;
import tech.guyi.component.message.stream.api.annotation.StreamSubscribe;
import tech.guyi.component.message.stream.api.annotation.Topic;

@Component
public class Test {

    @StreamSubscribe(topic = "/test/**")
    public void onMessage(@MessageContent String message, @Topic String topic) {
        System.out.printf("收到消息 [%s]: %s\n", topic, message);
    }

}
```

* [@MessageContent](./src/main/java/tech/guyi/component/message/stream/api/annotation/MessageContent.java) 消息内容
* [@Topic](./src/main/java/tech/guyi/component/message/stream/api/annotation/Topic.java) 消息的Topic
* [@StreamName](./src/main/java/tech/guyi/component/message/stream/api/annotation/StreamName.java) 消息来源的消息流名称
* [@MessageAttach](./src/main/java/tech/guyi/component/message/stream/api/annotation/MessageAttach.java) 消息流传入的非标准附加信息

#### 指定消费的消息流

默认情况下会消费所有存在的消息流中的消息, 如果想要消费指定消息流的消息, 可以加入 <code>stream</code> 配置。

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.annotation.StreamSubscribe;

@Component
public class Test {

    @StreamSubscribe(topic = "/test/**", stream = {"websocket","rabbitmq"})
    public void onMessage(String message) {
        System.out.println("收到消息: " + message);
    }

}
```

上述代码表示只消费来自Websocket和Rabbitmq的消息。

### 实现接口

可以直接实现消费者接口, 实现消息的消费 [MessageConsumer](./src/main/java/tech/guyi/component/message/stream/api/consumer/MessageConsumer.java)

## 发布消息

提供消息发布工具 [MessageStreamPublisher](./src/main/java/tech/guyi/component/message/stream/api/utils/MessageStreamPublisher.java) 向消息流中发布消息。

```java
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.utils.MessageStreamPublisher;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class Test {

    @Resource
    private MessageStreamPublisher publisher;

    public void test() {
        // 向所有消息流发布消息
        this.publisher.publish("topic", "消息内容".getBytes(StandardCharsets.UTF_8));
        // 向websocket发布消息
        this.publisher.publish("topic", "消息内容".getBytes(StandardCharsets.UTF_8), null, Collections.singletonList("websocket"));
    }

}
```

更多的重载方法及说明, 请参见注释。

### 自定义消息类型

当消息的类型不为 String 或 byte[] 时, 可以实现接口 [MessageTypeConverter](src/main/java/tech/guyi/component/message/stream/api/converter/MessageTypeConverter.java) 进行类型转换。

拥有类型转换的自定义消息类型可以直接在消费和发布中使用。