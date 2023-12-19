package org.example.app.JavaFx.objects;

public class hastag {
    private String username, content, datetime;
    private String[] hastags;

    public hastag(String username, String content, String[] hastags, String datetime) {
        this.username = username;
        this.content = content;
        this.hastags= hastags;
        this.datetime= datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getDateime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getHastags() {
        return hastags;
    }

    public void setHastags(String[] hastags) {
        this.hastags = hastags;
    }
}
