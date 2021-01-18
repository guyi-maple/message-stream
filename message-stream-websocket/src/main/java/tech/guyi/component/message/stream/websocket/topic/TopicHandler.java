package tech.guyi.component.message.stream.websocket.topic;

import lombok.NonNull;

/**
 * Topic处理器 <br />
 * 从收到的消息中提取Topic <br />
 * 给发送的消息添加topic <br />
 * @author guyi
 * @date 2021/1/18 15:57
 */
public interface TopicHandler {

    /**
     * 返回处理器名称 <br />
     * 当返回的名称与配置匹配时, 激活此处理器 <br />
     * 返回的名称不能为空 <br />
     * @return 处理器名称
     */
    @NonNull
    String getName();

    /**
     * 从消息体中获取Topic
     * @param message 消息体
     * @return Topic
     */
    String getTopic(String message);

    /**
     * 为消息体设置Topic
     * @param topic Topic
     * @param message 原始消息体
     * @return 设置Topic后的消息体
     */
    String setTopic(String topic, String message);

}
