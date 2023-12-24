package org.example.app.service;

import org.example.app.entity.Post;
import org.example.app.utils.JsonReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BlogService {
    public List<Post> loadPosts() {
        try {
            return JsonReader.readFilePost("Post");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> searchPostsByKeyword(List<Post> posts, String keyword) {
        return posts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}