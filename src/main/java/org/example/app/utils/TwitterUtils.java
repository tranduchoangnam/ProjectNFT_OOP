package org.example.app.utils;

import org.example.app.entity.Tweet;
import org.json.JSONArray;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterUtils {

    public static List<String> extractHashtags(String content) {
        List<String> hashtags = new ArrayList<>();
        Pattern hashtagPattern = Pattern.compile("#\\w+");
        Matcher matcher = hashtagPattern.matcher(content);
        while (matcher.find()) {
            hashtags.add(matcher.group());
        }
        return hashtags;
    }

    public static void writeTweetsToFile(List<Tweet> tweets, String filename) {
        JSONArray jsonArray = new JSONArray();
        for (Tweet tweet : tweets) {
            jsonArray.put(tweet.toJSONObject());
        }
        String path = Paths.get("src/main/resources/json", filename).toString();
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonArray.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}