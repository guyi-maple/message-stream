package tech.guyi.component.message.stream.api.annotation.listener;

import tech.guyi.component.message.stream.api.attach.AttachKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>消费者附件参数提供者.</p>
 * <p>在基于注解的消费者创建中, 可以使用此注解向消息流实现传递附加参数.</p>
 * <p>支持在自定义注解中使用此注解, 具体用法参见 @GroupId</p>
 * @see GroupId
 * @author guyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface ConsumerAttachProvider {

    /**
     * 键名称
     * @see AttachKey
     * @return 键名称
     */
    Class<? extends AttachKey> key();

    /**
     * 要传入的值
     * @return 值
     */
    String value() default "";

}
