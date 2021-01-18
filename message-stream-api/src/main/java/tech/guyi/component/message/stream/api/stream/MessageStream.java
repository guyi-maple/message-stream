package tech.guyi.component.message.stream.api.stream;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import java.util.Set;

/**
 * 消息流接口 <br />
 * 实现此接口,获取不同来源的消息
 * @author guyi
 * @date 2021/1/15 22:58
 */
public interface MessageStream {

    /**
     * 消息流名称 <br />
     * 用来标识不同的消息流, 不可重复或返回NULL
     * @return 消息流名称
     */
    @NonNull
    String getName();

    /**
     * 关闭消息流
     */
    void close();

    /**
     * 打开消息流
     * @return 消息
     */
    Flux<Message> open(Set<String> topics);

}
