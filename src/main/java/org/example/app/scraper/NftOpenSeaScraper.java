package org.example.app.scraper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.*;
import java.util.ArrayList;
import java.lang.InterruptedException;
import java.time.Duration;
import java.util.Set;

import org.example.app.entity.NftCollection;
import org.example.app.entity.NftMarketplace;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

public class NftOpenSeaScraper extends NftScraper {
    private final String[] period = { "1h", "6h", "1d", "1w" };
    private final String[] periodUrls = {
            "https://opensea.io/rankings/trending",
            "https://opensea.io/rankings/trending?sortBy=six_hour_volume",
            "https://opensea.io/rankings/trending?sortBy=one_day_volume",
            "https://opensea.io/rankings/trending?sortBy=seven_day_volume" };

    public NftOpenSeaScraper() {
        super();
        this.setNftMarketplace(new NftMarketplace("OpenSea"));
        this.setUrl("https://opensea.io/rankings/trending");
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
        int index = 0;
        // System.out.println(periodButtons.size());
        // scrapSubAction(getUrl(), driver, wait, period[0]);
        driver.quit();

        for (String periodUrl : periodUrls) {
            WebDriverManager.chromedriver().setup();
            WebDriver newDriver = new ChromeDriver();
            WebDriverWait newWait = new WebDriverWait(newDriver, Duration.ofSeconds(20));
            scrapSubAction(periodUrl, newDriver, newWait, period[index]);
            index++;
            newDriver.quit();

            // newDriver.quit();
            // this.setNftInPeriod(period[index], danhSachNftCollection);
            // index++;
        }
    }

    protected void scrapSubAction(String url, WebDriver driver, WebDriverWait wait, String period) {
        try {
            List<NftCollection> danhSachNftCollection = new ArrayList<NftCollection>();
            JavascriptExecutor js = (JavascriptExecutor) driver;

            driver.get(url);
            // wait for all loading collections
            Thread.sleep(3000);
            WebElement table = driver.findElement(By.xpath(
                    "//*[@id=\"main\"]/main/div/div/div[3]/div/div[4]"));
            Integer tableHeight = table.getSize().getHeight();
            Integer height = tableHeight / 4;
            // List<Integer> names = new ArrayList<Integer>();
            Pattern pattern = Pattern.compile(".*\\d+.*");
            for (int i = 1; i <= 5; i++) {
                List<WebElement> collections = table.findElements(By.xpath("./div"));
                for (WebElement collection : collections) {
                    String content = collection.getText();
                    String[] lines = content.split("\n");

                    // System.out.println(nftCollection.toString());
                    Matcher matcher = pattern.matcher(lines[0]);
                    if (!matcher.matches())
                        continue;

                    NftCollection nftCollection = new NftCollection(lines[0], lines[1], lines[2], "-", lines[4],
                            lines[3], -1, -1);
                    danhSachNftCollection.add(nftCollection);
                }
                Collections.sort(danhSachNftCollection, new Comparator<NftCollection>() {
                    @Override
                    public int compare(NftCollection o1, NftCollection o2) {
                        int rank1 = Integer.parseInt(o1.getRank());
                        int rank2 = Integer.parseInt(o2.getRank());
                        return rank1 - rank2;
                    }
                });
                Set<NftCollection> uniqueSet = new LinkedHashSet<>(danhSachNftCollection);
                danhSachNftCollection = new ArrayList<>(uniqueSet);
                js.executeScript("window.scrollBy(0," + height.toString() + ")");
                Thread.sleep(2000);
            }
            this.setNftInPeriod(period, danhSachNftCollection);
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
