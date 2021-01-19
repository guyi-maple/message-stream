package tech.guyi.component.message.stream.api.hook;

import lombok.Getter;

/**
 * 钩子类型枚举
 * @author guyi
 * @date 2021/1/18 18:10
 */
public class MessageStreamHookType<E,H extends MessageStreamHook<E>> {

    /**
     * 执行参数类型
     */
    @Getter
    private Class<H> classes;

    /**
     * 创建钩子类型
     * @param classes 执行参数类型
     * @param <E> 执行参数类型
     * @param <H> 钩子类型
     * @return 钩子类型
     */
    public static <E,H extends MessageStreamHook<E>> MessageStreamHookType<E,H> valueOf(Class<H> classes){
        MessageStreamHookType<E,H> type = new MessageStreamHookType<>();
        type.classes = classes;
        return type;
    }

}
