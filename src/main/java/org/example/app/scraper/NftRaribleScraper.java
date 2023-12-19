package org.example.app.scraper;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.InterruptedException;

import org.example.app.entity.NftCollection;
import org.example.app.entity.NftMarketplace;

import org.openqa.selenium.By;
// import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class NftRaribleScraper extends NftScraper {

    public NftRaribleScraper() {
        super();
        this.setNftMarketplace(new NftMarketplace("Rarible"));
        this.setUrl("https://rarible.com");
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
        String[] cssSelectors = { "[data-marker='hot-collections-period-H1']",
                "[data-marker='hot-collections-period-DAY']", "[data-marker='hot-collections-period-WEEK']",
                "[data-marker='hot-collections-period-MONTH']" };
        String[] period = { "1h", "1d", "1w", "1m" };
        int index = 0;
        for (String cssSelector : cssSelectors) {
            List<NftCollection> danhSachNftCollection = new ArrayList<NftCollection>();

            // wait for button existed in DOM
            WebElement time = wait
                    .until(presenceOfElementLocated(By.cssSelector(cssSelector)));
            time.click();

            // wait for all loading collections
            for (int i = 1; i <= 11; i++)
                wait.until(elementToBeClickable(By.xpath(
                        "//div[@role='rowgroup']/div[1]/div/div[@class='ReactVirtualized__Grid__innerScrollContainer']/div["
                                + i + "]/a")));

            // get all collections
            List<WebElement> collections = driver.findElements(
                    By.xpath(
                            "//div[@role='rowgroup']/div[1]/div/div[@class='ReactVirtualized__Grid__innerScrollContainer']/child::div"));
            for (WebElement collection : collections) {
                try {
                    String rank = collection.findElement(By.xpath("./a/div/div/div[1]/span")).getText();
                    String name = collection.findElement(By.xpath("./a/div/div/div[2]/a/div/div[2]/span")).getText();
                    String floorPrice = collection.findElement(By.xpath("./a/div/div/div[3]/span")).getText();
                    String floorChange = collection.findElement(By.xpath("./a/div/div/div[4]/span")).getText();
                    String volume = collection.findElement(By.xpath("./a/div/div/div[5]/span")).getText();
                    String volumeChange = collection.findElement(By.xpath("./a/div/div/div[6]/span")).getText();
                    int items = Integer.parseInt(
                            collection.findElement(By.xpath("./a/div/div/div[7]/span")).getAttribute("data-value"));

                    int owners = Integer
                            .parseInt(collection.findElement(By.xpath("./a/div/div/div[8]/span"))
                                    .getAttribute("data-value"));

                    NftCollection nftCollection = new NftCollection(rank, name, floorPrice,
                            floorChange, volume,
                            volumeChange, items, owners);
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
