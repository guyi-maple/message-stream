package tech.guyi.component.message.stream.api.annotation.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>监听消息的Topic.</p>
 * <p>只有当消息的Topic与注解指定的值匹配时才会触发.</p>
 * <p>支持使用模糊匹配.</p>
 * <p>此支持库的Topic概念与某些消息队列的Topic概念并不一致.</p>
 * <ul>
 *     <li>在Kafka中表示消息Key匹配</li>
 *     <li>在Rocketmq中表示消息的Tag匹配</li>
 * </ul>
 * <p>使用此注解指定的Topic值具体与消息的那个属性相匹配由消息流实现决定.</p>
 * @see org.springframework.util.AntPathMatcher
 * @author guyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Topic {

    /**
     * 指定Topic
     * @return Topic数组
     */
    String[] value();

}
