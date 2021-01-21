package tech.guyi.component.message.stream.api.hook.defaults;

import tech.guyi.component.message.stream.api.hook.MessageStreamHook;

import java.util.Set;

/**
 * 消息流打开钩子.
 * 当打开所有存在的消息流时执行回调.
 * @author guyi
 */
public interface MessageStreamOpenHook extends MessageStreamHook<Set<String>> {
}
