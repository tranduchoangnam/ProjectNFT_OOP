package org.example.app.scraper;

import org.example.app.entity.Post;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PostDefiantScraper extends PostScraper {

    public PostDefiantScraper() throws InterruptedException {
        super();
        scrap();
    }

    @Override
    protected void navigateToNftPage(String nftUrl) {
        getDriver().get("https://thedefiant.io/nfts");
        getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/main/div")));
    }

    @Override
    protected List<Post> collectPostsFromPage() throws InterruptedException {
        List<Post> collectedPosts = new ArrayList<Post>();

        for (int loopCount = 1; loopCount <= MAX_LOOP; loopCount++) {
            try {
                WebElement exitButton = getDriver()
                        .findElement(By.xpath("//*[@id=\"headlessui-dialog-panel-:r1:\"]/div[2]/div[2]/button"));
                exitButton.click();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Loop " + loopCount + ":");
            List<WebElement> posts = getDriver().findElements(By.xpath("/html/body/div[2]/main/div/div[3]/div"));
            System.out.println("Number of posts found: " + posts.size());

            for (WebElement post : posts) {
                JSONObject obj = extractDetailsFromPostElement(post);
                if (obj == null)
                    continue;
                JSONArray jsonArray = obj.getJSONArray("hashtags");

                // Converting JSONArray to List<String>
                List<String> stringList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    stringList.add(jsonArray.getString(i));
                }
                Post postDetails = new Post(obj.getString("title"), obj.getString("username"), obj.getString("time"),
                        obj.getString("url"), stringList);
                if (postDetails != null)
                    collectedPosts.add(postDetails);
            }
            scrollToLoadMorePosts();
        }
        return collectedPosts;
    }

    @Override
    protected JSONObject extractDetailsFromPostElement(WebElement post) {
        JSONObject postObject = new JSONObject();
        try {
            String username = post.findElement(By.xpath(".//div[2]/p/a")).getText();
            String title = post.findElement(By.xpath(".//div[2]/a[2]/h3")).getText();
            String time = post.findElement(By.xpath(".//div[2]/p/span[2]")).getText();
            String url = post.findElement(By.xpath(".//div[2]/a[2]")).getAttribute("href");

            List<String> hashtags = extractHashtags(title);
            postObject.put("username", username);
            postObject.put("title", title);
            postObject.put("hashtags", hashtags);
            postObject.put("url", url);
            postObject.put("time", time);

            // System.out.println("Post details: " + postObject.toString());
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

    // @Override
    // public void scrap() {
    // try {
    // navigateToNftPage(nftUrl);
    // this.postArray = collectPostsFromPage();
    // saveScrapedDataToFile("Post.json");
    // System.out.println(postArray);
    // } catch (InterruptedException e) {
    // System.err.println("Error occurred: " + e.getMessage());
    // e.printStackTrace();
    // } finally {
    // this.closeWebBrowser();
    // }
    // }
}
