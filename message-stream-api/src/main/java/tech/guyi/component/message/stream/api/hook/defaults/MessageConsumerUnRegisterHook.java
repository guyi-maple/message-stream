package tech.guyi.component.message.stream.api.hook.defaults;

import tech.guyi.component.message.stream.api.hook.MessageStreamHook;

/**
 * 消息消费者取消注册钩子.
 * 当消息消费者被注册时回调
 * @author guyi
 */
public interface MessageConsumerUnRegisterHook extends MessageStreamHook<String> {
}
