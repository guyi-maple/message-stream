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

/**
 * 基于方法的消息消费者
 */
@AllArgsConstructor
abstract class MethodMessageConsumer implements MessageConsumer<Object> {

    private final Object bean;
    private final Method method;
    // 消息内容参数类型
    private final Class<?> type;
    private final StreamSubscribe streamSubscribe;

    @Override
    public List<String> getStream() {
        return Arrays.asList(streamSubscribe.stream());
    }

    @Override
    public Map<String, Object> getAttach() {
        return Arrays.stream(streamSubscribe.params())
                .collect(Collectors.toMap(Parameter::key, Parameter::value));
    }

    @Override
    public List<String> getTopic() {
        return Arrays.asList(streamSubscribe.topic());
    }

    @Override
    public Class messageType() {
        return this.type;
    }

    protected abstract void accept(Map<String,Object> map, Object bean, Method method);

    /**
     * 将消息流传递的消息变为map
     * @param message 消息内容
     * @param topic Topic
     * @param sourceStream 来源消息流的名称
     * @param attach 额外信息
     */
    @Override
    public void accept(Object message, String topic, String sourceStream, Map<String,Object> attach) {
        Map<String,Object> map = new HashMap<>();
        // 将消息内容转为map
        // 键名称与getParameterName方法返回的名称需要保持一致
        map.put("message", message);
        map.put("topic", topic);
        map.put("stream", sourceStream);
        map.put("attach", attach);
        // 调用抽象方法, 执行监听方法
        this.accept(map, this.bean, this.method);
    }

    /**
     * <p>获取传入参数类型.</p>
     * <p>获取参数上是否修饰了指定的注解, 判断该参数应该注入的内容.</p>
     * @param parameter 参数
     * @return 内容名称
     */
    static String getParameterName(java.lang.reflect.Parameter parameter){
        if (parameter.getAnnotation(MessageContent.class) != null){
            return "message";
        }
        if (parameter.getAnnotation(Topic.class) != null){
            return "topic";
        }
        if (parameter.getAnnotation(StreamName.class) != null){
            return "stream";
        }
        if (parameter.getAnnotation(MessageAttach.class) != null){
            return "attach";
        }
        return null;
    }

    /**
     * <p>获取内容类型的位置.</p>
     * <p>根据getParameterName方法获取应该注入的参数内容名称.</p>
     * <p>找不到的内容类型则使用null占位</p>
     * @see MethodMessageConsumer#getParameterName(java.lang.reflect.Parameter) 
     * @param parameter 参数组
     * @return 内容类型的位置
     */
    static String[] getParameterNames(java.lang.reflect.Parameter[] parameter){
        String[] names = new String[parameter.length];
        for (int i = 0; i < parameter.length; i++) {
            names[i] = getParameterName(parameter[i]);
        }
        return names;
    }

}

/**
 * 自动注册消息消费者
 * @author guyi
 */
public class AutoRegister implements BeanPostProcessor {

    @Resource
    private MessageConsumers messageConsumers;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getMethods()) {
            StreamSubscribe listener = AnnotationUtils.findAnnotation(method, StreamSubscribe.class);
            if (listener != null){
                Class<?>[] types = method.getParameterTypes();
                MethodMessageConsumer consumer;
                // 如果被修饰的方法只有一个参数, 则默认注入消息内容
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
                    // 如果存在多个参数, 则获取参数上的注解, 确定每个位置的参数应该注入什么内容
                    java.lang.reflect.Parameter[] parameters = method.getParameters();
                    Class<?> type = Arrays.stream(parameters)
                            .filter(t -> "message".equals(MethodMessageConsumer.getParameterName(t)))
                            .findFirst()
                            .map(java.lang.reflect.Parameter::getType)
                            .orElse(null);
                    // 获取内容类型的位置
                    String[] names = MethodMessageConsumer.getParameterNames(parameters);
                    consumer = new MethodMessageConsumer(bean,method, type == null ? Object.class : type, listener){
                        @Override
                        @SneakyThrows
                        protected void accept(Map<String, Object> map, Object bean, Method method) {
                            method.invoke(
                                    bean,
                                    // 根据获取到的内容位置名称, 从Map中取出数据并转换为Object数组
                                    Arrays.stream(names)
                                            .map(map::get)
                                            .toArray(Object[]::new)
                            );
                        }
                    };
                }
                // 注册消费者
                this.messageConsumers.register(consumer);
            }
        }

        // 如果Bean实现了消息消费者接口, 则直接注册
        if (bean instanceof MessageConsumer){
            this.messageConsumers.register((MessageConsumer) bean);
        }

        return bean;
    }

}
