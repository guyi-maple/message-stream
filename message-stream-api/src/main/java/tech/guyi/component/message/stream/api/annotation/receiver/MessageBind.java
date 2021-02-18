package tech.guyi.component.message.stream.api.annotation.receiver;

import tech.guyi.component.message.stream.api.enums.MessageBindType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>消息内容绑定.</p>
 * <p>通过bind绑定被修饰参数应该被注入的消息内容</p>
 * @author guyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface MessageBind {

    /**
     * 需要绑定的消息内容
     * @see MessageBindType
     * @return 绑定类型
     */
    MessageBindType bind();

}
