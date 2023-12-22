package org.example.app.JavaFx.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.Bindings;
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
import java.util.*;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private ListView<String> postListView; //thêm khai báo ListView

    //@Override

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("/resources/json/Rarible.json"));
            List<hastag> events = new Gson().fromJson(reader, new TypeToken<List<hastag>>() {}.getType());

            // Tạo danh sách các chuỗi để hiển thị trong ListView
            ObservableList<String> items = FXCollections.observableArrayList();

            // Lặp qua danh sách events và thêm thông tin từ mỗi sự kiện vào danh sách chuỗi
            for (hastag event : events) {
                String info = String.format("Username: %s\nDatetime: %s\nContent: %s\nHashtags: %s\n",
                        event.getUsername(), event.getDateime(), event.getContent(), Arrays.toString(event.getHastags()));
                items.add(info);
            }

            // Đặt dữ liệu vào ListView
            postListView.setItems(items);
            postListView.setFixedCellSize(100); // Điều chỉnh độ cao tùy thuộc vào nội dung hiển thị
            postListView.prefHeightProperty().bind(Bindings.size(items).multiply(postListView.getFixedCellSize()).add(2)); // Đặt chiều cao của ListView
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
