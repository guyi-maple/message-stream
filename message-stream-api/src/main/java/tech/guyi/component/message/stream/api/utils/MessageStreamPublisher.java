package tech.guyi.component.message.stream.api.utils;

import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.stream.MessageStreams;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 消息发布工具
 * @author guyi
 * @date 2021/1/19 10:09
 */
public class MessageStreamPublisher {

    @Resource
    private MessageStreams streams;
    @Resource
    private MessageTypeConverters converters;

    /**
     * 向消息流发布消息 <br />
     * 如果消息实体的stream不为空, 则会向stream指定的消息流发布消息 <br />
     * 如果消息实体中stream指定的消息流不存在, 则会丢弃该消息
     * @param topic 消息主题
     * @param bytes 消息内容
     * @param attach 附加信息
     * @param streams 要发布到的消息流, 为空表示发布到全部
     */
    public void publish(String topic, byte[] bytes, Map<String,Object> attach, List<String> streams){
        this.streams.publish(topic,bytes,attach,streams);
    }

    /**
     * 向消息流发布消息 <br />
     * 如果消息实体的stream不为空, 则会向stream指定的消息流发布消息 <br />
     * 如果消息实体中stream指定的消息流不存在, 则会丢弃该消息
     * @param topic 消息主题
     * @param bytes 消息内容
     * @param streams 要发布到的消息流, 为空表示发布到全部
     */
    public void publish(String topic, byte[] bytes, List<String> streams){
        this.publish(topic,bytes,null,streams);
    }

    /**
     * 向所有消息流发布消息
     * @param topic 消息主题
     * @param bytes 消息内容
     * @param attach 附加信息
     */
    public void publish(String topic, byte[] bytes, Map<String,Object> attach){
        this.publish(topic,bytes,attach, null);
    }

    /**
     * 向所有消息流发布消息
     * @param topic 消息主题
     * @param bytes 消息内容
     */
    public void publish(String topic, byte[] bytes){
        this.publish(topic,bytes,null, null);
    }


    /**
     * 向消息流发布消息 <br />
     * 如果消息实体的stream不为空, 则会向stream指定的消息流发布消息 <br />
     * 如果消息实体中stream指定的消息流不存在, 则会丢弃该消息
     * @param topic 消息主题
     * @param message 消息内容
     * @param attach 附加信息
     * @param streams 要发布到的消息流, 为空表示发布到全部
     */
    public <T> void publish(String topic, T message, Map<String,Object> attach, List<String> streams){
        this.publish(topic,this.converters.convert(message),attach,streams);
    }

    /**
     * 向消息流发布消息 <br />
     * 如果消息实体的stream不为空, 则会向stream指定的消息流发布消息 <br />
     * 如果消息实体中stream指定的消息流不存在, 则会丢弃该消息
     * @param topic 消息主题
     * @param message 消息内容
     * @param streams 要发布到的消息流, 为空表示发布到全部
     */
    public <T> void publish(String topic, T message, List<String> streams){
        this.publish(topic,message,null,streams);
    }

    /**
     * 向所有消息流发布消息 <br />
     * @param topic 消息主题
     * @param message 消息内容
     * @param attach 附加信息
     */
    public <T> void publish(String topic, T message, Map<String,Object> attach){
        this.publish(topic,message,attach,null);
    }

    /**
     * 向所有消息流发布消息 <br />
     * @param topic 消息主题
     * @param message 消息内容
     */
    public <T> void publish(String topic, T message){
        this.publish(topic,message,null,null);
    }



}
