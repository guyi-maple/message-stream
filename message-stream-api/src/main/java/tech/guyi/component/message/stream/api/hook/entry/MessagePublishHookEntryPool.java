package tech.guyi.component.message.stream.api.hook.entry;

import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.configuration.MessageStreamConfiguration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 消息对象池
 * @author guyi
 * @date 2021/5/8 15:01
 */
@Component
public class MessagePublishHookEntryPool implements InitializingBean {

    @Resource
    private MessageStreamConfiguration configuration;

    private final BlockingDeque<MessagePublishHookEntry> deque = new LinkedBlockingDeque<>();

    @Override
    public void afterPropertiesSet() {
        for (int i = 0; i < configuration.getPublishHookPoolSize(); i++) {
            this.deque.add(new MessagePublishHookEntry());
        }
    }

    @SneakyThrows
    public MessagePublishHookEntry pack(String topic, byte[] bytes, Map<Class<? extends AttachKey>, Object> attach, List<String> stream, Object result) {
        MessagePublishHookEntry message = this.deque.take();
        message.pack(topic, bytes, attach, stream, result);
        return message;
    }

    public void roll(MessagePublishHookEntry entry) {
        entry.clear();
        this.deque.add(entry);
    }




}
