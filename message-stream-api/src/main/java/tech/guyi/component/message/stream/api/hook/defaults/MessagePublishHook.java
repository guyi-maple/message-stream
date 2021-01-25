package tech.guyi.component.message.stream.api.hook.defaults;

import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.entry.MessagePublishHookEntry;

/**
 * <p>消息发布钩子.</p>
 * <p>当向消息流中发布消息时执行回调.</p>
 * @author guyi
 */
public interface MessagePublishHook extends MessageStreamHook<MessagePublishHookEntry> {
}
