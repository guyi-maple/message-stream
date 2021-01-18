package tech.guyi.component.message.stream.rabbitmq;

import com.rabbitmq.client.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * @author guyi
 * @date 2021/1/16 00:04
 */
public class RabbitmqMessageStream implements MessageStream, InitializingBean {

    // 通道
    private Channel channel;
    // 连接
    private Connection connection;

    @Resource
    private RabbitmqConfiguration configuration;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.connect();
    }

    /**
     * 建立连接
     */
    public void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(configuration.getUsername());
        factory.setPassword(configuration.getPassword());
        factory.setVirtualHost(configuration.getVirtualHost());
        factory.setHost(configuration.getHost());
        factory.setPort(configuration.getPort());

        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
    }

    @Override
    public @NonNull String getName() {
        return "rabbitmq";
    }

//    @Override
//    @SneakyThrows
//    public void publish(Message message) {
//        String key = message.getTopic().replaceAll("/",".");
//        this.channel.basicPublish(configuration.getExchange(),key,null,message.getContent());
//    }

//    @Override
//    @SneakyThrows
//    public void register(MessageConsumerEntry consumer) {
//        channel.queueDeclare(configuration.getQueue(), false, false, false, null);
//        for (String topic : consumer.getTopic()) {
//            topics.add(topic);
//            channel.queueBind(configuration.getQueue(), configuration.getExchange(), topic.replaceAll("/","."));
//            channel.basicConsume(configuration.getQueue(), true,new DefaultConsumer(channel){
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
//                    Message message = new Message();
//                    message.setStream(getName());
//                    message.setContent(body);
//                    message.setTopic(topic);
//                    message.setAddress(configuration.getHost());
//                    consumer.getReceiver().accept(message);
//                }
//            });
//        }
//    }

//    @Override
//    @SneakyThrows
//    public void unregister(String topic) {
//        topics.remove(topic);
//        this.channel.queueUnbind(configuration.getQueue(),configuration.getExchange(),topic.replaceAll("/","."));
//    }

    @Override
    @SneakyThrows
    public void close() {
//        topics.forEach(this::unregister);
        this.channel.close();
        this.connection.close();
    }

    @SneakyThrows
    private void start(FluxSink<Message> sink,Set<String> topics){
        channel.queueDeclare(configuration.getQueue(), false, false, false, null);
        for (String topic : topics) {
            topics.add(topic);
            channel.queueBind(configuration.getQueue(), configuration.getExchange(), topic.replaceAll("/","."));
            channel.basicConsume(configuration.getQueue(), true,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    sink.next(new Message(topic, body));
                }
            });
        }
    }

    @Override
    public Flux<Message> open(Set<String> topics) {
        return Flux.create(sink -> this.start(sink,topics));
    }


}
