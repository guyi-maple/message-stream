package tech.guyi.component.message.stream.api.utils;

import org.springframework.util.AntPathMatcher;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * @author guyi
 * @date 2021/1/18 22:46
 */
public class AntPathMatchers {

    // 匹配器队列
    private final Queue<AntPathMatcher> matchers = new LinkedList<>();

    /**
     * 获取匹配器 <br />
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
