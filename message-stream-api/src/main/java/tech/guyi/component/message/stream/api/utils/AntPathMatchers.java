package tech.guyi.component.message.stream.api.utils;

import org.springframework.util.AntPathMatcher;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 匹配工具.
 * 多用于匹配消息流推送的消息Topic与消费者要消费的Topic是否匹配.
 * 内部使用线程安全的队列保证此工具的线程安全, 匹配器使用后需要调用roll方法, 将匹配器归还到队列中
 * @author guyi
 */
public class AntPathMatchers {

    // 匹配器队列
    private final Queue<AntPathMatcher> matchers = new ConcurrentLinkedQueue<>();

    /**
     * 获取匹配器.
     * 如果匹配器队列中没有匹配器, 则创建一个新的匹配器
     * @return 匹配器
     */
    public AntPathMatcher get(){
        return Optional.ofNullable(this.matchers.poll())
                .orElseGet(AntPathMatcher::new);
    }

    /**
     * 将匹配器压回队列中
     * @param matcher 匹配器
     */
    public void roll(AntPathMatcher matcher){
        this.matchers.add(matcher);
    }

    /**
     * 是否匹配
     * @param pattern 表达式
     * @param value 需要验证的值
     * @return 是否匹配
     */
    public boolean match(String pattern, String value){
        AntPathMatcher matcher = this.get();
        boolean match = matcher.match(pattern,value);
        this.roll(matcher);
        return match;
    }

}
