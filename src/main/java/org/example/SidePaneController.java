package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class SidePaneController implements Initializable {

    @FXML
    private LineChart<?, ?> chartMonitoring;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XYChart.Series series = new XYChart.Series();
        series.setName("Data-Points");
        series.getData().add(new XYChart.Data("1", 23));
        chartMonitoring.getData().add(series);
    }
}
