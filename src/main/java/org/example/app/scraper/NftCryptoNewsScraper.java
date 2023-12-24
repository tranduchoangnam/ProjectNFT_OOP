package org.example.app.scraper;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NftCryptoNewsScraper {
    private static final int MAX_POSTS = 20; // Giả sử chỉ cần lấy 20 bài
    private final WebDriver driver;
    private JSONArray postArray;
    private final WebDriverWait wait;
    private String nftUrl;

    public NftCryptoNewsScraper() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.manage().deleteAllCookies();
        this.postArray = new JSONArray();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    public void scrapeNftData() throws InterruptedException {
        navigateToNftPage(nftUrl);
        this.postArray = collectPostsFromPage();
    }

    private void navigateToNftPage(String nftUrl) {
        driver.get("https://cryptonews.net/news/nft/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/div/div/section")));
    }

    private JSONArray collectPostsFromPage() throws InterruptedException {
        JSONArray collectedPosts = new JSONArray();

        List<WebElement> posts = driver.findElements(By.xpath("/html/body/main/div/div/section/div"));
        System.out.println("Number of posts found: " + posts.size());

        for (int i = 0; i < Math.min(MAX_POSTS, posts.size()); i++) {
            WebElement post = posts.get(i);
            JSONObject postDetails = extractDetailsFromPostElement(post);
            collectedPosts.put(postDetails);
        }

        return collectedPosts;
    }

    private JSONObject extractDetailsFromPostElement(WebElement post) {
        JSONObject postObject = new JSONObject();
        try {
            String username = post.findElement(By.xpath(".//div/div/div/div/span[1]")).getText();
            String title = post.findElement(By.xpath(".//div/a")).getText();
            String time = post.findElement(By.xpath(".//div/div/div/div/span[2]")).getText();
            String url = post.findElement(By.xpath(".//div/a")).getAttribute("href");

            List<String> hashtags = extractHashtags(title);
            postObject.put("username", username);
            postObject.put("title", title);
            postObject.put("hashtags", hashtags);
            postObject.put("url", url);
            postObject.put("time", time);

            System.out.println("Post details: " + postObject.toString());
        } catch (NoSuchElementException e) {
            System.out.println("Element not found");
        }
        return postObject;
    }

    private List<String> extractHashtags(String title) {
        List<String> hashtags = new ArrayList<>();
        Pattern hashtagPattern = Pattern.compile(".//div[2]/a[1]");
        Matcher matcher = hashtagPattern.matcher(title);
        while (matcher.find()) {
            hashtags.add(matcher.group());
        }
        return hashtags;
    }

    public void saveScrapedDataToFile(String filename) {
        JSONArray existingPosts = readPostsFromFile(filename);
        Set<String> existingPostDetails = new HashSet<>();

        for (int i = 0; i < existingPosts.length(); i++) {
            String postDetail = createPostDetailString(existingPosts.getJSONObject(i));
            existingPostDetails.add(postDetail);
        }

        for (int i = 0; i < postArray.length(); i++) {
            JSONObject post = postArray.getJSONObject(i);
            String detail = createPostDetailString(post);
            if (!existingPostDetails.contains(detail)) {
                existingPosts.put(post);
            }
        }

        writePostsToFile(existingPosts, filename);
    }

    private String createPostDetailString(JSONObject post) {
        return post.optString("username", "") +
                post.optString("title", "") +
                post.optString("url", "") +
                post.optString("time", "") +
                post.optString("hashtags", "");
    }

    private void writePostsToFile(JSONArray posts, String filename) {
        try (FileWriter file = new FileWriter(filename, false)) {
            file.write(posts.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray readPostsFromFile(String filename) {
        File file = new File(filename);
        JSONArray existingPosts = new JSONArray();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                existingPosts = new JSONArray(new JSONTokener(reader));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return existingPosts;
    }

    public void closeWebBrowser() {
        driver.quit();
    }

    public static void main(String[] args) {
        NftCryptoNewsScraper scraper = new NftCryptoNewsScraper();
        try {
            scraper.scrapeNftData();
            scraper.saveScrapedDataToFile("src/main/resources/json/NftCryptoNewsData.json");
            System.out.println("Data scraped and saved successfully.");
        } catch (InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scraper.closeWebBrowser();
        }
    }
}
