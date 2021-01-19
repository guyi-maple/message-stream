package tech.guyi.component.message.stream.api.worker;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 消息流工作空间 <br />
 * 所有基于线程池的操作, 都应使用此接口实现
 * @author guyi
 * @date 2021/1/18 22:50
 */
public interface MessageStreamWorker extends ScheduledExecutorService {
}
