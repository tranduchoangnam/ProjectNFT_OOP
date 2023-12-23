package org.example.app.JavaFx.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.example.app.service.ChartService;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChartController extends HelloController {
    @FXML
    private MenuButton menuButton;

    @FXML
    private MenuButton menuButton2;

    @FXML
    private MenuButton menuButton3;

    @FXML
    private Slider zoomSlider;

    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private Button infoButton;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private AnchorPane graphPane;
    private ChartService chartService;
    private double mouseX;
    private double mouseY;
    private double graphTranslateX;
    private double graphTranslateY;
    private String currentLink;

    public ChartController() {
        chartService = new ChartService();
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
    }

    @FXML
    protected void truyCapSan(ActionEvent event) {
        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        try {
            driver.get(currentLink);
            WebElement button = driver.findElement(By.id("TruyCapSan"));
            button.click();
        } finally {
        }
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            graphTranslateX = graphPane.getTranslateX();
            graphTranslateY = graphPane.getTranslateY();
        }
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;
            graphPane.setTranslateX(graphTranslateX + deltaX);
            graphPane.setTranslateY(graphTranslateY + deltaY);
        }
    }

    @FXML
    public void handleZoomSliderChanged() {
        double zoomLevel = zoomSlider.getValue();
        // Thực hiện phóng to và thu nhỏ đồ thị
        lineChart.setScaleX(zoomLevel);
        lineChart.setScaleY(zoomLevel);
        lineChart.toBack();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleZoomSliderChanged();
        });

        MenuItem binance = new MenuItem("Binance");
        binance.setOnAction(event -> {
            chartService.loadFile("json/Binance.json");
            getCurrentLink("https://www.binance.com/en/markets/overview");
            menuButton3.setText(binance.getText());
        });

        MenuItem niftygateway = new MenuItem("NiftyGateway");
        niftygateway.setOnAction(event -> {
            chartService.loadFile("json/NiftyGateway.json");
            getCurrentLink("https://www.niftygateway.com/");
            menuButton3.setText(niftygateway.getText());
        });

        MenuItem openSea = new MenuItem("OpenSea");
        openSea.setOnAction(event -> {
            chartService.loadFile("json/OpenSea.json");
            getCurrentLink("https://www.opensea.io/");
            menuButton3.setText(openSea.getText());
        });

        MenuItem rarible = new MenuItem("Rarible");
        rarible.setOnAction(event -> {
            chartService.loadFile("json/Rarible.json");
            getCurrentLink("https://www.rarible.com/");
            menuButton3.setText(rarible.getText());
        });

        menuButton3.getItems().addAll(binance, niftygateway, openSea, rarible);

        MenuItem floorPriceItem = new MenuItem("Floor Price");
        floorPriceItem.setOnAction(event -> {
            handleMenuItemSelected("FloorPrice");
            menuButton.setText(floorPriceItem.getText());
        });

        MenuItem volumeItem = new MenuItem("Volume");
        volumeItem.setOnAction(event -> {
            handleMenuItemSelected("Volume");
            menuButton.setText(volumeItem.getText());
        });

        MenuItem volumeChangeItem = new MenuItem("Volume Change");
        volumeChangeItem.setOnAction(event -> {
            handleMenuItemSelected("VolumeChange");
            menuButton.setText(volumeChangeItem.getText());
        });

        menuButton.getItems().addAll(floorPriceItem, volumeItem, volumeChangeItem);

        MenuItem oneDayItem = new MenuItem("1 Ngày");
        oneDayItem.setOnAction(event -> {
            chartService.handleTimePeriodSelected(chartService.getOneDay());
            menuButton2.setText(oneDayItem.getText());
        });

        MenuItem oneWeekItem = new MenuItem("1 Tuần");
        oneWeekItem.setOnAction(event -> {
            chartService.handleTimePeriodSelected(chartService.getOneWeek());

            menuButton2.setText(oneWeekItem.getText());
        });

        MenuItem oneHourItem = new MenuItem("1 Giờ");
        oneHourItem.setOnAction(event -> {
            chartService.handleTimePeriodSelected(chartService.getOneHour());

            menuButton2.setText(oneHourItem.getText());
        });

        MenuItem oneMonthItem = new MenuItem("1 Tháng");
        oneMonthItem.setOnAction(event -> {
            chartService.handleTimePeriodSelected(chartService.getOneMonth());

            menuButton2.setText(oneMonthItem.getText());
        });

        menuButton2.getItems().addAll(oneHourItem, oneDayItem, oneWeekItem, oneMonthItem);
    }

    public void getCurrentLink(String link) {
        currentLink = link;
        ;
    }

    public void handleMenuItemSelected(String selectedValue) {
        // Xử lý sự kiện khi chọn một MenuItem
        double[][] chartData = chartService.getChartData(selectedValue);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < chartData.length; i++) {
            series.getData().add(new XYChart.Data<>(chartData[i][0], chartData[i][1]));
        }
        series.setName(selectedValue + " vs Rank");

        // Clear existing data and add new series

        lineChart.getData().clear();
        lineChart.getData().add(series);

        yAxis.setLabel(selectedValue);
        // Add regression line to the series
        chartService.addRegressionLine(series);
        // Xác định sự kiện nhấp chuột vào điểm trên đồ thị
        for (XYChart.Data<Number, Number> data : series.getData()) {
            data.getNode().setOnMouseClicked(event -> {
                handleDataPointClicked(data);

            });
        }

    }

    public void handleDataPointClicked(XYChart.Data<Number, Number> data) {
        int selectedIndex = data.getXValue().intValue(); // Lấy chỉ mục từ xValue

        // Lấy thông tin chi tiết từ service
        JsonObject item = chartService.getItemDetails(selectedIndex);

        // Trích xuất thông tin từ item
        String name = item.get("name").getAsString();
        String rank = item.get("rank").getAsString();
        String floorPrice = item.get("floorPrice").getAsString();
        String floorChange = item.get("floorChange").getAsString();
        String volume = item.get("volume").getAsString();
        String volumeChange = item.get("volumeChange").getAsString();
        int items = item.get("items").getAsInt();
        int owners = item.get("owners").getAsInt();

        // Hiển thị thông tin lên button
        String info = "THÔNG TIN CHI TIẾT" + "\n"
                + "Name: " + name + "\n"
                + "Rank: " + rank + "\n"
                + "Floor Price: " + floorPrice + "\n"
                + "Floor Change: " + floorChange + "\n"
                + "Volume: " + volume + "\n"
                + "VolumeChange: " + volumeChange + "\n"
                + "Items: " + items + "\n"
                + "Owners: " + owners;

        infoButton.setText(info);
        infoButton.setDisable(false);
    }

}