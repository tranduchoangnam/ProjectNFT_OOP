package org.example.app.service;

import org.example.app.entity.Tweet;
import org.example.app.utils.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterService {
    private List<Tweet> allTweets;

    public TwitterService() {
        try {
            allTweets = JsonReader.readFileTwitterData();
        } catch (IOException e) {
            e.printStackTrace();
            allTweets = new ArrayList<>();
        }
    }
    public List<Tweet> searchTweetsByHashtag(String hashtag) {
        List<Tweet> filteredTweets = new ArrayList<>();
        if (allTweets == null || hashtag == null || hashtag.isEmpty()) {
            return filteredTweets;
        }

        for (Tweet tweet : allTweets) {
            if (tweet != null && tweet.getHashtags() != null && tweet.getHashtags().contains(hashtag)) {
                filteredTweets.add(tweet);
            }
        }
        return filteredTweets;
    }
}

