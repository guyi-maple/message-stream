package tech.guyi.component.message.stream.api.consumer;

import lombok.SneakyThrows;
import org.springframework.core.annotation.AnnotatedElementUtils;
import tech.guyi.component.message.stream.api.annotation.receiver.MessageBind;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.enums.MessageBindType;
import tech.guyi.component.message.stream.api.utils.ObjectQueue;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>基于方法的消息消费者.</p>
 * <p>所有基于注解的消费者方法都会被包装为此对象进行消息的消费.</p>
 * @author guyi
 */
public class MethodMessageConsumer implements Consumer<ReceiveMessageEntry> {

    /**
     * 消费者方法所在的对象
     */
    private final Object bean;
    /**
     * 消费者方法
     */
    private final Method method;
    /**
     * 消息类型转换器
     */
    private final MessageTypeConverters converters;

    private final ObjectQueue<Object[]> arrayQueue = new ObjectQueue<Object[]>() {
        @Override
        protected Object[] create() {
            return new Object[0];
        }
    };

    public MethodMessageConsumer(Object bean, Method method, MessageTypeConverters converters) {
        this.bean = bean;
        this.method = method;
        this.converters = converters;
        this.init();
    }

    // 消息获取
    private List<Function<ReceiveMessageEntry, Object>> messageGetters;

    private void init(){
        Parameter[] parameters = this.method.getParameters();

        if (parameters.length == 0){
            this.messageGetters = Collections.emptyList();
            return;
        }

        if (parameters.length == 1){
            this.messageGetters = Collections.singletonList(entry -> this.converters.convert(entry.getBytes(), parameters[0].getType()));
            return;
        }

        this.messageGetters = Arrays.stream(parameters)
                .map(parameter ->
                        Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(parameter, MessageBind.class))
                                .map(annotation -> {
                                    MessageBindType bind = annotation.bind();
                                    if (bind == MessageBindType.CONTENT){
                                        return (Function<ReceiveMessageEntry, Object>) entry ->
                                                converters.convert((byte[]) bind.getGetter().apply(entry), parameter.getType());
                                    }else{
                                        return bind.getGetter();
                                    }
                                })
                                .orElse(null))
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public void accept(ReceiveMessageEntry entry) {
        Object[] params = this.messageGetters.stream()
                .map(Optional::ofNullable)
                .map(getter -> getter.map(get -> get.apply(entry)).orElse(null))
                .toArray(size -> this.arrayQueue.get());
        this.method.invoke(this.bean, params);
        this.arrayQueue.roll(params);
    }

}
