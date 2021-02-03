package tech.guyi.component.message.stream.api.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author guyi
 */
@Data
@AllArgsConstructor
public class MessageConsumerEntry {

    /**
     * 消息流名称集合
     */
    private List<String> streams;

    /**
     * 消息消费者消息到达处理
     */
    private Consumer<ReceiveMessageEntry> consumer;

    /**
     * 消息消费者附加参数
     */
    private Map<Class<? extends AttachKey>,Object> attach;

    /**
     * 消息流匹配
     */
    private String streamPattern;

    public MessageConsumerEntry(List<String> streams, Consumer<ReceiveMessageEntry> consumer, Map<Class<? extends AttachKey>,Object> attach) {
        this.streams = streams;
        this.consumer = consumer;
        this.attach = attach;
    }

    /**
     * 移除消息流
     * @param streamName 要移除的消息流名称
     */
    public void removeStream(String streamName){
        Optional.ofNullable(this.streams)
                .ifPresent(ss -> {
                    ss.remove(streamName);
                    this.streamPattern = null;
                });
    }

    /**
     * 匹配消息流名称是否在消费者监听的范围内
     * @param streamName 消息流名称
     * @return 是否在消费者监听的范围内
     */
    public boolean matches(String streamName){
        if (ObjectUtils.isEmpty(this.streamPattern)){
            this.streamPattern = Optional.ofNullable(this.streams)
                    .filter(ss -> !ss.isEmpty())
                    .map(ss -> ss.stream().reduce("",(p, s) -> String.format("%s|%s", p,s)))
                    .map(pattern -> pattern.substring(1))
                    .orElse("*");
        }
        return Pattern.matches(this.streamPattern, streamName);
    }

}
