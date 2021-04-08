package tech.guyi.component.message.stream.api.consumer;

import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 消息消费者
 * @author guyi
 * @date 2021/4/7 22:48
 */
public interface MessageConsumer extends Consumer<ReceiveMessageEntry> {

    default List<String> topics() {
        return Collections.singletonList("*");
    }

    default Map<Class<? extends AttachKey>,Object> attach() {
        return Collections.emptyMap();
    }

    default List<String> streams() {
        return null;
    }

}
