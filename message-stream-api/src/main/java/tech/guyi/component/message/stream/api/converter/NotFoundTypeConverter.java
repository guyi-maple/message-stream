package tech.guyi.component.message.stream.api.converter;

/**
 * @author guyi
 * @date 2021/1/18 23:10
 */
public class NotFoundTypeConverter extends RuntimeException {

    public NotFoundTypeConverter(Class<?> type) {
        super(String.format("找不到 [%s] 的类型转换器", type));
    }
}
