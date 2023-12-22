package org.example.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.List;

public class Tweet extends SocialMedia {
    private String content;

    @JsonCreator
    public Tweet (@JsonProperty("content") String content,
                   @JsonProperty("username") String username,
                   @JsonProperty("time") String time,
                   @JsonProperty("hashtags") List<String> hashtags) {
        super(username, time, hashtags);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("content", content);
        obj.put("username", getUsername());
        obj.put("time", getTime());
        obj.put("hashtags", getHashtags());
        return obj;
    }
}
