package tech.guyi.component.message.stream.api.hook.defaults;

import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.entry.MessagePublishHookEntry;

/**
 * 消息发布钩子 <br />
 * 当向消息流中发布消息时执行回调 <br />
 * @author guyi
 * @date 2021/1/18 18:18
 */
public interface MessagePublishHook extends MessageStreamHook<MessagePublishHookEntry> {
}
