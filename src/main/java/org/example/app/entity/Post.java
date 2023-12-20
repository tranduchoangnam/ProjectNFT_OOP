package org.example.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.List;

public class Post {
    private String title;
    private String url;
    private String username;
    private String time;
    private List<String> hashtags;

    @JsonCreator
    public Post(@JsonProperty("title") String title,
                @JsonProperty("url") String url,
                @JsonProperty("username") String username,
                @JsonProperty("time") String time,
                @JsonProperty("hashtags") List<String> hashtags){
        this.title = title;
        this.url = url;
        this.username = username;
        this.hashtags = hashtags;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }
    public String getTime(){
        return time;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("url", url);
        obj.put("username", username);
        obj.put("hashtags", hashtags);
        obj.put("time", time);
        return obj;
    }
}
