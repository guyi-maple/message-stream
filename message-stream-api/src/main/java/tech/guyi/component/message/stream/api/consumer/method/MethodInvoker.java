package tech.guyi.component.message.stream.api.consumer.method;

import lombok.Setter;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;

/**
 * <p>方法执行者</p>
 * <p>动态字节码生成的基类</p>
 * @author guyi
 * @date 2021/4/8 11:06
 */
@Setter
public abstract class MethodInvoker<B> {

    protected B bean;

    /**
     * 执行方法
     * @param message 消息实体
     * @param converters 消息转换器
     */
    public abstract void invoke(ReceiveMessageEntry message, MessageTypeConverters converters);

}
