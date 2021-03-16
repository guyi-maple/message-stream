package tech.guyi.component.message.stream.api.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.consumer.MessageConsumers;
import tech.guyi.component.message.stream.api.hook.MessageStreamHook;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;
import tech.guyi.component.message.stream.api.hook.defaults.MessagePublishHook;
import tech.guyi.component.message.stream.api.hook.entry.MessagePublishHookEntry;
import tech.guyi.component.message.stream.api.stream.entry.Message;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 消息流仓库
 * @author guyi
 */
@Slf4j
public class MessageStreams implements InitializingBean {

    // 消息流集合
    private final Map<String, MessageStream<Object>> streams = new HashMap<>();

    @Resource
    private MessageStreamHookRunner hookRunner;
    @Resource
    private MessageConsumers messageConsumers;

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageStream.class)
                .values()
                .forEach(stream -> streams.put(stream.getName(),stream));

        // 打开所有流
        this.streams.forEach((name,stream) -> stream.open(message -> this.messageConsumers.onMessage(
                message.getTopic(),
                name,
                message.getAttach(),
                message.getBytes()
        )));

        // 回调消息流打开钩子
        this.hookRunner.run(MessageStreamHook.STREAM_OPEN, this.streams.keySet());
    }

    /**
     * 根据正则表达式匹配消息流名称查找消息流.
     * @param pattern 正则表达式
     * @return 消息流集合
     */
    public List<MessageStream<Object>> pattern(String pattern){
        return this.streams.keySet().stream()
                .filter(name -> Pattern.matches(pattern,name))
                .map(this.streams::get)
                .collect(Collectors.toList());
    }

    /**
     * <p>通过消息流名称集合查找消息流.</p>
     * <p>当传入NULL或空集合时, 返回所有消息流.</p>
     * @param names 消息流名称集合
     * @return 消息流集合
     */
    public List<MessageStream<Object>> filter(List<String> names){
        return Optional.ofNullable(names)
                .filter(ns -> !names.isEmpty())
                .map(ns -> ns.stream().map(this.streams::get).collect(Collectors.toList()))
                .orElse(this.all());
    }

    /**
     * 获取所有消息流
     * @return 消息流集合
     */
    public List<MessageStream<Object>> all(){
        return new LinkedList<>(this.streams.values());
    }

    /**
     * 关闭所有消息流
     */
    public void close(){
        this.close(null);
    }

    /**
     * 关闭消息流
     * @param names 需要被关闭的消息流
     */
    public void close(List<String> names){
        // 如果names不为空则关闭指定的消息流
        // 如果names为空则关闭所有消息流
        Optional.ofNullable(names)
                .filter(ns -> !ns.isEmpty())
                .map(ns -> ns.stream().map(this.streams::get).collect(Collectors.toList()))
                .map(stream -> (Collection<MessageStream<Object>>) stream)
                .orElse(this.all())
                .forEach(MessageStream::close);
    }

    /**
     * <p>向消息流发布消息.</p>
     * <p>如果消息实体的stream不为空, 则会向stream指定的消息流发布消息.</p>
     * <p>如果消息实体中stream指定的消息流不存在, 则会丢弃该消息</p>
     * @param topic 消息主题
     * @param bytes 消息内容
     * @param attach 附加信息
     * @param streams 要发布到的消息流, 为空表示发布到全部
     */
    public void publish(String topic, byte[] bytes, Map<Class<? extends AttachKey>,Object> attach, List<String> streams){
        Message message = new Message(topic,bytes,Optional.ofNullable(attach).orElse(Collections.emptyMap()));

        // 存储消息推送返回
        Map<String,Object> resultMap = new HashMap<>();

        // 如果streams不为空获取到需要发布的消息流, 并发布消息
        // 如果streams为空获取到所有消息流, 并发布消息
        Optional.ofNullable(streams)
                .filter(name -> !ObjectUtils.isEmpty(name))
                .map(names ->
                        this.streams.values()
                                .stream()
                                .filter(stream -> names.contains(stream.getName()))
                                .collect(Collectors.toList()))
                .map(list -> (Collection<MessageStream<Object>>) list)
                .orElse(this.streams.values())
                //循环发布消息到消息流
                .forEach(stream -> stream.publish(message).ifPresent(r -> resultMap.put(stream.getName(), r)));

        resultMap.forEach((key,value) -> {
            // 回调消息发布钩子
            this.hookRunner.run(
                    MessageStreamHook.PUBLISH,
                    hook -> ((MessagePublishHook) hook).forStream().equals(key),
                    new MessagePublishHookEntry(
                            message,
                            streams,
                            value
                    )
            );
        });
    }

}
