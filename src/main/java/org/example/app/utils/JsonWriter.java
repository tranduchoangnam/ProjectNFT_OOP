package org.example.app.utils;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.example.app.scraper.NftRaribleScraper;
import org.example.app.scraper.NftBinanceScraper;
import org.example.app.scraper.NftNiftyGatewayScraper;
import org.example.app.scraper.NftOpenSeaScraper;
import org.example.app.scraper.NftScraper;

import java.io.IOException;
import java.nio.file.Paths;

public class JsonWriter {
    public void nftCollectionFileWriter() {
        // NftScraper raribleScraper = new NftRaribleScraper();
        // NftScraper binanceScraper = new NftBinanceScraper();
        // NftScraper niftyGatewayScraper = new NftNiftyGatewayScraper();
        NftScraper openSeaScraper = new NftOpenSeaScraper();

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            // writer.writeValue(Paths.get("src/main/resources/json/" +
            // raribleScraper.getName() + ".json").toFile(),
            // raribleScraper.getNftInPeriod());
            // writer.writeValue(Paths.get("src/main/resources/json/" +
            // binanceScraper.getName() + ".json").toFile(),
            // binanceScraper.getNftInPeriod());
            // writer.writeValue(Paths.get("src/main/resources/json/" +
            // niftyGatewayScraper.getName() + ".json").toFile(),
            // niftyGatewayScraper.getNftInPeriod());
            writer.writeValue(Paths.get("src/main/resources/json/" +
                    openSeaScraper.getName() + ".json").toFile(),
                    openSeaScraper.getNftInPeriod());
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}