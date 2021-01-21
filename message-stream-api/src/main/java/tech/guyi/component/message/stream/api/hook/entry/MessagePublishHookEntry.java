package tech.guyi.component.message.stream.api.hook.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import java.util.List;

/**
 * 消息发布钩子执行参数
 * @author guyi
 */
@Getter
@AllArgsConstructor
public class MessagePublishHookEntry {

    /**
     * 消息
     */
    private final Message message;
    /**
     * 指向消息流名称
     */
    private final List<String> stream;

}
