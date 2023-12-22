package org.example.app.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.example.app.entity.NftCollection;
import org.example.app.entity.Tweet;
import org.example.app.entity.Post;

public class JsonReader {
    public HashMap<String, List<NftCollection>> readFileRarible() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, List<NftCollection>> obj = mapper.readValue(new File("src/main/resources/json/Rarible.json"),
                new TypeReference<HashMap<String, List<NftCollection>>>() {
                });
        return obj;
    }
    public static List<Tweet> readFileTwitterData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Tweet> list = mapper.readValue(new File("src/main/resources/json/TwitterData.json"),
                new TypeReference<List<Tweet>>() {
                });
        return list;
    }
    public static List<Post> readFileNftPostsData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Post> list = mapper.readValue(new File("src/main/resources/json/NftPostsData.json"),
                new TypeReference<List<Post>>() {
                });
        return list;
    }
}
