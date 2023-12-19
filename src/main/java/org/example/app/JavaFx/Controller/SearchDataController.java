package org.example.app.JavaFx.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import org.example.app.JavaFx.objects.hastag;
public class SearchDataController {
    @FXML
    private Label username;
    @FXML
    private Label datetime;
    @FXML
    private Label hastags;
    @FXML
    private Label content;

    @FXML
    protected void Actionbacksearch(ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SearchPage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setTitle("Page");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }


    //@Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("/resources/json/Rarible.json"));
            List<hastag> events = new Gson().fromJson(reader, new TypeToken<List<hastag>>() {
            }.getType());
            // festivals.forEach(tmp -> System.out.println(tmp.getFestivalName()));
            int i = 0;
            boolean found = false;
            if (!HelloApplication.findString.equals(""))
                for (; i < events.size(); i++) {
                    String name = events.get(i).getUsername().toUpperCase();
                    if (name.contains(HelloApplication.findString)) {
                        found = true;
                        break;
                    }
                }
            if (found) {
                content.setText(events.get(i).getContent());
                String[] tmp = events.get(i).getHastags();
                String s = "";
                for (String l : tmp) {
                    s = s + l + "\n";
                }
                hastags.setText(s);
                tmp = new String[]{events.get(i).getDateime()};
                s = "";
                for (String l : tmp) {
                    s = s + l + "\n";
                }
                username.setText(events.get(i).getUsername());
                datetime.setText(s);
            } else {
                content.setText("Không rõ");
                username.setText("Không rõ");
                datetime.setText("Không rõ");
                hastags.setText("Không rõ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
