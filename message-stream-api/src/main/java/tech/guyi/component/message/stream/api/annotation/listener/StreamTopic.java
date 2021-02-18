package tech.guyi.component.message.stream.api.annotation.listener;

import org.springframework.core.annotation.AliasFor;
import tech.guyi.component.message.stream.api.attach.StreamTopicAttachKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>某些消息队列自身存在Topic的概念, 如： Kafka、Rocketmq等.</p>
 * <p>使用此注解, 可以指定消息流实现的Topic属性.</p>
 * @author guyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ConsumerAttachProvider(key = StreamTopicAttachKey.class)
public @interface StreamTopic {

    @AliasFor(annotation = ConsumerAttachProvider.class, attribute = "value")
    String value();

}
