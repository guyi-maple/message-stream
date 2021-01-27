package tech.guyi.component.message.stream.rocketmq;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqAliyunConfiguration;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqConfiguration;
import tech.guyi.component.message.stream.rocketmq.creatoe.AliyunRocketmqCreator;
import tech.guyi.component.message.stream.rocketmq.creatoe.DefaultRocketmqCreator;
import tech.guyi.component.message.stream.rocketmq.creatoe.RocketmqCreator;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Rocketmq消息流实现
 * @author guyi
 */
public class RocketmqMessageStream implements MessageStream, InitializingBean {

    // 普通消息消费者集合
    private final Map<String, DefaultMQPushConsumer> consumers = new HashMap<>();

    // Rocketmq消费与生产创建者
    private RocketmqCreator creator;

    // 消息到达处理
    private Consumer<Message> receiver;

    // 消息生产者
    private DefaultMQProducer producer;

    @Resource
    private ApplicationContext context;
    @Resource
    private RocketmqConfiguration configuration;
    @Resource
    private RocketmqAliyunConfiguration aliyunConfiguration;

    @Override
    public @NonNull String getName() {
        return "rocketmq";
    }

    @Override
    public void afterPropertiesSet() {
        // 如果启用了阿里云配置
        if (aliyunConfiguration.isEnable()){
            this.creator = this.context.getBean(AliyunRocketmqCreator.class);
        }else{
            creator = this.context.getBean(DefaultRocketmqCreator.class);
        }
    }

    @Override
    public void close() {
        this.consumers.values().forEach(DefaultMQPushConsumer::shutdown);
    }

    @Override
    @SneakyThrows
    public void register(String topic, Map<String, Object> attach) {
        // 获取Rocketmq的Topic
        // 此Topic具体说明参见 RocketmqConfiguration.topic 的说明
        String rocketTopic = Optional.ofNullable(attach)
                .map(a -> a.get("topic"))
                .map(Object::toString)
                .orElse(configuration.getTopic());

        if (!this.consumers.containsKey(rocketTopic)){
            DefaultMQPushConsumer consumer = this.creator.createConsumer();
            consumer.setNamesrvAddr(configuration.getNameServer());

            consumer.subscribe(rocketTopic, "*");

            consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
                messages.stream()
                        .map(ext -> new Message(ext.getTags(),ext.getBody()))
                        .forEach(receiver);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            // 放入集合
            this.consumers.put(rocketTopic,consumer);

            consumer.start();
        }
    }

    @Override
    @SneakyThrows
    public void open(Consumer<Message> receiver) {
        this.receiver = receiver;

        // 创建并启动生产者
        this.producer = this.creator.createProducer();
        this.producer.setNamesrvAddr(configuration.getNameServer());
        this.producer.start();
    }

    @Override
    @SneakyThrows
    public void publish(Message message) {
        org.apache.rocketmq.common.message.Message mess = new org.apache.rocketmq.common.message.Message();
        mess.setTopic(
                Optional.ofNullable(message.getAttach())
                        .map(a -> a.get("topic"))
                        .map(Object::toString)
                        .orElse(configuration.getTopic())
        );
        mess.setTags(message.getTopic());
        mess.setBody(message.getBytes());
        this.producer.send(mess);
    }

}
