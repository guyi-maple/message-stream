package tech.guyi.component.message.stream.api.hook.defaults;

import tech.guyi.component.message.stream.api.hook.MessageStreamHook;

import java.util.List;

/**
 * 消息消费者注册钩子.
 * 当消息消费者被注册时回调
 * @author guyi
 */
public interface MessageConsumerRegisterHook extends MessageStreamHook<List<String>> {
}
