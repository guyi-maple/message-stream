package tech.guyi.component.message.stream.api.publish;

import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.api.stream.entry.PublishResult;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * <p>消息流消息推送者</p>
 * <p>用于向消息流推送消息</p>
 * @author guyi
 * @date 2021/2/18 10:24
 */
public class MessageStreamPublisher {

    @Resource
    private MessageStreams streams;
    @Resource
    private MessageStreamWorker worker;
    @Resource
    private MessageTypeConverters converters;

    /**
     * 推送消息
     * @param message 消息实体
     * @param streams 要推送到的消息流集合
     * @return 推送返回
     */
    public List<CompletableFuture<PublishResult>> publish(Message message, List<MessageStream> streams){
         return streams.stream()
                .map(stream -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return stream.publish(message).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }, this.worker))
                .collect(Collectors.toList());
    }

    /**
     * 推送消息到所有存在的消息流
     * @param message 消息实体
     * @return 推送返回
     */
    public List<CompletableFuture<PublishResult>> publish(Message message){
        return this.publish(message, this.streams.getStreams());
    }

    /**
     * 推送消息
     * @param topic 消息Topic
     * @param message 消息内容
     * @param attach 附加信息
     * @param streams 要推送到的消息流集合
     * @param <T> 消息内容类型
     * @return 推送返回
     */
    public <T> List<CompletableFuture<PublishResult>> publish(String topic, T message, Map<String,Object> attach, List<MessageStream> streams){
        return this.publish(
                new Message(topic, converters.convert(message), attach),
                streams
        );
    }

    /**
     * 推送消息
     * @param topic 消息Topic
     * @param message 消息内容
     * @param attach 附加信息
     * @param <T> 消息内容类型
     * @return 推送返回
     */
    public <T> List<CompletableFuture<PublishResult>> publish(String topic, T message, Map<String,Object> attach){
        return this.publish(topic,message, attach, this.streams.getStreams());
    }



}
