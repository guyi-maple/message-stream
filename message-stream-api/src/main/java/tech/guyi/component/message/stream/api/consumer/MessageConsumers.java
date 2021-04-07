package tech.guyi.component.message.stream.api.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;
import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.utils.AntPathMatchers;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 消息消费者仓库
 * @author guyi
 */
@Slf4j
public class MessageConsumers implements InitializingBean {

    @Resource
    private AntPathMatchers matchers;
    @Resource
    private MessageStreamWorker worker;
    @Resource
    private MessageStreams messageStreams;
    @Resource
    private MessageStreamHookRunner runner;

    // 消费者集合
    private final Map<String, List<MessageConsumerEntry>> consumers = new HashMap<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageConsumer.class)
                .values()
                .forEach(consumer -> this.register(consumer.topics(), consumer, consumer.attach(), consumer.streams()));
    }

    /**
     * 消息到达处理
     * @param topic Topic
     * @param stream 消息流名称
     * @param attach 附加信息
     * @param bytes 消息内容
     * @param entry 消息消费者实体
     */
    public void onMessage(String topic, String stream, Map<Class<? extends AttachKey>,Object> attach, byte[] bytes, MessageConsumerEntry entry){
        this.worker.submit(() -> {
            try{
                entry.getConsumer().accept(new ReceiveMessageEntry(bytes,topic,stream, attach));
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
    public void onMessage(String topic, String stream, Map<Class<? extends AttachKey>,Object> attach, byte[] bytes){
        this.consumers.keySet().stream()
                .filter(key -> matchers.match(key,topic))
                .map(consumers::get)
                .flatMap(Collection::stream)
                .filter(entry -> entry.matches(stream))
                .forEach(entry -> onMessage(topic,stream,attach,bytes,entry));
    }

    public void register(List<String> topics, Consumer<ReceiveMessageEntry> consumer, Map<Class<? extends AttachKey>,Object> attach, List<String> streamNames){
        if (streamNames == null || streamNames.isEmpty()){
            streamNames = this.messageStreams.all().stream().map(MessageStream::getName).collect(Collectors.toList());
        }

        MessageConsumerEntry entry = new MessageConsumerEntry(streamNames, consumer, attach);

        // 注册消费者并通知消息流
        if (topics != null && !topics.isEmpty()){
            for (String topic : topics) {
                List<MessageConsumerEntry> entries = Optional.ofNullable(this.consumers.get(topic))
                        .orElseGet(LinkedList::new);
                entries.add(entry);
                this.consumers.put(topic, entries);
                messageStreams.filter(streamNames).forEach(stream -> stream.register(topic, attach));
            }

            // 回调消费者注册钩子
            this.runner.run(MessageStreamHook.REGISTER, topics);
        }
    }

    public void unregister(String topic, List<String> streamNames){
        // 根据给定的Topic和消息流名称集合获取需要被取消注册的消费者
        // 如果给定的消息流名称集合为NULL或空集合, 则返回所有Topic匹配的消费者
        List<MessageConsumerEntry> entries = this.consumers.getOrDefault(topic, Collections.emptyList())
                .stream()
                .filter(entry -> Optional.ofNullable(streamNames)
                        .filter(ss -> !ss.isEmpty())
                        .map(ss -> entry.getStreams().stream().anyMatch(ss::contains))
                        .orElse(true))
                .collect(Collectors.toList());

        // 根据给定的消息流名称集合获取消息流集合
        List<MessageStream<Object>> streams = this.messageStreams.filter(streamNames);
        // 通知消息流取消Topic的注册
        entries.forEach(entry -> streams.forEach(stream -> stream.unregister(topic, entry.getAttach())));

        // 在消费者实体中删除本次取消注册的消息流名称
        streams.forEach(stream -> entries.forEach(entry -> entry.removeStream(stream.getName())));

        // 删除消息流名称集合已经为空的消费者实体
        Optional.ofNullable(this.consumers.get(topic))
                .ifPresent(es -> {
                    this.consumers.put(
                            topic,
                            es.stream()
                                    .filter(e -> !e.getStreams().isEmpty())
                                    .collect(Collectors.toList())
                    );
                    // 回调消费者取消注册钩子
                    this.runner.run(MessageStreamHook.UN_REGISTER, topic);
                });
    }

}
