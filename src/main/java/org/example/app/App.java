package org.example.app;

import java.io.IOException;
import org.example.app.scraper.DemoScraper;

public class App {

    public static void main(String[] args) throws IOException {
        DemoScraper demoScraper = new DemoScraper();
        System.out.println(demoScraper.getDanhSachSanPham());
    }

}
