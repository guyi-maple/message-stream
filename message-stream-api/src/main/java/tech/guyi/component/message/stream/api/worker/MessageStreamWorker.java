package tech.guyi.component.message.stream.api.worker;

import java.util.concurrent.ScheduledExecutorService;

/**
 * <p>消息流工作空间.</p>
 * <p>消息流所有基于线程池的操作, 都应使用此接口实现, 便于关闭消息流时统一的线程清理</p>
 * @author guyi
 */
public interface MessageStreamWorker extends ScheduledExecutorService {
}
