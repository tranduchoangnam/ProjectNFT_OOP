package org.example.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.List;

public class Twitter {
    private String content;
    private String time;
    private List<String> hashtags;
    private String username;
    @JsonCreator
    public Twitter(@JsonProperty("content") String content,
                   @JsonProperty("time") String time,
                   @JsonProperty("hashtags") List<String> hashtags,
                   @JsonProperty("username") String username) {
        this.content = content;
        this.time = time;
        this.hashtags = hashtags;
        this.username = username;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public List<String> getHashtags() {
        return hashtags;
    }
    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }
    public String getUsername() {
        return  username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("content", content);
        obj.put("time", time);
        obj.put("hashtags", hashtags);
        obj.put("username", username);
        return obj;
    }
}