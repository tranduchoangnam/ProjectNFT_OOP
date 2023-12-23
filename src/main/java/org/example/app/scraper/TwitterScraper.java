package org.example.app.scraper;

import org.example.app.entity.Tweet;
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
import java.util.HashSet;
import java.util.Set;

public class TwitterScraper implements Scrapping {
    private static final int MAX_TWEETS = 15;
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public TwitterScraper() throws InterruptedException {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.manage().deleteAllCookies();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.js = (JavascriptExecutor) driver;
        scrap();
    }

    public List<Tweet> scrapeTwitterData() throws InterruptedException {
        loginToTwitter("marktraa2149", "dcm.lab05");
        List<String> names = readNamesFromJson("src/main/resources/json/Rarible.json");

        List<Tweet> allTweets = new ArrayList<>();
        for (String name : names) {
            try {
                searchHashtagOnTwitter(name);
                List<Tweet> tweets = collectTweetsFromSearchResults();
                if (tweets != null) {
                    allTweets.addAll(tweets);
                }
            } catch (TimeoutException e) {
                System.out.println("Timeout occurred for hashtag: " + name);
                break;
            }

            if (isErrorDisplayed()) {
                System.out.println("Error detected, stopping data collection.");
                break;
            }
        }
        TwitterUtils.writeTweetsToFile(allTweets, "TwitterData.json");
        return allTweets;
    }

    private void loginToTwitter(String username, String password) {
        driver.get("http://twitter.com/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("text"))).sendKeys(username);
        wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//span[text()='Next']/ancestor::div[contains(@role, 'button')]")))
                .click();
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        passwordInput.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@data-testid='LoginForm_Login_Button']")))
                .click();
    }

    private void searchHashtagOnTwitter(String name) {
        try {
            WebElement searchBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Search query']")));
            clearSearchBox(searchBox);
            searchBox.sendKeys(name + Keys.ENTER);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//article[@role='article']")));
        } catch (StaleElementReferenceException e) {
            WebElement searchBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Search query']")));
            clearSearchBox(searchBox);
            searchBox.sendKeys(name + Keys.ENTER);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//article[@role='article']")));
        }
    }

    private void clearSearchBox(WebElement searchBox) {
        searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        searchBox.sendKeys(Keys.DELETE);
    }

    private String createTweetIdentifier(Tweet tweet) {
        return tweet.getUsername() + "|" + tweet.getTime() + "|" + tweet.getContent();
    }

    private boolean isErrorDisplayed() {
        try {
            WebElement errorElement = driver
                    .findElement(By.xpath("//*[contains(text(), 'Something went wrong. Try reloading.')]"));
            return errorElement != null && errorElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private List<Tweet> collectTweetsFromSearchResults() throws InterruptedException {
        List<Tweet> tweets = new ArrayList<>();
        Set<String> tweetIdentifiers = new HashSet<>();
        boolean isEndOfPageReached = false;

        while (tweets.size() < MAX_TWEETS && !isEndOfPageReached) {
            if (isErrorDisplayed()) {
                System.out.println("Error detected: 'Something went wrong. Try reloading.'");
                return tweets;
            }
            long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

            List<WebElement> tweetElements = driver.findElements(By.xpath("//article[@role='article']"));
            for (WebElement tweetElement : tweetElements) {
                if (tweets.size() >= MAX_TWEETS)
                    break;
                Tweet tweet = extractDetailsFromTweetElement(tweetElement);
                if (tweet != null) {
                    String tweetId = createTweetIdentifier(tweet);
                    if (!tweetIdentifiers.contains(tweetId)) {
                        tweets.add(tweet);
                        tweetIdentifiers.add(tweetId);
                    }
                }
            }
            scrollToLoadMoreTweets();
            Thread.sleep(2000);

            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) {
                isEndOfPageReached = true;
            }
        }

        return tweets;
    }

    private void scrollToLoadMoreTweets() throws InterruptedException {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000); // Wait for content to load
    }

    private Tweet extractDetailsFromTweetElement(WebElement tweetElement) {
        try {
            String username = findElementWithRetry(tweetElement,
                    By.xpath(".//div[contains(@data-testid,'User-Name')]//span"), 3).getText();
            String content = findElementWithRetry(tweetElement, By.xpath(".//div[@data-testid='tweetText']"), 3)
                    .getText();
            String time = findElementWithRetry(tweetElement, By.xpath(".//time"), 3).getAttribute("datetime");
            String formattedTime = convertIsoToStandard(time);
            List<String> hashtags = TwitterUtils.extractHashtags(content);
            return new Tweet(content, username, formattedTime, hashtags);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            System.out.println("Some elements were not found in this tweet.");
            return null;
        }
    }

    private WebElement findElementWithRetry(WebElement tweetElement, By by, int retries) {
        WebElement element = null;
        while (retries > 0) {
            try {
                element = driver.findElement(by);
                return element;
            } catch (StaleElementReferenceException e) {
                retries--;
            }
        }
        throw new NoSuchElementException("Element not found after retrying.");
    }

    public void closeWebBrowser() {
        driver.quit();
    }

    @Override
    public void scrap() throws InterruptedException {
        try {
            List<Tweet> tweets = scrapeTwitterData();
            TwitterUtils.writeTweetsToFile(tweets, "TwitterData.json");

        } catch (InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeWebBrowser();
        }
    }

    public static String convertIsoToStandard(String isoDateTime) {
        ZonedDateTime zdt = ZonedDateTime.parse(isoDateTime);
        return zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private List<String> readNamesFromJson(String filePath) {
        Set<String> namesSet = new HashSet<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(content);

            addNamesFromJsonArray(namesSet, jsonObject, "1d");
            addNamesFromJsonArray(namesSet, jsonObject, "1w");
            addNamesFromJsonArray(namesSet, jsonObject, "1m");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(namesSet);
    }

    private void addNamesFromJsonArray(Set<String> namesSet, JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String name = item.optString("name");
                namesSet.add(name);
            }
        }
    }
}