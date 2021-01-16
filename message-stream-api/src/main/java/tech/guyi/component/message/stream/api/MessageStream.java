package tech.guyi.component.message.stream.api;

import lombok.NonNull;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;

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
     * 发布消息
     * @param message 要发布的消息
     */
    void publish(Message message);

    /**
     * 注册一个消息消费者 <br />
     * @param consumer 消息消费者实体
     * @return 注册ID
     */
    void register(MessageConsumerEntry consumer);

    /**
     * 取消注册
     * @param topic 主题
     */
    void unregister(String topic);

    /**
     * 关闭消息流
     */
    void close();



}
