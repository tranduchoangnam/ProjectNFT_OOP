package org.example.app.scraper;

import org.example.app.entity.Post;
import org.example.app.utils.JsonReader;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.time.Duration;

public abstract class PostScraper implements Scrapping {
    public static final int MAX_LOOP = 100;
    private final WebDriver driver;
    private List<Post> postArray;
    private final WebDriverWait wait;
    private String nftUrl;

    public PostScraper() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.manage().deleteAllCookies();
        this.postArray = new ArrayList<Post>();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    public List<Post> getPostArray() {
        return postArray;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    protected abstract void navigateToNftPage(String nftUrl);

    protected abstract List<Post> collectPostsFromPage() throws InterruptedException;

    protected void scrollToLoadMorePosts() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(4000); // Wait for content to load
    }

    protected abstract JSONObject extractDetailsFromPostElement(WebElement post);

    protected abstract List<String> extractHashtags(String title);

    protected void saveScrapedDataToFile(String filename) {

        List<Post> existingPosts = readPostsFromFile(filename);
        Set<String> existingPostDetails = new HashSet<>();

        if (existingPosts != null) {
            for (int i = 0; i < existingPosts.size(); i++) {
                String postDetail = existingPosts.get(i).getUrl();
                existingPostDetails.add(postDetail);
            }
        } else {
            existingPosts = new ArrayList<Post>();
        }

        for (int i = 0; i < postArray.size(); i++) {
            Post post = postArray.get(i);
            String detail = post.getUrl();
            if (!existingPostDetails.contains(detail)) {
                existingPosts.add(post);
            }
        }

        this.postArray = existingPosts;
    }

    protected List<Post> readPostsFromFile(String filename) {
        try {
            List<Post> existingPosts = new ArrayList<Post>();
            existingPosts = JsonReader.readFilePost(filename);
            return existingPosts;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void closeWebBrowser() {
        driver.quit();
    }

    @Override
    public void scrap() throws InterruptedException {
        try {
            navigateToNftPage(nftUrl);
            this.postArray = collectPostsFromPage();
            saveScrapedDataToFile("Post.json");
            System.out.println(postArray);
        } catch (InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            this.closeWebBrowser();
        }
    }
}
