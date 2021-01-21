package tech.guyi.component.message.stream.websocket.executor;

import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式替换
 * @author guyi
 */
public class WebSocketServerExecutors {

    private final Pattern pattern = Pattern.compile("\\{([^}]*)\\}");

    @Resource
    private List<WebSocketServerExecutor> executors;

    /**
     * 替换表达式
     * @param text 原始文本
     * @return 替换后的文本
     */
    public String replace(String text){
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            String ex = matcher.group(1);
            String value = this.executors.stream()
                    .map(executor -> executor.execute(ex))
                    .filter(val -> !ObjectUtils.isEmpty(val))
                    .max(String::compareTo)
                    .orElse(ex);
            text = text.replace(String.format("{%s}", ex), value);
        }
        return text;
    }

}
