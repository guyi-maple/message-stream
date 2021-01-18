package tech.guyi.component.message.stream.api.consumer;

import java.util.List;
import java.util.Map;

/**
 * @author guyi
 * @date 2021/1/16 12:51
 */
public interface MessageConsumer<M> {

    /**
     * 感兴趣的消息主题
     * @return 感兴趣的消息主题
     */
    List<String> getTopic();

    /**
     * 需要被绑定到的消息流 <br />
     * 具体说明见 MessageConsumerEntry.stream
     * @return 消息流名称
     */
    default List<String> getStream(){
        return null;
    }

    /**
     * 此消费者的额外参数 <br />
     * 具体说明见 MessageConsumerEntry.attach
     * @return 额外参数
     */
    default Map<String,Object> getAttach(){
        return null;
    }

    /**
     * 返回消息内容类型
     * @return 消息内容类型
     */
    Class<M> messageType();

    /**
     * 消费消息
     * @param message 消息内容
     * @param topic Topic
     * @param sourceStream 来源消息流的名称
     * @param attach 额外信息
     */
    void accept(M message, String topic, String sourceStream, Map<String,Object> attach);



}
