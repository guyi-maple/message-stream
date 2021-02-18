package tech.guyi.component.message.stream.api.annotation.listener;

import org.springframework.core.annotation.AliasFor;
import tech.guyi.component.message.stream.api.attach.GroupIdAttachKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 某些消息流的实现有分组的概念, 可以使用此注解指定GroupId的值.
 * @author guyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ConsumerAttachProvider(key = GroupIdAttachKey.class)
public @interface GroupId {

    /**
     * 分组ID的值
     * @return 分组ID
     */
    @AliasFor(annotation = ConsumerAttachProvider.class, attribute = "value")
    String value();

}
