package tech.guyi.component.message.stream.api.hook;

import tech.guyi.component.message.stream.api.hook.defaults.MessageConsumerRegisterHook;
import tech.guyi.component.message.stream.api.hook.defaults.MessageConsumerUnRegisterHook;
import tech.guyi.component.message.stream.api.hook.defaults.MessagePublishHook;
import tech.guyi.component.message.stream.api.hook.defaults.MessageStreamOpenHook;
import tech.guyi.component.message.stream.api.hook.entry.MessagePublishHookEntry;

import java.util.List;
import java.util.Set;

/**
 * <p>消息流钩子.</p>
 * <p>消息流各流程事件的回调.</p>
 * @author guyi
 * @param <E> 执行参数类型
 */
public interface MessageStreamHook<E> {

    /**
     * 消息消费者注册钩子
     */
    MessageStreamHookType<List<String>, MessageConsumerRegisterHook> REGISTER = MessageStreamHookType.valueOf(MessageConsumerRegisterHook.class);

    /**
     * 消息消费者取消注册钩子
     */
    MessageStreamHookType<String, MessageConsumerUnRegisterHook> UN_REGISTER = MessageStreamHookType.valueOf(MessageConsumerUnRegisterHook.class);

    /**
     * 发布消息钩子
     */
    MessageStreamHookType<MessagePublishHookEntry, MessagePublishHook> PUBLISH = MessageStreamHookType.valueOf(MessagePublishHook.class);

    /**
     * 消息流打开钩子
     */
    MessageStreamHookType<Set<String>, MessageStreamOpenHook> STREAM_OPEN = MessageStreamHookType.valueOf(MessageStreamOpenHook.class);

    /**
     * 当钩子被执行时调用
     * @param entry 执行参数
     */
    void on(E entry);

}
