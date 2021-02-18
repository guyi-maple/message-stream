package tech.guyi.component.message.stream.kafka;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.attach.GroupIdAttachKey;
import tech.guyi.component.message.stream.api.attach.StreamTopicAttachKey;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.kafka.attach.*;
import tech.guyi.component.message.stream.kafka.configuration.ConfigurationType;
import tech.guyi.component.message.stream.kafka.configuration.KafkaConfiguration;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author guyi
 */
@Slf4j
public class KafkaMessageStream implements MessageStream {

    @Resource
    private MessageStreamWorker worker;
    @Resource
    private KafkaConfiguration configuration;

    // 生产者
    private KafkaProducer<String,byte[]> producer;
    // 消费者集合
    private final Map<String, KafkaConsumer<String,byte[]>> consumers = new HashMap<>();
    // 是否消息信息
    private boolean run;

    // 消息接收者
    private Consumer<Message> receiver;

    @Override
    public @NonNull String getName() {
        return "kafka";
    }

    // 打开生产者连接
    private void openProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", configuration.getBootstrapServers(ConfigurationType.PRODUCER));
        props.put("acks", configuration.getProducer().getAcks());
        props.put("retries", configuration.getProducer().getRetries());
        props.put("batch.size", configuration.getProducer().getBatchSize());
        props.put("key.serializer", configuration.getProducer().getKeySerializer());
        props.put("value.serializer", configuration.getProducer().getValueSerializer());
        this.producer = new KafkaProducer<>(props);
    }

    // 创建消费者连接
    private KafkaConsumer<String,byte[]> createConsumer(Map<Class<? extends AttachKey>, Object> attach){
        Properties props = new Properties();

        //设置属性, 当Attach中存在时使用, 否则使用全局配置
        props.put("bootstrap.servers", Optional.ofNullable(attach.get(BootstrapServerAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getBootstrapServers(ConfigurationType.CONSUMER)));
        props.put("group.id", Optional.ofNullable(attach.get(GroupIdAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getConsumer().getGroupId()));
        props.put("enable.auto.commit", Optional.ofNullable(attach.get(AutoCommitAttachKey.class))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(configuration.getConsumer().isAutoCommit()));
        props.put("auto.commit.interval.ms",Optional.ofNullable(attach.get(CommitIntervalAttachKey.class))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(configuration.getConsumer().getInterval()));
        props.put("session.timeout.ms", Optional.ofNullable(attach.get(TimeoutAttachKey.class))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(configuration.getConsumer().getTimeout()));
        props.put("heartbeat.interval.ms", Optional.ofNullable(attach.get(HeartbeatIntervalAttachKey.class))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(configuration.getConsumer().getHeartbeat()));
        props.put("max.poll.records", Optional.ofNullable(attach.get(MaxRecordsAttachKey.class))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(configuration.getConsumer().getMaxRecords()));
        props.put("auto.offset.reset", Optional.ofNullable(attach.get(OffsetResetAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getConsumer().getReset()));
        props.put("key.deserializer", Optional.ofNullable(attach.get(KeyDeserializerAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getConsumer().getKeyDeserializer()));
        props.put("value.deserializer", Optional.ofNullable(attach.get(ValueDeserializerAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getConsumer().getKeyDeserializer()));

        KafkaConsumer<String,byte[]> consumer = new KafkaConsumer<>(props);

        // 如果附加配置中存在StreamTopic则使用, 否则使用全局配置
        consumer.subscribe(Optional.ofNullable(attach.get(StreamTopicAttachKey.class))
                .map(Object::toString)
                .map(Collections::singletonList)
                .orElse(configuration.getConsumer().getTopic()));

        this.run = true;

        return consumer;
    }

    // 拉取消息
    // 线程池中执行
    private void pullMessage(KafkaConsumer<String,byte[]> consumer){
        while (this.run){
            try{
                try{
                    consumer.poll(Duration.ofMillis(configuration.getConsumer().getInterval()))
                            .forEach(record -> this.receiver.accept(new Message(record.key(),record.value())));
                }catch (WakeupException wakeupException){}
            }catch (Exception e) {
                log.error("Kafka消息拉取异常", e);
            }
        }
        consumer.close();
    }

    @Override
    public void close() {
        // 让消费者线程跳出死循环, 关闭消费者
        this.run = false;
        // 关闭生产者
        this.producer.close();
        // 清空消费者集合
        this.consumers.clear();
    }

    @Override
    public void open(Consumer<Message> receiver) {
        // 打开生产者连接
        this.openProducer();
        // 缓存消息接收者
        this.receiver = receiver;
    }

    /**
     * <p>注册新的消息Topic</p>
     * <p>消息Topic将会与Kafka消息中的Key进行匹配</p>
     * @param topic 消息主题
     * @param attach 消息消费者传递的额外参数
     */
    @Override
    public void register(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        String groupId = Optional.ofNullable(attach.get(GroupIdAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getConsumer().getGroupId());
        if (!this.consumers.containsKey(groupId)){
            KafkaConsumer<String,byte[]> consumer = this.createConsumer(attach);
            this.consumers.put(groupId, consumer);
            this.worker.submit(() -> this.pullMessage(consumer));
        }
    }

    @Override
    public void publish(Message message) {
        // 如果消息中存在Topic配置则使用, 否则使用全局配置
        String streamTopic = Optional.ofNullable(message.getAttach().get(StreamTopicAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getProducer().getTopic());
        this.producer.send(new ProducerRecord<>(streamTopic, message.getTopic(), message.getBytes()));
    }

}
