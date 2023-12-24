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
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class NftNiftyGatewayScraper extends NftScraper {

    public NftNiftyGatewayScraper() {
        super();
        this.setNftMarketplace(new NftMarketplace("NiftyGateway"));
        this.setUrl("https://www.niftygateway.com/rankings");
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
        String[] period = { "1d", "1w", "1m", "all" };
        int index = 0;

        wait.until(elementToBeClickable(By.xpath(
                "//*[@id=\"root\"]/div/div/div[2]/div[1]/div[1]/div[4]/button")));
        WebElement time = driver.findElement(By.xpath(
                "//*[@id=\"root\"]/div/div/div[2]/div[1]/div[1]"));
        List<WebElement> periodButtons = time.findElements(By.tagName("button"));
        // System.out.println(periodButtons.size());
        for (WebElement periodButton : periodButtons) {
            List<NftCollection> danhSachNftCollection = new ArrayList<NftCollection>();

            periodButton.click();
            // wait for all loading collections
            Thread.sleep(2000);
            for (int i = 1; i <= 50; i++) {
                try {
                    WebElement collection = wait.until(presenceOfElementLocated(
                            By.xpath(
                                    "//*[@id=\"root\"]/div/div/div[2]/div[2]/div/div/div[1]/div/table/tbody/tr[" + i
                                            + "]")));
                    String rank = collection.findElement(By.xpath("./td[1]/p")).getText();
                    String name = collection.findElement(By.xpath("./td[2]/p/a/div/p")).getText();
                    String floorPrice = collection.findElement(By.xpath("./td[5]/p")).getText();
                    // String floorChange = "-";
                    String volume = "-", volumeChange = "-";
                    try {
                        volume = collection.findElement(By.xpath("./td[3]/p/b/span")).getText();
                    } catch (Exception e) {
                    }
                    try {
                        volumeChange = collection.findElement(By.xpath("./td[3]/p/div")).getText().replace("\n",
                                "");
                    } catch (Exception e) {
                    }
                    NftCollection nftCollection = new NftCollection(rank, name, floorPrice,
                            "-", volume,
                            volumeChange, -1, -1);
                    danhSachNftCollection.add(nftCollection);
                    // System.out.println(rank + name + floorPrice + volume + volumeChange);
                } catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
            }

            this.setNftInPeriod(period[index], danhSachNftCollection);
            index++;
        }
    }

}
