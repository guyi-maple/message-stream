package tech.guyi.component.message.stream.redis;

import lombok.NonNull;
import redis.clients.jedis.Jedis;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.api.stream.entry.PublishResult;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * 基于Redis实现的消息流
 * @author guyi
 * @date 2021/1/19 12:51
 */
public class RedisMessageStream implements MessageStream {

    @Resource
    private RedisConfiguration configuration;
    @Resource
    private MessageStreamWorker worker;

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
    public void register(String topic) {
        this.pubSub.subscribe(topic);
    }

    @Override
    public void unregister(String topic) {
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
    public Future<PublishResult> publish(Message message) {
        return this.worker.submit(() -> new PublishResult(
                Long.class,
                this.jedis.publish(message.getTopic().getBytes(StandardCharsets.UTF_8), message.getBytes())
        ));
    }

}
