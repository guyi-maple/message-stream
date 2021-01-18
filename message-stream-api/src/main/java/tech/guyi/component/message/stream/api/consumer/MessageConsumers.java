package tech.guyi.component.message.stream.api.consumer;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.utils.AntPathMatchers;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息消费者仓库
 * @author guyi
 * @date 2021/1/18 22:31
 */
public class MessageConsumers implements ApplicationListener<ContextRefreshedEvent> {

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
    private Map<String, MessageConsumer<?>> consumers = new HashMap<>();

    /**
     * 消息到达处理
     * @param topic Topic
     * @param stream 消息流名称
     * @param attach 附加信息
     * @param bytes 消息内容
     * @param consumer 消息消费者
     */
    private void onMessage(String topic, String stream, Map<String,Object> attach, byte[] bytes, MessageConsumer consumer){
        Class<?> type = consumer.messageType();
        this.worker.submit(() -> consumer.accept(this.converters.convert(bytes, type), topic, stream, attach));
    }

    /**
     * 消息到达处理
     * @param topic Topic
     * @param stream 消息流名称
     * @param attach 附加信息
     * @param bytes 消息内容
     */
    private void onMessage(String topic, String stream, Map<String,Object> attach, byte[] bytes){
        this.consumers.keySet().stream()
                .filter(key -> matchers.match(key,topic))
                .distinct()
                .map(consumers::get)
                .filter(consumer -> consumer.getStream() == null
                        || consumer.getStream().isEmpty()
                        || consumer.getStream().contains(stream))
                .forEach(consumer -> onMessage(topic,stream,attach,bytes,consumer));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 容器启动完成
        if (event.getApplicationContext().getParent() == null){
            // 打开所有流
            this.messageStreams.getStreams()
                    .forEach(stream -> this.worker.execute(() -> {
                        Set<String> topics = this.consumers.values()
                                .stream()
                                .filter(consumer -> consumer.getStream() == null
                                        || consumer.getStream().isEmpty()
                                        || consumer.getStream().contains(stream.getName()))
                                .map(MessageConsumer::getTopic)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet());
                        stream.open(topics).subscribe(message -> this.onMessage(
                                message.getTopic(),
                                stream.getName(),
                                message.getAttach(),
                                message.getBytes()));
                    }));
        }
    }

    /**
     * 注册消息消费者
     * @param consumer 消息消费者
     */
    public void register(MessageConsumer consumer){
        for (Object topic : consumer.getTopic()) {
            this.consumers.put(topic.toString(), consumer);
        }
        this.runner.run(MessageStreamHook.REGISTER, consumer.getTopic());
    }

}
