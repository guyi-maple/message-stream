package tech.guyi.component.message.stream.api.hook.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import java.util.List;

/**
 * @author guyi
 * @date 2021/1/19 09:48
 */
@Getter
@AllArgsConstructor
public class MessagePublishHookEntry {

    private final Message message;
    private final List<String> stream;

}
