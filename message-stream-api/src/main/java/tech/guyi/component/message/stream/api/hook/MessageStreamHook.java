package tech.guyi.component.message.stream.api.hook;

import tech.guyi.component.message.stream.api.entry.Message;

import java.util.List;

/**
 * 消息流钩子 <br />
 * 消息流各流程事件的回调 <br />
 * 消息消费者注册、消息发布钩子始终会被回调, 其他钩子是否被回调, 由消息流实现决定 <br />
 * 具体回调触发条件, 参见实现 <br />
 * @author guyi
 * @date 2021/1/18 17:40
 * @param <E>
 */
public interface MessageStreamHook<E> {

    // 消息消费者注册钩子
    MessageStreamHookType<List<String>,MessageConsumerRegisterHook> REGISTER = MessageStreamHookType.valueOf(MessageConsumerRegisterHook.class);
    // 发布消息钩子
    MessageStreamHookType<Message,MessagePublishHook> PUBLISH = MessageStreamHookType.valueOf(MessagePublishHook.class);

    void on(E entry);

}
