package org.example.app.service;

import org.example.app.entity.Twitter;
import org.example.app.utils.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterService {
    private List<Twitter> allTweets;

    public TwitterService() {
        try {
            allTweets = JsonReader.readFileTwitterData();
        } catch (IOException e) {
            e.printStackTrace();
            allTweets = new ArrayList<>();
        }
    }
    public List<Twitter> searchTweetsByHashtag(String hashtag) {
        List<Twitter> filteredTweets = new ArrayList<>();
        if (allTweets == null || hashtag == null || hashtag.isEmpty()) {
            return filteredTweets;
        }

        for (Twitter tweet : allTweets) {
            if (tweet != null && tweet.getHashtags() != null && tweet.getHashtags().contains(hashtag)) {
                filteredTweets.add(tweet);
            }
        }
        return filteredTweets;
    }
}

