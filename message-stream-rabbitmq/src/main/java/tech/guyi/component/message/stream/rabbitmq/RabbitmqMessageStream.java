package tech.guyi.component.message.stream.rabbitmq;

import com.rabbitmq.client.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.rabbitmq.attach.ExchangeAttachKey;
import tech.guyi.component.message.stream.rabbitmq.attach.QueueAttachKey;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author guyi
 */
public class RabbitmqMessageStream implements MessageStream<Boolean> {

    // 通道
    private Channel channel;
    // 连接
    private Connection connection;
    // 消息接收者
    private Consumer<Message> receiver;

    @Resource
    private RabbitmqConfiguration configuration;

    // 替换Topic
    private String replaceTopic(String topic){
        return topic.replaceAll("/",".")
                .replaceAll("\\*\\*","#");
    }

    @Override
    public @NonNull String getName() {
        return "rabbitmq";
    }

    @Override
    @SneakyThrows
    public void close() {
        this.channel.close();
        this.connection.close();
    }


    /**
     * 当有新的消息Topic被注册时, 将消息Topic转为RouterKey, 与队列及路由器绑定
     * @see MessageStream#register(String, Map)
     * @param topic 消息主题
     * @param attach 消息消费者传递的额外参数
     */
    @Override
    @SneakyThrows
    public void register(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        String queue = Optional.ofNullable(attach.get(QueueAttachKey.class))
                .map(Object::toString)
                .orElse(configuration.getQueue());

        // 声明队列
        // 当队列在服务器中不存在时则被创建
        channel.queueDeclare(queue, false, false, false, null);

        channel.queueBind(
                queue,
                Optional.ofNullable(attach.get(ExchangeAttachKey.class))
                        .map(Objects::toString)
                        .orElse(configuration.getExchange()),
                this.replaceTopic(topic));

        channel.basicConsume(queue, true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                try {
                    receiver.accept(new Message(topic, body));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 当有消息Topic被取消注册时, 将消息Topic转为RouterKey, 与队列及路由器解除绑定
     * @see MessageStream#unregister(String, Map)
     * @param topic 消息主题
     * @param attach 消息消费者传递的额外参数
     */
    @Override
    @SneakyThrows
    public void unregister(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        channel.queueUnbind(
                Optional.ofNullable(attach.get(QueueAttachKey.class))
                        .map(Object::toString)
                        .orElse(configuration.getQueue()),
                Optional.ofNullable(attach.get(ExchangeAttachKey.class))
                        .map(Objects::toString)
                        .orElse(configuration.getExchange()),
                this.replaceTopic(topic));
    }

    @Override
    @SneakyThrows
    public void open(Consumer<Message> receiver) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(configuration.getUsername());
        factory.setPassword(configuration.getPassword());
        factory.setVirtualHost(configuration.getVirtualHost());
        factory.setHost(configuration.getHost());
        factory.setPort(configuration.getPort());

        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
        this.receiver = receiver;
    }

    @Override
    @SneakyThrows
    public Optional<Boolean> publish(Message message) {
        // topic转为routerKey
        String key = this.replaceTopic(message.getTopic());
        //获取交换机
        String exchange = Optional.ofNullable(message.getAttach().get(ExchangeAttachKey.class))
                .map(Objects::toString)
                .orElse(configuration.getExchange());
        this.channel.basicPublish(exchange,key,null,message.getBytes());
        return Optional.of(true);
    }

}
