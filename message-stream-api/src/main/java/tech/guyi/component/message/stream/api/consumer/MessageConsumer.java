package tech.guyi.component.message.stream.api.consumer;

import tech.guyi.component.message.stream.api.entry.Message;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author guyi
 * @date 2021/1/16 12:51
 */
public interface MessageConsumer extends Consumer<Message> {

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
    List<String> getStream();

    /**
     * 此消费者的额外参数 <br />
     * 具体说明见 MessageConsumerEntry.attach
     * @return 额外参数
     */
    Map<String,Object> getAttach();

}
