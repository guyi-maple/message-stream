package tech.guyi.component.message.stream.redis;

import lombok.AllArgsConstructor;
import redis.clients.jedis.JedisPubSub;

import java.util.function.BiConsumer;

/**
 * @author guyi
 */
@AllArgsConstructor
public class MessageStreamJedisPubSub extends JedisPubSub {

    private final BiConsumer<String,String> consumer;

    @Override
    public void onMessage(String channel, String message) {
        this.consumer.accept(channel,message);
    }
}
