package org.example.app.entity;

import java.util.List;

import org.json.JSONObject;

public abstract class SocialMedia {
    protected String username;
    protected String time;
    protected List<String> hashtags;

    public SocialMedia(String username, String time, List<String> hashtags) {
        this.username = username;
        this.time = time;
        this.hashtags = hashtags;
    }

    public SocialMedia() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public abstract JSONObject toJSONObject();

}
