package tech.guyi.component.message.stream.api.hook.defaults;

import lombok.NonNull;
import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.entry.MessagePublishHookEntry;

/**
 * <p>消息发布钩子.</p>
 * <p>当向消息流中发布消息时执行回调.</p>
 * @author guyi
 */
public interface MessagePublishHook extends MessageStreamHook<MessagePublishHookEntry> {

    /**
     * <p>注册到的消息流名称</p>
     * <p>返回值不能为空</p>
     * @return 消息流名称
     */
    @NonNull String forStream();

}
