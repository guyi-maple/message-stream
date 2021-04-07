package tech.guyi.component.message.stream.api.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 对象复用队列
 * @author guyi
 * @date 2021/4/7 21:38
 */
public abstract class ObjectQueue<O> {

    protected abstract O create();

    private final Queue<O> queue = new ConcurrentLinkedQueue<>();

    /**
     * 获取对象
     * @return 对象
     */
    public O get(){
        O obj = this.queue.poll();
        if (obj == null) {
            obj = this.create();
        }
        return obj;
    }

    /**
     * 归还对象
     * @param obj 要归还对象
     */
    public void roll(O obj) {
        this.queue.add(obj);
    }

}
