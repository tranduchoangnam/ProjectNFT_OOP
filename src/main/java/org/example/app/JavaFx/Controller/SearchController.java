package org.example.app.JavaFx.Controller;

import com.sun.jna.StringArray;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.app.entity.Tweet;
import org.example.app.service.TwitterService;
import org.example.app.service.BlogService;
import org.example.app.entity.Post;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class SearchController implements Initializable {
//    public SearchController(ListView<Twitter> filteredTweets) {
//        this.fifteredTweets = filteredTweets;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    @FXML
    public Button SearchData;
    @FXML
    public Button SearchBlog;
    @FXML
    private TextField hastagField;

    @FXML
  private TwitterService twitterService;
   @FXML
   private BlogService blogService;
    @FXML
    private VBox tweetlist;

    @FXML
    protected void SearchBlog(ActionEvent event) {
        try {
            tweetlist.getChildren().clear();
            tweetlist.setSpacing(10);
            String hastag = hastagField.getText();
            BlogService postService = new BlogService();
            List<Post> posts = postService.loadPosts();
            if (posts != null) {

                List<Post> matchedPosts = postService.searchPostsByKeyword(posts, hastag);

                for (Post post : matchedPosts) {
                    VBox tweet = new VBox();
                    Label title = new Label(post.getTitle());
                    Label username = new Label(post.getUsername());
                    Label time = new Label(post.getTime());
                    time.setTextFill(Color.BLUE);
                    Hyperlink url = new Hyperlink(post.getUrl());
                    url.setOnAction(e -> {
                        try {
                            Desktop.getDesktop().browse(new URI(post.getUrl()));
                        }   catch (IOException | URISyntaxException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    time.setTextFill(Color.BLUE);
                    tweet.getChildren().addAll(username,time,url, title);
                    tweet.setPadding(new Insets(10,10,10,10));
                    tweet.setSpacing(5);
                    tweet.setStyle("-fx-border-width:2; -fx-border-color:  #A987a8");
                    tweetlist.getChildren().add(tweet);

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @FXML
    protected void SearchData(ActionEvent event) {
        try{
            tweetlist.getChildren().clear();
            tweetlist.setSpacing(10);
            String hastag = hastagField.getText();
            twitterService = new TwitterService();
            ArrayList<Tweet> filteredTweets = twitterService.searchTweetsByHashtag(hastag);
            for(Tweet t : filteredTweets) {
                VBox tweet = new VBox();
                Label username = new Label(t.getUsername());
                Label time = new Label(t.getTime());
                time.setTextFill(Color.BLUE);
                TextArea contentArea=new TextArea(t.getContent());
                contentArea.setWrapText(true);
                contentArea.setEditable(false);
                ScrollPane content = new ScrollPane(contentArea);
                content.setFitToWidth(true);
                content.setMinHeight(100);
                tweet.getChildren().addAll(username,time,content);
                tweet.setPadding(new Insets(10,10,10,10));
                tweet.setSpacing(5);
                tweet.setStyle("-fx-border-width:2; -fx-border-color:  #A987a8");
                tweetlist.getChildren().add(tweet);


    } }
        catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }
    @FXML
    public void Actionback(ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/hello-view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setTitle("Page");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @FXML
    public void Test(ActionEvent event){
        System.out.println("Helllllooooo");
    }
}
