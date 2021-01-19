package tech.guyi.component.message.stream.api.hook;

import tech.guyi.component.message.stream.api.hook.defaults.MessageConsumerRegisterHook;
import tech.guyi.component.message.stream.api.hook.defaults.MessageConsumerUnRegisterHook;
import tech.guyi.component.message.stream.api.hook.defaults.MessagePublishHook;
import tech.guyi.component.message.stream.api.hook.entry.MessagePublishHookEntry;

import java.util.List;

/**
 * 消息流钩子 <br />
 * 消息流各流程事件的回调 <br />
 * @author guyi
 * @date 2021/1/18 17:40
 * @param <E>
 */
public interface MessageStreamHook<E> {

    // 消息消费者注册钩子
    MessageStreamHookType<List<String>, MessageConsumerRegisterHook> REGISTER = MessageStreamHookType.valueOf(MessageConsumerRegisterHook.class);
    // 消息消费者取消注册钩子
    MessageStreamHookType<String, MessageConsumerUnRegisterHook> UN_REGISTER = MessageStreamHookType.valueOf(MessageConsumerUnRegisterHook.class);
    // 发布消息钩子
    MessageStreamHookType<MessagePublishHookEntry, MessagePublishHook> PUBLISH = MessageStreamHookType.valueOf(MessagePublishHook.class);

    void on(E entry);

}
