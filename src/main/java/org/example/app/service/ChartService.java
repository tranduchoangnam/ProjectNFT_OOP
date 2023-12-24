package org.example.app.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import javafx.scene.chart.XYChart;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChartService {

    private JsonArray one, oneDay;
    private JsonArray oneHour;
    private JsonArray oneWeek;
    private JsonArray oneMonth;
    
    public JsonArray getOneDay() {
        return oneDay; 
    }
    public JsonArray getOneHour() {
        return oneHour; 
    }
    public JsonArray getOneWeek() {
        return oneWeek; 
    }
    public JsonArray getOneMonth() {
        return oneMonth; 
    }
    
    public void loadFile(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
        if (inputStream != null) {
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                oneDay = jsonObject.getAsJsonArray("1d");
                oneHour = jsonObject.getAsJsonArray("1h");
                oneWeek = jsonObject.getAsJsonArray("1w");
                oneMonth = jsonObject.getAsJsonArray("1m");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleTimePeriodSelected(JsonArray timePeriodArray) {
        one = timePeriodArray;
    }
	
    public double[][] getChartData(String selectedValue) {
        int dataSize = one.size();
        double[][] chartData = new double[dataSize][2];

        for (int i = 0; i < dataSize; i++) {
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

            chartData[i][0] = rank;
            chartData[i][1] = yValue;
        }

        return chartData;
    }
    
    public void addRegressionLine(XYChart.Series<Number, Number> series) {
        // Create a SimpleRegression instance
        SimpleRegression regression = new SimpleRegression();

        // Add data points to the regression
        for (XYChart.Data<Number, Number> data : series.getData()) {
            double xValue = data.getXValue().doubleValue();
            double yValue = data.getYValue().doubleValue();
            regression.addData(xValue, yValue);
        }

        // Calculate the correlation coefficient
        double[] xValues = series.getData().stream().mapToDouble(data -> data.getXValue().doubleValue()).toArray();
        double[] yValues = series.getData().stream().mapToDouble(data -> data.getYValue().doubleValue()).toArray();
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(xValues, yValues);

        // Round the correlation coefficient to two decimal places
        String correlationCoefficientFormatted = String.format("%.2f", correlationCoefficient);

        // Get the slope and intercept of the regression line
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();

        // Create the regression line formula
        String regressionFormula = "y = " + String.format("%.2f", slope) + "x + " + String.format("%.2f", intercept);

        // Create the regression line series
        XYChart.Series<Number, Number> regressionSeries = new XYChart.Series<>();
        regressionSeries.setName("Regression Line: " + regressionFormula + " --> Correlation Coefficient: " + correlationCoefficientFormatted);

        // Get the x-values range of the data
        double minX = series.getData().stream().mapToDouble(data -> data.getXValue().doubleValue()).min().orElse(0.0);
        double maxX = series.getData().stream().mapToDouble(data -> data.getXValue().doubleValue()).max().orElse(0.0);

        // Add the start and end points of the regression line
        regressionSeries.getData().add(new XYChart.Data<>(minX, slope * minX + intercept));
        regressionSeries.getData().add(new XYChart.Data<>(maxX, slope * maxX + intercept));

        // Add the regression line series to the chart
        series.getChart().getData().add(regressionSeries);
    }
	
    public JsonObject getItemDetails(int selectedIndex) {
        int i = selectedIndex - 1;
        JsonObject item = one.get(i).getAsJsonObject();
        return item;
    }
}