package tech.guyi.component.message.stream.websocket.consumer;

import org.springframework.util.AntPathMatcher;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息消费者仓库
 * @author guyi
 * @date 2021/1/18 15:23
 */
public class ConsumerRepository {

    // Topic匹配器
    private final AntPathMatcher matcher = new AntPathMatcher();
    // 消息消费者集合
    private final List<MessageConsumerEntry> consumers = new LinkedList<>();

    /**
     * 添加消费者
     * @param consumer 消费者
     */
    public void add(MessageConsumerEntry consumer){
        this.consumers.add(consumer);
    }

    /**
     * 移除消费者
     * @param consumer 消费者
     */
    public void remove(MessageConsumerEntry consumer){
        this.consumers.remove(consumer);
    }

    /**
     * 根据Topic移除消费者
     * @param topic topic
     */
    public void remove(String topic){
        this.consumers.stream()
                .filter(consumer -> consumer.getTopic().stream().anyMatch(topic::equals))
                .collect(Collectors.toList())
                .forEach(consumer -> {
                    if (consumer.getTopic().size() == 1){
                        this.remove(consumer);
                    }else{
                        consumer.getTopic().remove(topic);
                    }
                });
    }

    /**
     * 接收到消息, 分发给消息消息消费者
     * @param message 消息
     */
    public void receive(Message message){
        this.consumers.stream()
                .filter(consumer -> consumer.getTopic().stream().anyMatch(t -> matcher.match(t, message.getTopic())))
                .forEach(consumer -> consumer.getReceiver().accept(message));
    }


}
