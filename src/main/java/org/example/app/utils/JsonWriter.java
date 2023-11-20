package org.example.app.utils;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.example.app.scraper.NftCollectionScraper;

import java.io.IOException;
import java.nio.file.Paths;

public class JsonWriter {
    public void nftCollectionFileWriter() {
        NftCollectionScraper scraper = new NftCollectionScraper();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(Paths.get("src/main/resources/json/" + scraper.getName() + ".json").toFile(),
                    scraper.getNftInPeriod());
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}