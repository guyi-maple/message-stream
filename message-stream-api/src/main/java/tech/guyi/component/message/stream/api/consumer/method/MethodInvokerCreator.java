package tech.guyi.component.message.stream.api.consumer.method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;
import org.springframework.core.annotation.AnnotatedElementUtils;
import tech.guyi.component.message.stream.api.annotation.receiver.MessageBind;
import tech.guyi.component.message.stream.api.consumer.entry.ReceiveMessageEntry;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.enums.MessageBindType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 方法执行器创建者
 * @author guyi
 * @date 2021/4/8 14:06
 */
public class MethodInvokerCreator {

    private static final String packageName = "tech.guyi.component.message.stream.api.consumer.method.invoker";

    @SneakyThrows
    public static <T>  MethodInvoker<T> create(T bean, Method method) {
        ClassPool pool = ClassPool.getDefault();

        String beanName = bean.getClass().getName();
        if (beanName.contains("$$")){
            beanName = beanName.substring(0, beanName.indexOf("$$"));
        }

        String methodName = method.getName();
        Parameter[] parameters = method.getParameters();

        CtClass invoker = pool.makeClass(String.format("%s.%sMethodInvoker", packageName, UUID.randomUUID().toString().replaceAll("-","")));
        invoker.setSuperclass(pool.get(MethodInvoker.class.getName()));

        StringBuilder source = new StringBuilder();
        source.append("public void invoke(")
                .append(ReceiveMessageEntry.class.getName())
                .append(" message, ")
                .append(MessageTypeConverters.class.getName())
                .append(" converters) {\n");

        // 参数长度为0, 直接执行
        if (parameters.length == 0) {
            source.append("((")
                    .append(beanName)
                    .append(")")
                    .append("$0.bean).")
                    .append(methodName)
                    .append("();\n");
        }

        // 当参数长度为1时默认表示消息内容
        // 此时@MessageBind是无效的
        if (parameters.length == 1){
            source.append("((")
                    .append(beanName)
                    .append(")")
                    .append("$0.bean).")
                    .append(methodName)
                    .append("((")
                    .append(parameters[0].getType().getName())
                    .append(")")
                    .append("$2.convert(message.getBytes(),")
                    .append(parameters[0].getType().getName())
                    .append(".class")
                    .append("));\n");
        }

        if (parameters.length > 1) {
            List<MessageBindType> types = Arrays.stream(parameters)
                    .map(parameter -> Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(parameter, MessageBind.class))
                            .map(MessageBind::bind)
                            .orElse(null))
                    .collect(Collectors.toList());
            for (int i = 0; i < types.size(); i++) {
                MessageBindType type = types.get(i);
                source.append(parameters[i].getType().getName())
                        .append(" value").append(i)
                        .append("=");

                if (type == null) {
                    source.append("null");
                }else {
                    if (type == MessageBindType.CONTENT) {
                        source.append(String.format(
                                "$2.convert((byte[]) %s.%s.getGetter().apply($1),%s.class)",
                                MessageBindType.class.getName(),
                                type,
                                parameters[i].getType().getName()));
                    }else{
                        source.append(String.format(
                                "%s.%s.getGetter().apply($1)",
                                MessageBindType.class.getName(),
                                type));
                    }
                }
                source.append(";\n");
            }

            source.append("((")
                    .append(beanName)
                    .append(")")
                    .append("$0.bean).")
                    .append(methodName)
                    .append("(");

            for (int i = 0; i < types.size(); i++) {
                source.append("(")
                        .append(parameters[i].getType().getName())
                        .append(")")
                        .append("value").append(i);
                if (i != (types.size() - 1)){
                    source.append(",");
                }
            }
            source.append(");\n");
        }

        source.append("}");

        invoker.addMethod(CtMethod.make(source.toString(), invoker));

        MethodInvoker<T> object = (MethodInvoker<T>) invoker.toClass().newInstance();
        object.setBean(bean);

        return object;
    }

}
