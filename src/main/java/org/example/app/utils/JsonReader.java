package org.example.app.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.example.app.entity.NftCollection;
import org.example.app.entity.Twitter;

public class JsonReader {
    public HashMap<String, List<NftCollection>> readFileRarible() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, List<NftCollection>> obj = mapper.readValue(new File("src/main/resources/json/Rarible.json"),
                new TypeReference<HashMap<String, List<NftCollection>>>() {
                });
        return obj;
    }
    public static List<Twitter> readFileTwitterData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Twitter> list = mapper.readValue(new File("src/main/resources/json/TwitterData.json"),
                new TypeReference<List<Twitter>>() {
                });
        return list;
    }
}
