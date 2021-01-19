package tech.guyi.component.message.stream.api.consumer;

import lombok.extern.slf4j.Slf4j;
import tech.guyi.component.message.stream.api.converter.exception.NotFoundTypeConverterException;
import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.utils.AntPathMatchers;

import javax.annotation.Resource;
import java.util.*;

/**
 * 消息消费者仓库
 * @author guyi
 * @date 2021/1/18 22:31
 */
@Slf4j
public class MessageConsumers {

    @Resource
    private AntPathMatchers matchers;
    @Resource
    private MessageStreamWorker worker;
    @Resource
    private MessageStreams messageStreams;
    @Resource
    private MessageTypeConverters converters;
    @Resource
    private MessageStreamHookRunner runner;

    // 消费者集合
    private final Map<String, MessageConsumer<?>> consumers = new HashMap<>();

    /**
     * 消息到达处理
     * @param topic Topic
     * @param stream 消息流名称
     * @param attach 附加信息
     * @param bytes 消息内容
     * @param consumer 消息消费者
     */
    public void onMessage(String topic, String stream, Map<String,Object> attach, byte[] bytes, MessageConsumer consumer){
        Class<?> type = consumer.messageType();
        this.worker.submit(() -> {
            try{
                consumer.accept(this.converters.convert(bytes, type), topic, stream, attach);
            }catch (Exception e){
                log.error("消息消费异常", e);
            }
        });
    }

    /**
     * 消息到达处理
     * @param topic Topic
     * @param stream 消息流名称
     * @param attach 附加信息
     * @param bytes 消息内容
     */
    public void onMessage(String topic, String stream, Map<String,Object> attach, byte[] bytes){
        this.consumers.keySet().stream()
                .filter(key -> matchers.match(key,topic))
                .distinct()
                .map(consumers::get)
                .filter(consumer -> consumer.getStream() == null
                        || consumer.getStream().isEmpty()
                        || consumer.getStream().contains(stream))
                .forEach(consumer -> onMessage(topic,stream,attach,bytes,consumer));
    }

    /**
     * 注册消息消费者
     * @param consumer 消息消费者
     */
    public void register(MessageConsumer consumer){
        for (Object topic : consumer.getTopic()) {
            this.consumers.put(topic.toString(), consumer);
            this.messageStreams.register(topic.toString(), consumer.getStream());
        }

        // 回调消费者注册钩子
        this.runner.run(MessageStreamHook.REGISTER, consumer.getTopic());
    }

    /**
     * 取消消息消费者的注册
     * @param topic 消息主题
     */
    public void unregister(String topic){
        this.consumers.keySet().stream()
                .filter(topic::equals)
                .forEach(key -> {
                    MessageConsumer consumer = this.consumers.remove(key);
                    this.messageStreams.unregister(key, consumer.getStream());
                });

        // 回调消费者取消注册钩子
        this.runner.run(MessageStreamHook.UN_REGISTER, topic);

    }

}
