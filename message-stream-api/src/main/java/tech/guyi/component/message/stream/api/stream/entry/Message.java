package tech.guyi.component.message.stream.api.stream.entry;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * 消息流向上层推送消息的实体
 * @author guyi
 */
@Data
public class Message {

    /**
     * Topic
     */
    private final String topic;
    /**
     * 消息内容
     */
    private final byte[] bytes;
    /**
     * 附加信息
     */
    private final Map<String,Object> attach;

    public Message(String topic, byte[] bytes) {
        this(topic,bytes, Collections.emptyMap());
    }

    public Message(String topic, byte[] bytes, Map<String, Object> attach) {
        this.topic = topic;
        this.bytes = bytes;
        this.attach = attach;
    }
}
