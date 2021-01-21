package tech.guyi.component.message.stream.api.stream;

import lombok.NonNull;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 消息流接口.
 * 实现此接口,获取不同来源的消息
 * @author guyi
 */
public interface MessageStream {

    /**
     * 消息流名称.
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
     * 注册消息主题
     * @param topic 消息主题
     */
    void register(String topic);

    /**
     * 取消消息主题的注册
     * @param topic 消息主题
     */
    void unregister(String topic);

    /**
     * 打开消息流
     * @param receiver 消息接收者
     */
    void open(Consumer<Message> receiver);

    /**
     * 发布消息
     * @param message 消息实体
     */
    void publish(Message message);

}
