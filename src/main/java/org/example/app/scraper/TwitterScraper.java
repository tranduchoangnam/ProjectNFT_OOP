package org.example.app.scraper;

import org.example.app.entity.Twitter;
import org.example.app.utils.TwitterUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class TwitterScraper {
    private static final int MAX_TWEETS = 50;
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    public TwitterScraper() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.manage().deleteAllCookies();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.js = (JavascriptExecutor) driver;
    }

    public Map<String, Integer> scrapeTwitterData() throws InterruptedException {
        loginToTwitter("CandyHust76097", "dcm.lab05");
        List<String> names = readNamesFromJson("src/main/resources/json/Rarible.json");

        Map<String, Integer> hashtagTweetCounts = new HashMap<>();
        for (String name : names) {
            searchHashtagOnTwitter(name);
            List<Twitter> tweetsForName = collectTweetsFromSearchResults();
            hashtagTweetCounts.put(name, tweetsForName.size());
        }
        return hashtagTweetCounts;
    }

    private void loginToTwitter(String username, String password) {
        driver.get("http://twitter.com/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("text"))).sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']/ancestor::div[contains(@role, 'button')]"))).click();
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        passwordInput.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@data-testid='LoginForm_Login_Button']"))).click();
    }

    private void searchHashtagOnTwitter(String name) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Search query']")));
        searchBox.sendKeys(Keys.CONTROL + "a");
        searchBox.sendKeys(Keys.BACK_SPACE);
        searchBox.sendKeys(name + Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//article[@role='article']")));
    }

    private List<Twitter> collectTweetsFromSearchResults() throws InterruptedException {
        List<Twitter> tweets = new ArrayList<>();
        while (tweets.size() < MAX_TWEETS) {
            List<WebElement> tweetElements = driver.findElements(By.xpath("//article[@role='article']"));
            for (WebElement tweetElement : tweetElements) {
                if (tweets.size() >= MAX_TWEETS) break;
                Twitter tweet = extractDetailsFromTweetElement(tweetElement);
                tweets.add(tweet);
            }
            scrollToLoadMoreTweets();
        }
        return tweets;
    }

    private void scrollToLoadMoreTweets() throws InterruptedException {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000);
    }

    private Twitter extractDetailsFromTweetElement(WebElement tweetElement) {
        String username = tweetElement.findElement(By.xpath(".//div[contains(@data-testid,'User-Name')]//span")).getText();
        String content = tweetElement.findElement(By.xpath(".//div[@data-testid='tweetText']")).getText();
        String time = tweetElement.findElement(By.xpath(".//time")).getAttribute("datetime");
        String formattedTime = convertIsoToStandard(time);
        List<String> hashtags = TwitterUtils.extractHashtags(content);
        return new Twitter(content, formattedTime, hashtags, username);
    }
    private String extractTextWithDefault(WebElement baseElement, String xpath, String defaultValue) {
        try {
            return baseElement.findElement(By.xpath(xpath)).getText();
        } catch (NoSuchElementException e) {
            return defaultValue;
        }
    }

    private String extractAttributeWithDefault(WebElement baseElement, String xpath, String attributeName, String defaultValue) {
        try {
            return baseElement.findElement(By.xpath(xpath)).getAttribute(attributeName);
        } catch (NoSuchElementException e) {
            return defaultValue;
        }
    }
    public void closeWebBrowser() {
        driver.quit();
    }

    public static void main(String[] args) {
        TwitterScraper scraper = new TwitterScraper();
        try {
            Map<String, Integer> tweetCounts = scraper.scrapeTwitterData();
            for (Map.Entry<String, Integer> entry : tweetCounts.entrySet()) {
                System.out.println("Hashtag: " + entry.getKey() + ", Tweet count: " + entry.getValue());
            }
            System.out.println("Data scraped and analyzed successfully.");
        } catch (InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scraper.closeWebBrowser();
        }
    }

    public static String convertIsoToStandard(String isoDateTime) {
        ZonedDateTime zdt = ZonedDateTime.parse(isoDateTime);
        return zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private List<String> readNamesFromJson(String filePath) {
        List<String> names = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("1d");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String name = item.optString("name");
                names.add(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
}