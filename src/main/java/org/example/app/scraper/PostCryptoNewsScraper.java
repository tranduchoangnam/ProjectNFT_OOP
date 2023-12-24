package org.example.app.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.example.app.entity.Post;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostCryptoNewsScraper extends PostScraper {

    public PostCryptoNewsScraper() throws InterruptedException {
        super();
        scrap();
    }

    @Override
    protected void navigateToNftPage(String nftUrl) {
        getDriver().get("https://cryptonews.net/news/nft/");
        getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/div/div/section")));
    }

    @Override
    protected List<Post> collectPostsFromPage() throws InterruptedException {
        List<Post> collectedPosts = new ArrayList<Post>();

        List<WebElement> posts = getDriver().findElements(By.xpath("/html/body/main/div/div/section/div"));
        System.out.println("Number of posts found: " + posts.size());

        for (int i = 0; i < Math.min(MAX_LOOP, posts.size()); i++) {
            WebElement post = posts.get(i);
            JSONObject obj = extractDetailsFromPostElement(post);
            if (obj == null)
                continue;
            JSONArray jsonArray = obj.getJSONArray("hashtags");

            // Converting JSONArray to List<String>
            List<String> stringList = new ArrayList<>();
            for (int j = 0; j < jsonArray.length(); j++) {
                stringList.add(jsonArray.getString(i));
            }
            Post postDetails = new Post(obj.getString("title"), obj.getString("username"), obj.getString("time"),
                    obj.getString("url"), stringList);
            if (postDetails != null)
                collectedPosts.add(postDetails);
        }

        return collectedPosts;
    }

    @Override
    protected JSONObject extractDetailsFromPostElement(WebElement post) {
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
            return postObject;

        } catch (NoSuchElementException e) {
            System.out.println("Element not found");
            return null;
        }
    }

    @Override
    protected List<String> extractHashtags(String title) {
        List<String> hashtags = new ArrayList<>();
        Pattern hashtagPattern = Pattern.compile(".//div[2]/a[1]");
        Matcher matcher = hashtagPattern.matcher(title);
        while (matcher.find()) {
            hashtags.add(matcher.group());
        }
        return hashtags;
    }
}
