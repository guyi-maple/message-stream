package tech.guyi.component.message.stream.rabbitmq;

import com.rabbitmq.client.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import tech.guyi.component.message.stream.api.MessageStream;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;

import java.util.HashSet;
import java.util.Set;

/**
 * @author guyi
 * @date 2021/1/16 00:04
 */
public class RabbitmqMessageStream implements MessageStream {

    // 通道
    private Channel channel;
    // 连接
    private Connection connection;

    private String exchange;
    private String queue;

    private final Set<String> topics = new HashSet<>();

    /**
     * 建立连接
     * @param configuration 连接配置
     */
    @SneakyThrows
    public void connect(RabbitmqConfiguration configuration){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(configuration.getUsername());
        factory.setPassword(configuration.getPassword());
        factory.setVirtualHost(configuration.getVirtualHost());
        factory.setHost(configuration.getHost());
        factory.setPort(configuration.getPort());

        this.queue = configuration.getQueue();
        this.exchange = configuration.getExchange();

        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
    }

    @Override
    public @NonNull String getName() {
        return "rabbitmq";
    }

    @Override
    @SneakyThrows
    public void register(MessageConsumerEntry consumer) {
        channel.queueDeclare(this.queue, false, false, false, null);
        for (String topic : consumer.getTopic()) {
            topics.add(topic);
            channel.queueBind(this.queue, this.exchange, topic.replaceAll("/","."));
            channel.basicConsume(this.queue, true,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    Message message = new Message();
                    message.setStream(getName());
                    message.setContent(body);
                    message.setTopic(topic);
                    consumer.getReceiver().accept(message);
                }
            });
        }
    }

    @Override
    @SneakyThrows
    public void unregister(String topic) {
        topics.remove(topic);
        this.channel.queueUnbind(this.queue,this.exchange,topic.replaceAll("/","."));
    }

    @Override
    @SneakyThrows
    public void close() {
        topics.forEach(this::unregister);
        this.channel.close();
        this.connection.close();
    }


}
