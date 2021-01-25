package tech.guyi.component.message.stream.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>获取消息内容.</p>
 * <p>当监听器方法只有一个参数时无需使用此注解, 唯一参数将会默认注入消息内容.</p>
 * <p>使用此注解修饰方法参数, 消息内容将会注入到被修饰的参数上.</p>
 * <p>请确保被修饰的参数类型为 String、byte[] 获取其他拥有类型转换器的类型, 否则将会抛出异常.</p>
 * @author guyi
 * @see tech.guyi.component.message.stream.api.converter.MessageTypeConverter
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageContent {
}
