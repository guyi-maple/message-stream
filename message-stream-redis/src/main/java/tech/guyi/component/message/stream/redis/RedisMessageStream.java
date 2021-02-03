package tech.guyi.component.message.stream.redis;

import lombok.NonNull;
import redis.clients.jedis.Jedis;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基于Redis实现的消息流
 * @author guyi
 */
public class RedisMessageStream implements MessageStream {

    @Resource
    private RedisConfiguration configuration;

    // Redis客户端
    private Jedis jedis;
    // Redis消息订阅者
    private MessageStreamJedisPubSub pubSub;

    @Override
    public @NonNull String getName() {
        return "redis";
    }

    @Override
    public void close() {
        this.jedis.close();
    }

    @Override
    public void register(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        this.pubSub.subscribe(topic);
    }

    @Override
    public void unregister(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        this.pubSub.unsubscribe(topic);
    }

    @Override
    public void open(Consumer<Message> receiver) {
        this.jedis = new Jedis(
                configuration.getHost(),
                configuration.getPort(),
                configuration.getDatabase()
        );
        this.pubSub = new MessageStreamJedisPubSub((topic, content) ->
                receiver.accept(new Message(topic, content.getBytes(StandardCharsets.UTF_8))));
        this.jedis.subscribe(this.pubSub);
    }

    @Override
    public void publish(Message message) {
        this.jedis.publish(message.getTopic().getBytes(StandardCharsets.UTF_8), message.getBytes());
    }

}
