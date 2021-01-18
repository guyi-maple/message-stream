package tech.guyi.component.message.stream.api.hook;

import lombok.Getter;

/**
 * @author guyi
 * @date 2021/1/18 18:10
 */
public class MessageStreamHookType<E,H extends MessageStreamHook<E>> {

    @Getter
    private Class<H> classes;

    public static <E,H extends MessageStreamHook<E>> MessageStreamHookType<E,H> valueOf(Class<H> classes){
        MessageStreamHookType<E,H> type = new MessageStreamHookType<>();
        type.classes = classes;
        return type;
    }

}
