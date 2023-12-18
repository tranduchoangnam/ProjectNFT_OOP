package org.example.app.scraper;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.InterruptedException;

import org.example.app.entity.NftCollection;
import org.example.app.entity.NftMarketplace;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class NftBinanceScraper extends NftScraper {

    public NftBinanceScraper() {
        super();
        this.setNftMarketplace(new NftMarketplace("Binance"));
        this.setUrl("https://www.binance.com/en/nft/home");
        this.setName();
        try {
            scrap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, List<NftCollection>> getNftInPeriod() {
        return this.getNftMarketplace().getNftInPeriod();
    }

    @Override
    protected void scrapAction(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        // find trending collections in 1 week
        String[] period = { "1h", "6h", "1d", "1w" };
        int index = 0;

        WebElement acceptButton = wait
                .until(elementToBeClickable(By.cssSelector("button.css-1s94759")));
        acceptButton.click();

        WebElement time = wait
                .until(presenceOfElementLocated(
                        By.xpath("//*[@id='__APP']/div/div[2]/main/div/div[2]/div[2]/div/div/div[3]/div[2]")));
        List<WebElement> periodButtons = time.findElements(By.tagName("div"));

        for (WebElement periodButton : periodButtons) {
            List<NftCollection> danhSachNftCollection = new ArrayList<NftCollection>();

            periodButton.click();

            // wait for all loading collections
            wait.until(presenceOfElementLocated(
                    By.xpath(
                            "//*[@id=\"__APP\"]/div/div[2]/main/div/div[2]/div[2]/div/div/div[1]/div[2]/div/div[2]/div[10]/div/div[1]/div[3]/div/div[1]")));
            List<WebElement> trendingCollumns = driver.findElements(By.cssSelector("div.css-m3366f"));
            List<WebElement> loadedCollections = new ArrayList<WebElement>();
            for (WebElement trendingCollumn : trendingCollumns) {
                List<WebElement> listNft = trendingCollumn.findElements(By.xpath("./div"));
                for (WebElement nft : listNft) {
                    wait.until(visibilityOf(nft.findElement(By.xpath("./div/div[1]/div[3]"))));
                    loadedCollections.add(nft);
                }
            }

            int count = 0;
            for (WebElement collection : loadedCollections) {
                try {
                    count = count + 1;
                    String rank = Integer.toString(count);
                    String name = collection.findElement(By.xpath("./div/div[1]/div[3]")).getText();
                    String floorPrice = collection.findElement(By.xpath("./div/div[2]/div[1]")).getText();
                    String floorChange = collection.findElement(By.xpath("./div/div[2]/div[2]")).getText().replace("\n",
                            "");
                    String volume = collection.findElement(By.xpath("./div/div[3]/div[1]")).getText();
                    String volumeChange = collection.findElement(By.xpath("./div/div[3]/div[2]")).getText()
                            .replace("\n", "");

                    NftCollection nftCollection = new NftCollection(rank, name, floorPrice,
                            floorChange, volume,
                            volumeChange, -1, -1);
                    danhSachNftCollection.add(nftCollection);
                } catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
            }
            this.setNftInPeriod(period[index], danhSachNftCollection);
            index++;
        }
    }

    // private static void print(String msg, Object... args) {
    // System.out.println(String.format(msg, args));
    // }
}
