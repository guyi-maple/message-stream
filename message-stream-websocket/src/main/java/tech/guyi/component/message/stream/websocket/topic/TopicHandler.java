package tech.guyi.component.message.stream.websocket.topic;

import lombok.NonNull;

/**
 * <p>Topic处理器.</p>
 * <p>从收到的消息中提取Topic.</p>
 * <p>给发送的消息添加topic.</p>
 * @author guyi
 */
public interface TopicHandler {

    /**
     * <p>返回处理器名称.</p>
     * <p>当返回的名称与配置匹配时, 激活此处理器.</p>
     * <p>返回的名称不能为空.</p>
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
