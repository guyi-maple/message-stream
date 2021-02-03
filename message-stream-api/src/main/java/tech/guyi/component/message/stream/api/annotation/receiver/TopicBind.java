package tech.guyi.component.message.stream.api.annotation.receiver;

import tech.guyi.component.message.stream.api.enums.MessageBindType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定为Topic
 * @author guyi
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@MessageBind(bind = MessageBindType.TOPIC)
public @interface TopicBind {
}
