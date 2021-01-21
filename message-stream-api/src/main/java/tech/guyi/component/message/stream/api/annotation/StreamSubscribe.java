package tech.guyi.component.message.stream.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息监听注解.
 * 将此注解放置与方法上, 被注解的方法将会被注册为消息监听者.
 * 此注解只有放置在被public修改的方法上才会生效.
 * 此注解只有放置在Spring容器Bean中的方法上才会生效.
 * @author guyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StreamSubscribe {

    /**
     * 要监听的消息主题
     * @return 消息主题
     */
    String[] topic();

    /**
     * 指定要注册到的消息流.
     * 为NULL或空数组表示注册到所有消息流
     * @return 消息流名称
     */
    String[] stream() default {};

    /**
     * 自定义参数
     * @return 自定义参数
     */
    Parameter[] params() default {};

}
