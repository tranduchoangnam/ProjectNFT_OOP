package org.example.app.JavaFx.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.app.entity.Tweet;
import org.example.app.service.HotHashtagService;
import org.example.app.entity.Tweet;
import org.example.app.service.TwitterService;
import org.example.app.service.BlogService;
import org.example.app.entity.Post;
import org.example.app.utils.TwitterUtils;
import org.example.app.scraper.TwitterScraper;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class HotController extends HelloController {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private Button Day;

    @FXML
    private Button Week;

    @FXML
    private Button Month;
    @FXML
    private Button  dataDay;
    @FXML
    private Label theHottest;
    @FXML
    private TwitterService twitterService;



    @FXML
    private VBox tweetList;


    @FXML
    protected void Hotday(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Hotdaydata.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setTitle("Hotday");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @FXML
    protected void Hotweek(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Hotweekdata.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setTitle("HotPage");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @FXML
    protected void Hotmonth(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Hotmonthdata.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setTitle("HotPage");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @FXML
    protected void dataDay(ActionEvent event) throws IOException {
        tweetList.getChildren().clear();
        tweetList.setSpacing(10);
        List<String>  preiod = HotHashtagService.getMostPopularHashtags();
        String daily=preiod.get(0);
        theHottest.setText(daily);
        twitterService = new TwitterService();
        ArrayList<Tweet> filteredTweets = twitterService.searchTweetsByHashtag(daily);
        for (Tweet t : filteredTweets) {
            VBox tweet = new VBox();
            Label username = new Label(t.getUsername());
            Label time = new Label(t.getTime());
            time.setTextFill(Color.BLUE);
            TextArea contentArea = new TextArea(t.getContent());
            contentArea.setWrapText(true);
            contentArea.setEditable(false);
            ScrollPane content = new ScrollPane(contentArea);
            content.setFitToWidth(true);
            content.setMinHeight(100);
            tweet.getChildren().addAll(username, time, content);
            tweet.setPadding(new Insets(10, 10, 10, 10));
            tweet.setSpacing(5);
            tweet.setStyle("-fx-border-width:2; -fx-border-color:  #A987a8");
            tweetList.getChildren().add(tweet);

        } }
    @FXML
    protected void dataWeek(ActionEvent event) throws IOException {
        tweetList.getChildren().clear();
        tweetList.setSpacing(10);
        List<String>  preiod = HotHashtagService.getMostPopularHashtags();
        String week=preiod.get(1);
        theHottest.setText(week);
        twitterService = new TwitterService();
        ArrayList<Tweet> filteredTweets = twitterService.searchTweetsByHashtag(week);
        for (Tweet t : filteredTweets) {
            VBox tweet = new VBox();
            Label username = new Label(t.getUsername());
            Label time = new Label(t.getTime());
            time.setTextFill(Color.BLUE);
            TextArea contentArea = new TextArea(t.getContent());
            contentArea.setWrapText(true);
            contentArea.setEditable(false);
            ScrollPane content = new ScrollPane(contentArea);
            content.setFitToWidth(true);
            content.setMinHeight(100);
            tweet.getChildren().addAll(username, time, content);
            tweet.setPadding(new Insets(10, 10, 10, 10));
            tweet.setSpacing(5);
            tweet.setStyle("-fx-border-width:2; -fx-border-color:  #A987a8");
            tweetList.getChildren().add(tweet);


        }


    }
    @FXML
    protected void dataMonth(ActionEvent event) throws IOException {
        tweetList.getChildren().clear();
        tweetList.setSpacing(10);
        List<String>  preiod = HotHashtagService.getMostPopularHashtags();
        String month=preiod.get(2);
        theHottest.setText(month);
        twitterService = new TwitterService();
        ArrayList<Tweet> filteredTweets = twitterService.searchTweetsByHashtag(month);
        for (Tweet t : filteredTweets) {
            VBox tweet = new VBox();
            Label username = new Label(t.getUsername());
            Label time = new Label(t.getTime());
            time.setTextFill(Color.BLUE);
            TextArea contentArea = new TextArea(t.getContent());
            contentArea.setWrapText(true);
            contentArea.setEditable(false);
            ScrollPane content = new ScrollPane(contentArea);
            content.setFitToWidth(true);
            content.setMinHeight(100);
            tweet.getChildren().addAll(username, time, content);
            tweet.setPadding(new Insets(10, 10, 10, 10));
            tweet.setSpacing(5);
            tweet.setStyle("-fx-border-width:2; -fx-border-color:  #A987a8");
            tweetList.getChildren().add(tweet);
        }





        }
    @FXML
    protected void yearData(ActionEvent event) throws IOException {
        tweetList.getChildren().clear();
        tweetList.setSpacing(10);
        List<String>  preiod = HotHashtagService.getMostPopularHashtags();
        String year=preiod.get(3);
        theHottest.setText(year);
        twitterService = new TwitterService();
        ArrayList<Tweet> filteredTweets = twitterService.searchTweetsByHashtag(year);
        for (Tweet t : filteredTweets) {
            VBox tweet = new VBox();
            Label username = new Label(t.getUsername());
            Label time = new Label(t.getTime());
            time.setTextFill(Color.BLUE);
            TextArea contentArea = new TextArea(t.getContent());
            contentArea.setWrapText(true);
            contentArea.setEditable(false);
            ScrollPane content = new ScrollPane(contentArea);
            content.setFitToWidth(true);
            content.setMinHeight(100);
            tweet.getChildren().addAll(username, time, content);
            tweet.setPadding(new Insets(10, 10, 10, 10));
            tweet.setSpacing(5);
            tweet.setStyle("-fx-border-width:2; -fx-border-color:  #A987a8");
            tweetList.getChildren().add(tweet);
        }





    }


}
//    @FXML
//    protected void Actionback(ActionEvent event) throws IOException {
//        try {
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/hello-view.fxml"));
//            Parent root1 = (Parent) fxmlLoader.load();
//            stage.setTitle("Page");
//            stage.setScene(new Scene(root1));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ;
//    }

