package org.example.app.scraper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.Duration;


public class NftPostsScraper {
    private static final int MAX_POSTS = 50;
    private final WebDriver driver;
    private JSONArray postArray;
    private final WebDriverWait wait;
    private String nftUrl;

    public NftPostsScraper() {
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
        driver.get("https://thedefiant.io/nfts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/main/div")));
    }

    private JSONArray collectPostsFromPage() throws InterruptedException {
        JSONArray collectedPosts = new JSONArray();
        while (collectedPosts.length() < MAX_POSTS) {
            List<WebElement> posts = driver.findElements(By.xpath("/html/body/div[2]/main/div/div[3]/div[1]"));
            for (WebElement post : posts) {
                if (collectedPosts.length() >= MAX_POSTS) break;
                JSONObject postDetails = extractDetailsFromPostElement(post);
                collectedPosts.put(postDetails);
            }
            scrollToLoadMorePosts();
        }
        return collectedPosts;
    }

    private void scrollToLoadMorePosts() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000); // Wait for content to load
    }

    private JSONObject extractDetailsFromPostElement(WebElement post) {
        JSONObject postObject = new JSONObject();
        try {
            String username = post.findElement(By.xpath("/html/body/div[2]/main/div/div[3]/div[1]/div[2]/p/a")).getText();
            String title = post.findElement(By.xpath("/html/body/div[2]/main/div/div[3]/div[1]/div[2]/a[2]/h3")).getText();
            String url = post.findElement(By.xpath("/html/body/div[2]/main/div/div[3]/div[1]/div[2]/a[2]")).getAttribute("href");

            List<String> hashtags = extractHashtags(title);
            postObject.put("username", username);
            postObject.put("title", title);
            postObject.put("hashtags", hashtags);
            postObject.put("url", url);
        } catch (NoSuchElementException e) {
            System.out.println("Element not found");
        }
        return postObject;
    }

    private List<String> extractHashtags(String title) {
        List<String> hashtags = new ArrayList<>();
        Pattern hashtagPattern = Pattern.compile("/html/body/div[2]/main/div/div[3]/div[1]/div[2]/a[1]");
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
                JSONTokener tokener = new JSONTokener(reader);
                existingPosts = new JSONArray(tokener);
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
        NftPostsScraper scraper = new NftPostsScraper();
        try {
            scraper.scrapeNftData();
            scraper.saveScrapedDataToFile("src/main/resources/json/NftPostsData.json");
            System.out.println("Data scraped and saved successfully.");
        } catch (InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scraper.closeWebBrowser();
        }
    }
}
