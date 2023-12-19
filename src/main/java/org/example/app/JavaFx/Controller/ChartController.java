package org.example.app.JavaFx.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChartController implements Initializable {
    @FXML
    private MenuButton menuButton, menuButton2, menuButton3;

    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private Button infoButton;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private Slider zoomSlider;

    @FXML
    private AnchorPane graphPane;
    
    private JsonArray one, oneDay, oneHour, oneWeek, oneMonth;
    private String selectedTimePeriod;
 
    private double mouseX;
    private double mouseY;
    private double graphTranslateX;
    private double graphTranslateY;
    private WebDriver driver;
    private String currentLink;

    
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
    
    @FXML 
    protected void truyCapSan(ActionEvent event) {
    	WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
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
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleZoomSliderChanged();
        });
    	
    	// Chọn các sàn để xem
    	MenuItem binance = new MenuItem("Binance");
    	binance.setOnAction(event -> {
    	    loadFile("json/Binance.json");
    	    setCurrentLink("https://www.binance.com/en/markets/overview");
    	    menuButton3.setText(binance.getText());
    	});

    	MenuItem niftygateway = new MenuItem("NiftyGateway");
    	niftygateway.setOnAction(event -> {
    	    loadFile("json/NiftyGateway.json");
    	    setCurrentLink("https://www.niftygateway.com/");
    	    menuButton3.setText(niftygateway.getText());
    	});

    	MenuItem openSea = new MenuItem("OpenSea");
    	openSea.setOnAction(event -> {
    	    loadFile("json/OpenSea.json");
    	    setCurrentLink("https://www.opensea.io/");
    	    menuButton3.setText(openSea.getText());
    	});

    	MenuItem rarible = new MenuItem("Rarible");
    	rarible.setOnAction(event -> {
    	    loadFile("json/Rarible.json");
    	    setCurrentLink("https://www.rarible.com/");
    	    menuButton3.setText(rarible.getText());
    	});

    	menuButton3.getItems().addAll(binance, niftygateway, openSea, rarible);
    	
    	//Chọn trục y
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
        
    	//Chọn 1day/1week/1hour/1month
    	MenuItem oneDayItem = new MenuItem("1 Ngày");
    	oneDayItem.setOnAction(event -> {
    	    handleTimePeriodSelected(oneDay);
    	    selectedTimePeriod = "1 Ngày";
    	    menuButton2.setText(oneDayItem.getText());
    	});

    	MenuItem oneWeekItem = new MenuItem("1 Tuần");
    	oneWeekItem.setOnAction(event -> {
    	    handleTimePeriodSelected(oneWeek);
    	    selectedTimePeriod = "1 Tuần";
    	    menuButton2.setText(oneWeekItem.getText());
    	});

    	MenuItem oneHourItem = new MenuItem("1 Giờ");
    	oneHourItem.setOnAction(event -> {
    	    handleTimePeriodSelected(oneHour);
    	    selectedTimePeriod = "1 Giờ";
    	    menuButton2.setText(oneHourItem.getText());
    	});

    	MenuItem oneMonthItem = new MenuItem("1 Tháng");
    	oneMonthItem.setOnAction(event -> {
    	    handleTimePeriodSelected(oneMonth);
    	    selectedTimePeriod = "1 Tháng";
    	    menuButton2.setText(oneMonthItem.getText());
    	});

    	menuButton2.getItems().addAll(oneHourItem, oneDayItem, oneWeekItem, oneMonthItem);
    }
    
    private void setCurrentLink(String link) {
            currentLink = link;
    }
    private void loadFile(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
        if (inputStream != null) {
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                oneDay = jsonObject.getAsJsonArray("1d");
                oneHour = jsonObject.getAsJsonArray("1h");
                oneWeek = jsonObject.getAsJsonArray("1w");
                oneMonth = jsonObject.getAsJsonArray("1m");
                handleMenuItemSelected(menuButton.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void handleTimePeriodSelected(JsonArray timePeriodArray) {
        one = timePeriodArray;
        handleMenuItemSelected(menuButton.getText());
    }
    
    @FXML
    public void handleZoomSliderChanged() {
        double zoomLevel = zoomSlider.getValue();
        // Thực hiện phóng to và thu nhỏ đồ thị
        lineChart.setScaleX(zoomLevel);
        lineChart.setScaleY(zoomLevel);
        lineChart.toBack();
    }
    

    public void handleMenuItemSelected(String selectedValue) {
    	String timePeriod = selectedTimePeriod;
    	// Xử lý sự kiện khi chọn một MenuItem
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        
        for (int i = 0; i < one.size(); i++) {
            JsonObject item = one.get(i).getAsJsonObject();

            int rank = Integer.parseInt(item.get("rank").getAsString());
            double yValue = 0.0;

            if (selectedValue.equals("FloorPrice")) {
            	String floorPrice = item.get("floorPrice").getAsString();
            	Pattern pattern = Pattern.compile("(?:\\$)?(\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?)\\s*(?:BNB|ETH)?");
            	Matcher matcher = pattern.matcher(floorPrice);
            	if (matcher.find()) {
            	    String matchedValue = matcher.group(1).replaceAll(",", "");
            	    yValue = Double.parseDouble(matchedValue);
            	}
            } else if (selectedValue.equals("Volume")) {
                yValue = Double.parseDouble(item.get("volume").getAsString().replaceAll("[^\\d.]", ""));
            } else if (selectedValue.equals("VolumeChange")) {
            	String volumeChange = item.get("volumeChange").getAsString();
            	Pattern pattern = Pattern.compile("([+-]?\\d+(?:\\.\\d+)?)%");
            	Matcher matcher = pattern.matcher(volumeChange);
            	if (matcher.find()) {
            	    String matchedValue = matcher.group(1);
            	    yValue = Double.parseDouble(matchedValue);
            	}
            }
            
            series.getData().add(new XYChart.Data<>(rank, yValue));
        }
        series.setName(selectedValue + " vs Rank trong " + timePeriod);
        yAxis.setLabel(selectedValue);
        
     // Create a SimpleRegression instance
        SimpleRegression regression = new SimpleRegression();

        // Add data points to the regression
        for (XYChart.Data<Number, Number> data : series.getData()) {
            double xValue = data.getXValue().doubleValue();
            double yValue = data.getYValue().doubleValue();
            regression.addData(xValue, yValue);
        }
     // Calculate the hệ số tương quan
        double[] xValues = series.getData().stream().mapToDouble(data -> data.getXValue().doubleValue()).toArray();
        double[] yValues = series.getData().stream().mapToDouble(data -> data.getYValue().doubleValue()).toArray();
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(xValues, yValues);
        
     // Round the correlation coefficient to two decimal places
        String heSo = String.format("%.2f", correlationCoefficient);
        // Get the slope and intercept of the regression line
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
     // Create the regression line formula
        String congThuc = "y = " + String.format("%.2f", slope) + "x + " + String.format("%.2f", intercept);

        // Create the regression line series
        XYChart.Series<Number, Number> regressionSeries = new XYChart.Series<>();
        regressionSeries.setName("Regression Line:" + congThuc + "--> Hệ số: " + heSo);

        // Get the x-values range of the data
        double minX = series.getData().stream().mapToDouble(data -> data.getXValue().doubleValue()).min().orElse(0.0);
        double maxX = series.getData().stream().mapToDouble(data -> data.getXValue().doubleValue()).max().orElse(0.0);

     // Add the start and end points of the regression line
        regressionSeries.getData().add(new XYChart.Data<>(minX, slope * minX + intercept));
        regressionSeries.getData().add(new XYChart.Data<>(maxX, slope * maxX + intercept));
        
        lineChart.getData().clear();
        lineChart.getData().addAll(series, regressionSeries);
    
        // Xử lý sự kiện khi di chuột qua điểm dữ liệu
        for (XYChart.Data<Number, Number> data : series.getData()) {
            int xValue = data.getXValue().intValue();
            data.getNode().setOnMouseClicked(event -> {
                int i = xValue - 1;
                JsonObject item = one.get(i).getAsJsonObject();
                String name = item.get("name").getAsString();
                String rank = item.get("rank").getAsString();
                String floorPrice = item.get("floorPrice").getAsString();
                String floorChange = item.get("floorChange").getAsString();
                String volume = item.get("volume").getAsString();
                String volumeChange = item.get("volumeChange").getAsString();
                int items = item.get("items").getAsInt();
                int owners = item.get("owners").getAsInt();
                     
	                
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
                infoButton.setDisable(false); // Kích hoạt button
            });
        }
    }
}