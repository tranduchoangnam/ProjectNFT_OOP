package org.example.app.JavaFx.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
import javafx.stage.Screen;
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
import javafx.stage.StageStyle;
import org.example.app.service.TwitterService;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public Button searchButton;

    @FXML
    public void OnActionSearchButton (ActionEvent event){
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SearchPage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setTitle("SearchPage");
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SearchPage.fxml"));
//            Parent root1 = (Parent) fxmlLoader.load();
//            stage.setTitle("SearchPage");
//            stage.setScene(new Scene(root1));
//            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        };
    }
    public void OnActionHotButton (ActionEvent event){
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/HotPage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setTitle("HotPage");
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        };
    }
    public void OnActionChartButton (ActionEvent event){
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Chart.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setTitle("ChartPage");
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        };
    }

}