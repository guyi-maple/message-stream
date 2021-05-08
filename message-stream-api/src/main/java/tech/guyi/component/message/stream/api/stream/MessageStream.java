package tech.guyi.component.message.stream.api.stream;

import lombok.NonNull;
import tech.guyi.component.message.stream.api.attach.AttachKey;

import java.util.Map;
import java.util.Optional;

/**
 * <p>消息流接口.</p>
 * <p>实现此接口,获取不同来源的消息</p>
 * @author guyi
 * @param <T> 消息推送返回类型
 */
public interface MessageStream<T> {

    /**
     * <p>消息流名称.</p>
     * <p>用来标识不同的消息流, 不可重复或返回NULL</p>
     * @return 消息流名称
     */
    @NonNull
    String getName();

    /**
     * 关闭消息流
     */
    void close();

    /**
     * <p>注册消息主题</p>
     * <p>消息流实现支持Topic匹配时, 可重写此方法</p>
     * @param topic 消息主题
     * @param attach 消息消费者传递的额外参数
     */
    default void register(String topic, Map<Class<? extends AttachKey>,Object> attach){

    }

    /**
     * <p>取消消息主题的注册</p>
     * <p>消息流实现支持Topic匹配时, 可重写此方法</p>
     * @param topic 消息主题
     * @param attach 消息消费者传递的额外参数
     */
    default void unregister(String topic, Map<Class<? extends AttachKey>,Object> attach){

    }

    /**
     * 打开消息流
     * @param receiver 消息接收者
     */
    void open(MessageReceiver receiver);

    /**
     * 发布消息
     * @param topic Topic
     * @param bytes 消息内容
     * @param attach 消息附加信息
     */
    Optional<T> publish(String topic, byte[] bytes, Map<Class<? extends AttachKey>,Object> attach);

}
