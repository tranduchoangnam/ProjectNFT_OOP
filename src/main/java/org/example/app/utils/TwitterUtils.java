package org.example.app.utils;

import org.example.app.entity.Twitter;
import org.json.JSONArray;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        public static void writeTweetsToFile(List<Twitter> tweets, String filename) {
            JSONArray jsonArray = new JSONArray();
            for (Twitter tweet : tweets) {
                jsonArray.put(tweet.toJSONObject());
            }
            String path = Paths.get("src/main/resources/json",filename).toString();
            try (FileWriter file = new FileWriter(path)) {
                file.write(jsonArray.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static Map<String, Map<String, Integer>> countHashtagsByDate(List<Twitter> tweets) {
            Map<String, Map<String, Integer>> hashtagCountsByDate = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Twitter tweet : tweets) {
                LocalDateTime dateTime = LocalDateTime.parse(tweet.getTime(), formatter);
                String dateString = dateTime.toLocalDate().toString(); // Chuyển đổi thành LocalDate

                hashtagCountsByDate.putIfAbsent(dateString, new HashMap<>());
                Map<String, Integer> dailyHashtagCounts = hashtagCountsByDate.get(dateString);

                for (String hashtag : tweet.getHashtags()) {
                    dailyHashtagCounts.put(hashtag, dailyHashtagCounts.getOrDefault(hashtag, 0) + 1);
                }
            }
            return hashtagCountsByDate;
        }
        public static String findMostPopularHashtag(Map<String, Integer> hashtagCounts) {
            String mostPopularHashtag = null;
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : hashtagCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    mostPopularHashtag = entry.getKey();
                    maxCount = entry.getValue();
                }
            }
            return mostPopularHashtag;
        }
    }

