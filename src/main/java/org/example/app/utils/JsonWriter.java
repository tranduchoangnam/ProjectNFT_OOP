package org.example.app.utils;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.example.app.scraper.PostDefiantScraper;
import org.example.app.scraper.NftBinanceScraper;
import org.example.app.scraper.NftNiftyGatewayScraper;
import org.example.app.scraper.NftOpenSeaScraper;
import org.example.app.scraper.NftRaribleScraper;
import org.example.app.scraper.NftScraper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    public void nftCollectionFileWriter() {
        List<NftScraper> nftScraperList = new ArrayList<NftScraper>();
        NftScraper raribleScraper = new NftRaribleScraper();
        NftScraper binanceScraper = new NftBinanceScraper();
        NftScraper niftyGatewayScraper = new NftNiftyGatewayScraper();
        NftScraper openSeaScraper = new NftOpenSeaScraper();
        nftScraperList.add(raribleScraper);
        nftScraperList.add(binanceScraper);
        nftScraperList.add(niftyGatewayScraper);
        nftScraperList.add(openSeaScraper);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            for (NftScraper nftScraper : nftScraperList) {
                writer.writeValue(Paths.get("src/main/resources/json/" +
                        nftScraper.getName() + ".json").toFile(),
                        nftScraper.getNftInPeriod());
            }
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void postFileWriter() throws InterruptedException {
        PostDefiantScraper postScraper = new PostDefiantScraper();

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(Paths.get("src/main/resources/json/Post.json").toFile(),
                    postScraper.getPostArray());
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}