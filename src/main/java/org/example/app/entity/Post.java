package org.example.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.List;

public class Post extends SocialMedia {
    private String title;
    private String url;

    @JsonCreator
    public Post(@JsonProperty("title") String title,
            @JsonProperty("url") String url,
            @JsonProperty("username") String username,
            @JsonProperty("time") String time,
            @JsonProperty("hashtags") List<String> hashtags) {
        super(username, time, hashtags);
        this.title = title;
        this.url = url;
    }

    public Post() {
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

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("url", url);
        obj.put("username", getUsername());
        obj.put("time", getTime());
        obj.put("hashtags", getHashtags());
        return obj;
    }
}
