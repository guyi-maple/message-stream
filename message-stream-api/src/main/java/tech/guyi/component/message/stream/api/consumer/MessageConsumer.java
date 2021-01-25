package tech.guyi.component.message.stream.api.consumer;

import java.util.List;
import java.util.Map;

/**
 * 消息消费者接口.
 * @author guyi
 */
public interface MessageConsumer<M> {

    /**
     * 感兴趣的消息主题
     * @return 感兴趣的消息主题
     */
    List<String> getTopic();

    /**
     * <p>需要被绑定到的消息流名称.</p>
     * <p>当返回NULL或空集合时, 表示注册到所有存在的消息流中</p>
     * @return 消息流名称
     */
    default List<String> getStream(){
        return null;
    }

    /**
     * <p>此消费者的额外参数.</p>
     * <p>可能存在消息流实现需要消费者额外提供参数的情况, 此时可以使用此方法传递给消息流实现者</p>
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
