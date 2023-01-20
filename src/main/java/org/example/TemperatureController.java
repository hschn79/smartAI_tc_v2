package org.example;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.time.LocalDateTime;

public class TemperatureController {

    private XYChart.Series measurements;

    @FXML
    private LineChart<?, ?> lineChart;

    public void setTemperature(LocalDateTime time, int temp) {
        measurements = new XYChart.Series();
        XYChart.Series series = new XYChart.Series();
        series.setName("Measurement");
        series.getData().add(new XYChart.Data(time.toString(), temp));
        lineChart.getData().add(series);
    }
    public void initialize() {

    }
}
