package tech.guyi.component.message.stream.api.consumer;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import tech.guyi.component.message.stream.api.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
abstract class MethodMessageConsumer implements MessageConsumer<Object> {

    private final Object bean;
    private final Method method;
    private final Class<?> type;
    private final Subscribe subscribe;

    @Override
    public List<String> getStream() {
        return Arrays.asList(subscribe.stream());
    }

    @Override
    public Map<String, Object> getAttach() {
        return Arrays.stream(subscribe.params())
                .collect(Collectors.toMap(Parameter::key, Parameter::value));
    }

    @Override
    public List<String> getTopic() {
        return Arrays.asList(subscribe.topic());
    }

    @Override
    public Class messageType() {
        return this.type;
    }

    protected abstract void accept(Map<String,Object> map, Object bean, Method method);

    @Override
    public void accept(Object message, String topic, String sourceStream, Map<String,Object> attach) {
        Map<String,Object> map = new HashMap<>();
        map.put("message", message);
        map.put("topic", topic);
        map.put("stream", sourceStream);
        map.put("attach", attach);
        this.accept(map, this.bean, this.method);
    }

    static String getParameterName(java.lang.reflect.Parameter parameter){
        if (parameter.getAnnotation(Message.class) != null){
            return "message";
        }
        if (parameter.getAnnotation(Topic.class) != null){
            return "topic";
        }
        if (parameter.getAnnotation(StreamName.class) != null){
            return "stream";
        }
        if (parameter.getAnnotation(Attach.class) != null){
            return "attach";
        }
        return null;
    }
    static String[] getParameterNames(java.lang.reflect.Parameter[] parameter){
        String[] names = new String[parameter.length];
        for (int i = 0; i < parameter.length; i++) {
            names[i] = getParameterName(parameter[i]);
        }
        return names;
    }

}

/**
 * 自动注册
 * @author guyi
 * @date 2021/1/16 12:55
 */
public class AutoRegister implements BeanPostProcessor {

    @Resource
    private MessageConsumers messageConsumers;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getMethods()) {
            Subscribe listener = AnnotationUtils.findAnnotation(method, Subscribe.class);
            if (listener != null){
                Class<?>[] types = method.getParameterTypes();
                MethodMessageConsumer consumer;
                if (types.length == 1){
                    Class<?> type = types[0];
                    consumer = new MethodMessageConsumer(bean,method,type, listener){
                        @Override
                        @SneakyThrows
                        protected void accept(Map<String, Object> map, Object bean, Method method) {
                            method.invoke(bean,map.get("message"));
                        }
                    };
                }else {
                    java.lang.reflect.Parameter[] parameters = method.getParameters();
                    Class<?> type = Arrays.stream(parameters)
                            .filter(t -> "message".equals(MethodMessageConsumer.getParameterName(t)))
                            .findFirst()
                            .map(java.lang.reflect.Parameter::getType)
                            .orElse(null);
                    String[] names = MethodMessageConsumer.getParameterNames(parameters);
                    consumer = new MethodMessageConsumer(bean,method, type == null ? Object.class : type, listener){
                        @Override
                        @SneakyThrows
                        protected void accept(Map<String, Object> map, Object bean, Method method) {
                            method.invoke(
                                    bean,
                                    Arrays.stream(names)
                                            .map(map::get)
                                            .toArray(Object[]::new)
                            );
                        }
                    };
                }
                this.messageConsumers.register(consumer);
            }
        }

        if (bean instanceof MessageConsumer){
            this.messageConsumers.register((MessageConsumer) bean);
        }

        return bean;
    }

}
