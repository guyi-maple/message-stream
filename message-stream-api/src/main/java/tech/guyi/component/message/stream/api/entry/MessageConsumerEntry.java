package tech.guyi.component.message.stream.api.entry;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 消息消费者实体 <br />
 * 记录消息消费者的完整信息, 用于注册消息消费者
 * @author guyi
 * @date 2021/1/15 22:59
 */
@Data
public class MessageConsumerEntry {

    /**
     * 感兴趣的消息主题
     */
    private List<String> topic;
    /**
     * 需要被绑定到的消息流 <br />
     * 当同时存在多个消息流实现但并不想绑定所所有消息流上时, 可以使用此字段 <br />
     * 当为NULL或空集合时, 表示绑定到所有的消息流中
     */
    private List<String> stream;
    /**
     * 消息接收处理
     */
    private Consumer<Message> receiver;
    /**
     * 此消费者的额外参数 <br />
     * 当消息流实现需要非标准参数时, 可以使用此字段传递
     */
    private Map<String,Object> attach;

}
