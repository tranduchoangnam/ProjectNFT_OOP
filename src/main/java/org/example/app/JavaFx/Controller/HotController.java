package org.example.app.JavaFx.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

