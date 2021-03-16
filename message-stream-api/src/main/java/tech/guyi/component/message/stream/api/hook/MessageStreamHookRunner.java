package tech.guyi.component.message.stream.api.hook;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * 消息流钩子执行器
 * @author guyi
 */
public class MessageStreamHookRunner implements InitializingBean {

    // 消息流钩子集合
    private final List<MessageStreamHook<Object>> hooks = new LinkedList<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageStreamHook.class)
                .values()
                .forEach(hooks::add);
    }

    /**
     * 执行钩子
     * @param type 钩子类型
     * @param entry 执行参数
     * @param <E> 执行参数类型
     * @param <H> 钩子类型
     */
    public <E,H extends MessageStreamHook<E>> void run(MessageStreamHookType<E,H> type, E entry){
        this.hooks.stream()
                .filter(hook -> type.getClasses().isAssignableFrom(hook.getClass()))
                .forEach(hook -> hook.on(entry));
    }

    /**
     * 执行钩子
     * @param type 钩子类型
     * @param filter 过滤器
     * @param entry 执行参数
     * @param <E> 执行参数类型
     * @param <H> 钩子类型
     */
    public <E,H extends MessageStreamHook<E>> void run(MessageStreamHookType<E,H> type, Function<MessageStreamHook<E>, Boolean> filter, E entry){
        this.hooks.stream()
                .filter(hook -> type.getClasses().isAssignableFrom(hook.getClass()))
                .filter(hook -> filter.apply((MessageStreamHook<E>) hook))
                .forEach(hook -> hook.on(entry));
    }

}
