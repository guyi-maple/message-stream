package tech.guyi.component.message.stream.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取消息流实现传递过来的附加信息.
 * 使用此注解修饰方法参数, 附加信息将会注入到被修饰的参数上.
 * 请确保被修饰的参数类型为 Map&lt;String,Object&gt;, 否则将会抛出异常
 * @author guyi
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageAttach {
}
