package tech.guyi.component.message.stream.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;

import java.util.function.Function;

/**
 * 消息绑定类型
 * @author guyi
 */
@Getter
@AllArgsConstructor
public enum MessageBindType {

    CONTENT("content", ReceiveMessageEntry::getBytes),
    TOPIC("topic", ReceiveMessageEntry::getTopic),
    STREAM("stream", ReceiveMessageEntry::getStream),
    ATTACH("attach", ReceiveMessageEntry::getAttach);

    private final String value;
    private final Function<ReceiveMessageEntry, Object> getter;

}
