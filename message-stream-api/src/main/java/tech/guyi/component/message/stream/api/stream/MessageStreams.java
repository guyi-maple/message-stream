package tech.guyi.component.message.stream.api.stream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * 消息流仓库
 * @author guyi
 * @date 2021/1/16 12:58
 */
public class MessageStreams implements InitializingBean {

    // 消息流集合
    private final Map<String, MessageStream> streams = new HashMap<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageStream.class)
                .values()
                .forEach(stream -> streams.put(stream.getName(),stream));
    }

    /**
     * 获取所有消息流
     * @return 消息流集合
     */
    public Collection<MessageStream> getStreams(){
        return this.streams.values();
    }

//    /**
//     * 向消息流中发布消息 <br />
//     * 如果消息实体的stream不为空, 则会向stream指定的消息流发布消息 <br />
//     * 如果消息实体中stream指定的消息流不存在, 则会丢弃该消息
//     * @param message 要发布的消息
//     */
//    public void publish(Message message){
//        // 获取到需要发布的消息流, 并发布消息
//        Optional.ofNullable(message.getStream())
//                .filter(name -> !ObjectUtils.isEmpty(name))
//                .map(names ->
//                        this.streams.values()
//                                .stream()
//                                .filter(stream -> names.contains(stream.getName()))
//                                .collect(Collectors.toList()))
//                .map(list -> (Collection<MessageStream>) list)
//                .orElse(this.streams.values())
//                .forEach(stream -> stream.publish(message));
//
//        // 回调消息发布钩子
//        this.runner.run(MessageStreamHook.PUBLISH, message);
//    }

}
