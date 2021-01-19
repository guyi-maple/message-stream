package tech.guyi.component.message.stream.rabbitmq;

import com.rabbitmq.client.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * @author guyi
 * @date 2021/1/16 00:04
 */
public class RabbitmqMessageStream implements MessageStream {

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

    @Override
    @SneakyThrows
    public void register(String topic) {
        channel.queueBind(configuration.getQueue(), configuration.getExchange(), this.replaceTopic(topic));
        channel.basicConsume(configuration.getQueue(), true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                receiver.accept(new Message(topic, body));
            }
        });
    }

    @Override
    @SneakyThrows
    public void unregister(String topic) {
        channel.queueUnbind(configuration.getQueue(),configuration.getExchange(), this.replaceTopic(topic));
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
        channel.queueDeclare(configuration.getQueue(), false, false, false, null);
    }

    @Override
    @SneakyThrows
    public void publish(Message message) {
        String key = this.replaceTopic(message.getTopic());
        this.channel.basicPublish(configuration.getExchange(),key,null,message.getBytes());
    }

}
