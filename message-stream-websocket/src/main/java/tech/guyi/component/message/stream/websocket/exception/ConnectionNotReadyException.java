package tech.guyi.component.message.stream.websocket.exception;

/**
 * 抛出此异常表示连接还未建立时就执行消息发送操作
 * @author guyi
 */
public class ConnectionNotReadyException extends RuntimeException {

    public ConnectionNotReadyException(){
        super("连接还未准备就绪");
    }

}
