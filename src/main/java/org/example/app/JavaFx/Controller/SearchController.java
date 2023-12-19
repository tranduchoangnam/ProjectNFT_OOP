package org.example.app.JavaFx.Controller;

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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class SearchController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public Button SearchData;



    @FXML
    protected void SearchData(ActionEvent event) throws IOException {
        try {
            HelloApplication.findString = SearchData.getText().toUpperCase().trim();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SearchPageData.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setTitle("SearchPage");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        };

    }

    @FXML
    protected void Actionback(ActionEvent event) throws IOException {
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
}
