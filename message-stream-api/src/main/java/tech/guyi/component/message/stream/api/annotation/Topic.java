package tech.guyi.component.message.stream.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>获取消息主题.</p>
 * <p>使用此注解修饰方法参数, 消息主题将会注入到被修饰的参数上.</p>
 * <p>请确保被修饰的参数类型为 String, 否则将会抛出异常</p>
 * @author guyi
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Topic {
}
