package tech.guyi.component.message.stream.api.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 对象复用队列
 * @author guyi
 * @date 2021/4/7 21:38
 */
public abstract class ObjectQueue<O, P> {

    protected abstract O create(P p);

    private final Queue<O> queue = new ConcurrentLinkedQueue<>();

    /**
     * 获取对象
     * @return 对象
     */
    public O get(P p){
        O obj = this.queue.poll();
        if (obj == null) {
            obj = this.create(p);
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
