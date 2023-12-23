package org.example.app.service;

import org.example.app.entity.Tweet;
import org.example.app.utils.JsonReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HotHashtagService {
    public static Map<String, Map<String, Integer>> countHashtagsByTimeFrame(List<Tweet> tweets, String timeFrame) {
        Map<String, Map<String, Integer>> hashtagCountsByTimeFrame = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Tweet tweet : tweets) {
            LocalDateTime dateTime = LocalDateTime.parse(tweet.getTime(), formatter);
            String timeKey;
            switch (timeFrame) {
                case "DAY":
                    timeKey = dateTime.toLocalDate().toString();
                    break;
                case "WEEK":
                    timeKey = dateTime.format(DateTimeFormatter.ofPattern("YYYY-'W'ww", Locale.getDefault()));
                    break;
                case "MONTH":
                    timeKey = dateTime.format(DateTimeFormatter.ofPattern("YYYY-MM"));
                    break;
                case "YEAR":
                    timeKey = dateTime.format(DateTimeFormatter.ofPattern("YYYY"));
                    break;
                default:
                    timeKey = dateTime.toLocalDate().toString();
            }

            hashtagCountsByTimeFrame.putIfAbsent(timeKey, new HashMap<>());
            Map<String, Integer> timeFrameHashtagCounts = hashtagCountsByTimeFrame.get(timeKey);

            for (String hashtag : tweet.getHashtags()) {
                timeFrameHashtagCounts.put(hashtag, timeFrameHashtagCounts.getOrDefault(hashtag, 0) + 1);
            }
        }
        return hashtagCountsByTimeFrame;
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

    public static String getMostPopularHashtagByFrame(Map<String, Map<String, Integer>> hashtagData,
            String timeFrameKey,
            String timeFrameName) {
        if (hashtagData != null && hashtagData.containsKey(timeFrameKey)) {
            String mostPopularHashtag = HotHashtagService.findMostPopularHashtag(hashtagData.get(timeFrameKey));
            return mostPopularHashtag;
        } else {
            return "No data";
        }
    }

    public static List<String> getMostPopularHashtags() throws IOException {
        List<Tweet> tweets = JsonReader.readFileTwitterData();
        LocalDate currentDate = LocalDate.now();
        String currentDay = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentWeek = currentDate.format(DateTimeFormatter.ofPattern("YYYY-'W'ww",
                Locale.getDefault()));
        String currentMonth = currentDate.format(DateTimeFormatter.ofPattern("YYYY-MM"));
        String currentYear = currentDate.format(DateTimeFormatter.ofPattern("YYYY"));

        Map<String, Map<String, Integer>> dailyHashtags = HotHashtagService.countHashtagsByTimeFrame(tweets, "DAY");
        Map<String, Map<String, Integer>> weeklyHashtags = HotHashtagService.countHashtagsByTimeFrame(tweets,
                "WEEK");
        Map<String, Map<String, Integer>> monthlyHashtags = HotHashtagService.countHashtagsByTimeFrame(tweets,
                "MONTH");
        Map<String, Map<String, Integer>> yearlyHashtags = HotHashtagService.countHashtagsByTimeFrame(tweets,
                "YEAR");

        String daily = getMostPopularHashtagByFrame(dailyHashtags, currentDay, "Daily");
        String weekly = getMostPopularHashtagByFrame(weeklyHashtags, currentWeek, "Weekly");
        String monthly = getMostPopularHashtagByFrame(monthlyHashtags, currentMonth, "Monthly");
        String yearly = getMostPopularHashtagByFrame(yearlyHashtags, currentYear, "Yearly");
        List<String> mostPopularHashtags = new ArrayList<>();
        mostPopularHashtags.add(daily);
        mostPopularHashtags.add(weekly);
        mostPopularHashtags.add(monthly);
        mostPopularHashtags.add(yearly);
        return mostPopularHashtags;
    }
}
