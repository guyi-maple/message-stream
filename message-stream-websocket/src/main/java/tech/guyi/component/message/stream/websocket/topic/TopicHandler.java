package tech.guyi.component.message.stream.websocket.topic;

import lombok.NonNull;

/**
 * Topic处理器.
 * 从收到的消息中提取Topic.
 * 给发送的消息添加topic.
 * @author guyi
 */
public interface TopicHandler {

    /**
     * 返回处理器名称.
     * 当返回的名称与配置匹配时, 激活此处理器.
     * 返回的名称不能为空.
     * @return 处理器名称
     */
    @NonNull
    String getName();

    /**
     * 从消息体中获取Topic
     * @param bytes 消息体
     * @return Topic
     */
    String getTopic(byte[] bytes);

    /**
     * 为消息体设置Topic
     * @param topic Topic
     * @param bytes 原始消息体
     * @return 设置Topic后的消息体
     */
    byte[] setTopic(String topic, byte[] bytes);

}
