package tech.guyi.component.message.stream.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息流仓库
 * @author guyi
 * @date 2021/1/16 12:58
 */
public class MessageStreamRepository implements InitializingBean {

    // 消息流集合
    private final Map<String,MessageStream> streams = new HashMap<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageStream.class)
                .values()
                .forEach(stream -> streams.put(stream.getName(),stream));
    }

    /**
     * 添加消息流
     * @param stream 消息流
     */
    public void add(MessageStream stream){
        this.streams.put(stream.getName(),stream);
    }

    /**
     * 注册消息消费者
     * @param consumer 消息消费者
     */
    public void register(MessageConsumerEntry consumer){
        // 获取需要注册的消息流
        Collection<MessageStream> streams = Optional.ofNullable(consumer.getStream())
                .filter(names -> !names.isEmpty())
                .map(names ->
                        this.streams.values()
                                .stream()
                                .filter(stream -> names.contains(stream.getName()))
                                .collect(Collectors.toList()))
                .map(list -> (Collection<MessageStream>) list)
                .orElse(this.streams.values());

        // 消费者注册到消息流
        streams.forEach(stream -> stream.register(consumer));
    }

}
