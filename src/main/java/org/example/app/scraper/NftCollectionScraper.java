package org.example.app.scraper;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.InterruptedException;

import org.example.app.entity.NftCollection;
import org.example.app.entity.NftMarketplace;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
// import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.time.Duration;

public class NftCollectionScraper {
    private final String URL = "https://rarible.com";
    private NftMarketplace nftMarketplace;

    public NftCollectionScraper() {
        this.nftMarketplace = new NftMarketplace("Rarible");
        // scrap();
        try {
            scrap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.nftMarketplace.getName();
    }

    public HashMap<String, List<NftCollection>> getNftInPeriod() {
        return this.nftMarketplace.getNftInPeriod();
    }

    public void scrap() throws InterruptedException {
        HashMap<String, List<NftCollection>> nftInPeriod = new HashMap<String, List<NftCollection>>();

        // Selenium setup
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(URL);

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
            nftInPeriod.put(period[index], danhSachNftCollection);
            index++;
        }
        this.nftMarketplace.setNftInPeriod(nftInPeriod);
        Thread.sleep(2000);
        driver.quit();
    }

    // private static void print(String msg, Object... args) {
    // System.out.println(String.format(msg, args));
    // }
}
// demo crawl map location

// jsonObject.put("map", hrefs.get(1).attr("href"));

// String locationName = product.select(".location-name").text();
// jsonObject.put("name", locationName);

// // Extract and print the location address
// String locationAddress = product.select(".location-address").text();
// jsonObject.put("address", locationAddress);

// // Extract and print the location hours open
// String locationHoursOpen = product.select(".location-hours-open").text();
// jsonObject.put("hoursOpen", locationHoursOpen);

// Elements hrefs = product.select("a[href]");
// jsonObject.put("tel", hrefs.get(0).text());
// jsonObject.put("map", hrefs.get(1).attr("href"));
// System.out.println(jsonObject.toString() + ',');

// demo search
// driver.findElement(By.name("q")).sendKeys("cheese" + Keys.ENTER);
// WebElement firstResult =
// wait.until(presenceOfElementLocated(By.cssSelector("h3>div")));
// System.out.println(firstResult.getAttribute("textContent"));