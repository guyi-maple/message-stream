package tech.guyi.component.message.stream.websocket;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import tech.guyi.component.message.stream.websocket.getter.WebSocketServerAddressGetter;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 配置
 * @author guyi
 * @date 2021/1/18 14:55
 */
@Data
public class WebSocketConfiguration {

    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * Websocket连接地址 <br />
     */
    private String server;
    /**
     * Websocket连接地址获取器 <br />
     * 当server配置为空时, 此项配置才会被激活
     */
    private String serverAddressGetter;
    /**
     * 要启用的Topic处理器名称 <br />
     */
    private String topicHandlerName = "default-json";


    @Resource
    private ApplicationContext context;
    @SneakyThrows
    public String getServer(){
        if (ObjectUtils.isEmpty(this.server) && !ObjectUtils.isEmpty(this.serverAddressGetter)){
            try {
                Class<?> classes = Class.forName(this.serverAddressGetter);
                this.server = Optional.of(this.context.getBean(classes))
                        .filter(bean -> bean instanceof WebSocketServerAddressGetter)
                        .map(bean -> (WebSocketServerAddressGetter) bean)
                        .map(bean -> bean.get(this))
                        .orElse(this.server);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
        return this.server;
    }

}
