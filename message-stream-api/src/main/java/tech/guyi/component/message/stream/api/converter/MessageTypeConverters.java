package tech.guyi.component.message.stream.api.converter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import tech.guyi.component.message.stream.api.converter.exception.NotFoundTypeConverterException;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * 消息类型转换
 * @author guyi
 */
public class MessageTypeConverters implements InitializingBean {

    // 消息类型转换器集合
    private final List<MessageTypeConverter<Object>> converters = new LinkedList<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageTypeConverter.class)
                .values()
                .forEach(converter -> this.converters.add(converter));
    }

    /**
     * 消息转为byte数组
     * @param message 消息
     * @return byte数组
     */
    public byte[] convert(Object message){
        Class<?> type = message.getClass();

        // 如果消息类型为byte[], 则不需要做类型转换
        if (type == byte[].class){
            return (byte[]) message;
        }
        return this.converters.stream()
                .filter(converter -> converter.forType().isAssignableFrom(type))
                .findFirst()
                .map(converter -> converter.to(converter.forType().cast(message)))
                .orElseGet(() -> message.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * byte数组转为消息
     * @param bytes byte数组
     * @param type 消息类型
     * @param <M> 消息类型
     * @return byte数组
     */
    public <M> M convert(byte[] bytes, Class<M> type){
        // 如果消息类型为byte[], 则不需要做类型转换
        if (type == byte[].class){
            return type.cast(bytes);
        }
        return this.converters.stream()
                .filter(converter -> converter.forType().isAssignableFrom(type))
                .findFirst()
                .map(converter -> type.cast(converter.from(bytes,type)))
                .orElseThrow(() -> new NotFoundTypeConverterException(type));
    }

}
