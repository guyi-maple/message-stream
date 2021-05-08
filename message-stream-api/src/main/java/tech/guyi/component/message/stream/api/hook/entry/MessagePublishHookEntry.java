package tech.guyi.component.message.stream.api.hook.entry;

import lombok.Getter;
import tech.guyi.component.message.stream.api.attach.AttachKey;

import java.util.List;
import java.util.Map;

/**
 * 消息发布钩子执行参数
 * @author guyi
 */
@Getter
public class MessagePublishHookEntry {

    /**
     * Topic
     */
    private String topic;

    /**
     * 消息内容
     */
    private byte[] bytes;

    /**
     * 消息附加信息
     */
    private Map<Class<? extends AttachKey>, Object> attach;

    /**
     * 指向消息流名称
     */
    private List<String> stream;

    /**
     * 消息推送返回值
     */
    private Object result;

    /**
     * 包装
     * @param topic Topic
     * @param bytes 消息内容
     * @param attach 消息附加信息
     * @param stream 指向消息流名称
     * @param result 消息推送返回值
     */
    public void pack(String topic, byte[] bytes, Map<Class<? extends AttachKey>, Object> attach, List<String> stream, Object result) {
        this.topic = topic;
        this.bytes = bytes;
        this.attach = attach;
        this.stream = stream;
        this.result = result;
    }

    /**
     * 清理
     */
    public void clear() {
        this.topic = null;
        this.bytes = null;
        this.attach = null;
        this.stream = null;
        this.result = null;
    }

}
