package tech.guyi.component.message.stream.websocket.exception;

/**
 * @author guyi
 * @date 2021/1/19 11:17
 */
public class ConnectionNotReadyException extends RuntimeException {

    public ConnectionNotReadyException(){
        super("连接还未准备就绪");
    }

}
