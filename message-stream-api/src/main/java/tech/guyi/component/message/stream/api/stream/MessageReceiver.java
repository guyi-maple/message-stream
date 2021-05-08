package tech.guyi.component.message.stream.api.stream;

import tech.guyi.component.message.stream.api.attach.AttachKey;

import java.util.Map;

/**
 * 消息接收者
 * @author guyi
 * @date 2021/5/8 14:56
 */
public interface MessageReceiver {

    void accept(String topic, byte[] bytes, Map<Class<? extends AttachKey>, Object> attach);

}
