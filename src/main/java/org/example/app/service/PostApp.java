package org.example.app.service;

import org.example.app.service.PostService;
import org.example.app.entity.Post;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PostApp {
    public static void main(String[] args) {
        PostService postService = new PostService();
        List<Post> posts = postService.loadPosts();
        if (posts != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập keyword bạn muốn tìm kiếm: ");
            String keyword = scanner.nextLine();

            List<Post> matchedPosts = postService.searchPostsByKeyword(posts, keyword);

            for (Post post : matchedPosts) {
                System.out.println("@" + post.getUsername() + " - " + post.getTime());
                System.out.println("Title: " + post.getTitle());
                System.out.println("URL: " + post.getUrl());
                System.out.println();
            }
        }
    }
}