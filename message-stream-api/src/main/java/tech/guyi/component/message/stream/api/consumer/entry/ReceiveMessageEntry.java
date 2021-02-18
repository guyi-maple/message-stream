package tech.guyi.component.message.stream.api.consumer.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.guyi.component.message.stream.api.attach.AttachKey;

import java.util.Map;

/**
 * 接收消息实体
 * @author guyi
 */
@Getter
@AllArgsConstructor
public class ReceiveMessageEntry {

    /**
     * 消息内容
     */
    private final byte[] bytes;

    /**
     * 消息的Topic
     */
    private final String topic;

    /**
     * 消息来源的消息流名称
     */
    private final String stream;

    /**
     * 消息流传入的附加信息
     */
    private final Map<Class<? extends AttachKey>,Object> attach;

}
