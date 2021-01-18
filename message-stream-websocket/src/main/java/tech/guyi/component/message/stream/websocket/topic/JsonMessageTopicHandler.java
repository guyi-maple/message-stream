package tech.guyi.component.message.stream.websocket.topic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

/**
 * JSON格式消息的Token处理器
 * @author guyi
 * @date 2021/1/18 15:59
 */
public class JsonMessageTopicHandler implements TopicHandler {

    private final Gson gson = new Gson();
    private final String topicName = "topic";
    private final Type mapType = new TypeToken<Map<String,Object>>(){}.getType();

    @Override
    public @NonNull String getName() {
        return "default-json";
    }

    @Override
    public String getTopic(String message) {
        Map<String, Object> map = this.gson.fromJson(message,mapType);
        return Optional.ofNullable(map.get(this.topicName))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    public String setTopic(String topic, String message) {
        Map<String, Object> map = this.gson.fromJson(message,mapType);
        map.put(this.topicName,topic);
        return this.gson.toJson(map);
    }

}
