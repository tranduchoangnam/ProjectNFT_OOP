package org.example.app;

import java.io.IOException;
import org.example.app.utils.JsonWriter;
import org.example.app.utils.JsonReader;

public class App {

    public static void main(String[] args) throws InterruptedException, IOException {
        JsonWriter writer = new JsonWriter();
        writer.nftCollectionFileWriter();
        // NftCollectionScraper scraper = new NftCollectionScraper();
        // System.out.println(scraper.getdanhSachNftCollection());
        // JsonReader reader = new JsonReader();

        // System.out.println(reader.readFileRarible());
        System.exit(0);
    }

}
