package tech.guyi.component.message.stream.api.consumer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import tech.guyi.component.message.stream.api.annotation.listener.ConsumerAttachProvider;
import tech.guyi.component.message.stream.api.annotation.listener.Subscribe;
import tech.guyi.component.message.stream.api.annotation.listener.Topic;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自动注册消息消费者
 * @author guyi
 */
public class ConsumerAutoRegister implements BeanPostProcessor {

    @Resource
    private MessageConsumers consumers;
    @Resource
    private MessageTypeConverters converters;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getMethods()) {
            Optional.ofNullable(AnnotationUtils.findAnnotation(method, Subscribe.class))
                    .ifPresent(subscribe -> {
                        // 消费者
                        MethodMessageConsumer consumer = new MethodMessageConsumer(bean, method, this.converters);

                        // 获取要监听的topic
                        List<String> topics = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, Topic.class))
                                .map(Topic::value)
                                .map(Arrays::asList)
                                .orElse(Collections.singletonList("*"));

                        // 获取要监听的消息流
                        List<String> streams = Arrays.asList(subscribe.stream());

                        // 获取所有附加参数
                        Map<Class<? extends AttachKey>,Object> attach = AnnotatedElementUtils.findAllMergedAnnotations(method, ConsumerAttachProvider.class)
                                .stream()
                                .collect(Collectors.toMap(ConsumerAttachProvider::key, ConsumerAttachProvider::value));

                        // 注册消费者
                        this.consumers.register(topics,consumer,attach, streams);
                    });
        }

        return bean;
    }

}
