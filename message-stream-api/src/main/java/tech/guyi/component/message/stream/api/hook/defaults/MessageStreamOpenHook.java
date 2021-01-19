package tech.guyi.component.message.stream.api.hook.defaults;

import tech.guyi.component.message.stream.api.hook.MessageStreamHook;

import java.util.Set;

/**
 * 消息流打开钩子 <br />
 * 当打开所有存在的消息流时执行回调 <br />
 * @author guyi
 * @date 2021/1/18 18:18
 */
public interface MessageStreamOpenHook extends MessageStreamHook<Set<String>> {
}
