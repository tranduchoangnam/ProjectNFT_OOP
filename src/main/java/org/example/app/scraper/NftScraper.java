package org.example.app.scraper;

import java.util.HashMap;
import java.util.List;
import java.lang.InterruptedException;

import org.example.app.entity.NftCollection;
import org.example.app.entity.NftMarketplace;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public abstract class NftScraper {
    private String url;
    private String name;
    private NftMarketplace nftMarketplace;
    private HashMap<String, List<NftCollection>> nftInPeriod = new HashMap<String, List<NftCollection>>();

    public NftScraper() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = nftMarketplace.getName();
    }

    public NftMarketplace getNftMarketplace() {
        return nftMarketplace;
    }

    public void setNftMarketplace(NftMarketplace nftMarketplace) {
        this.nftMarketplace = nftMarketplace;
    }

    public HashMap<String, List<NftCollection>> getNftInPeriod() {
        return this.nftMarketplace.getNftInPeriod();
    }

    public void setNftInPeriod(String period, List<NftCollection> nftCollection) {
        this.nftInPeriod.put(period, nftCollection);
    }

    public void scrap() throws InterruptedException {
        // Selenium setup
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(url);
        scrapAction(driver, wait);

        this.nftMarketplace.setNftInPeriod(nftInPeriod);
        Thread.sleep(2000);
        driver.quit();
    }

    protected abstract void scrapAction(WebDriver driver, WebDriverWait wait)
            throws InterruptedException;
}
