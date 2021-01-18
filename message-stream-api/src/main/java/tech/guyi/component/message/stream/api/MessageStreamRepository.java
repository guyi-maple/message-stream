package tech.guyi.component.message.stream.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;
import tech.guyi.component.message.stream.api.hook.MessageConsumerRegisterHook;
import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;

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
    private MessageStreamHookRunner runner;

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

        // 回调注册钩子
        this.runner.run(MessageStreamHook.REGISTER, consumer);
    }

    /**
     * 向消息流中发布消息 <br />
     * 如果消息实体的stream不为空, 则会向stream指定的消息流发布消息 <br />
     * 如果消息实体中stream指定的消息流不存在, 则会丢弃该消息
     * @param message 要发布的消息
     */
    public void publish(Message message){
        // 获取到需要发布的消息流, 并发布消息
        Optional.ofNullable(message.getStream())
                .filter(name -> !ObjectUtils.isEmpty(name))
                .map(names ->
                        this.streams.values()
                                .stream()
                                .filter(stream -> names.contains(stream.getName()))
                                .collect(Collectors.toList()))
                .map(list -> (Collection<MessageStream>) list)
                .orElse(this.streams.values())
                .forEach(stream -> stream.publish(message));

        // 回调消息发布钩子
        this.runner.run(MessageStreamHook.PUBLISH, message);
    }

}
