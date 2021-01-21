package tech.guyi.component.message.stream.api.worker;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 消息流工作空间.
 * 消息流所有基于线程池的操作, 都应使用此接口实现, 便于关闭消息流时统一的线程清理
 * @author guyi
 */
public interface MessageStreamWorker extends ScheduledExecutorService {
}
