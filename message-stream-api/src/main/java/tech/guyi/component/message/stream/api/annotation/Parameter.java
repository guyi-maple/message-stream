package tech.guyi.component.message.stream.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 监听器参数注解 <br />
 * 用于在基于注解注册监听器时的自定义参数传递
 * @author guyi
 * @date 2021/1/16 13:35
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

    /**
     * 键
     * @return 键
     */
    String key();

    /**
     * 值
     * @return 值
     */
    String value();

}
