package tech.guyi.component.message.stream.api;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import tech.guyi.component.message.stream.api.annotation.MessageListener;
import tech.guyi.component.message.stream.api.annotation.MessageListenerParam;
import tech.guyi.component.message.stream.api.consumer.MessageConsumer;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@AllArgsConstructor
class InvokeMethod implements Consumer<Message> {

    private final Object bean;
    private final Method method;

    @Override
    public void accept(Message message) {
        try {
            method.invoke(this.bean,message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}

/**
 * 自动注册
 * @author guyi
 * @date 2021/1/16 12:55
 */
public class AutoRegister implements BeanPostProcessor {

    @Resource
    private ApplicationContext context;

    @Resource
    private MessageStreamRepository repository;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getMethods()) {
            MessageListener listener = AnnotationUtils.findAnnotation(method,MessageListener.class);
            if (listener != null){
                Class<?>[] types = method.getParameterTypes();
                if (types.length == 1 && types[0] == Message.class){
                    MessageConsumerEntry entry = new MessageConsumerEntry();
                    entry.setTopic(Arrays.asList(listener.topic()));
                    entry.setStream(Arrays.asList(listener.stream()));
                    entry.setAttach(Arrays.stream(listener.params())
                                    .collect(Collectors.toMap(MessageListenerParam::key, MessageListenerParam::value)));
                    entry.setReceiver(new InvokeMethod(bean,method));
                    this.repository.register(entry);
                }
            }
        }

        if (bean instanceof MessageConsumer){
            MessageConsumer consumer = (MessageConsumer) bean;
            MessageConsumerEntry entry = new MessageConsumerEntry();
            entry.setTopic(consumer.getTopic());
            entry.setStream(consumer.getStream());
            entry.setAttach(consumer.getAttach());
            entry.setReceiver(consumer);
            this.repository.register(entry);
        }

        return bean;
    }

}
