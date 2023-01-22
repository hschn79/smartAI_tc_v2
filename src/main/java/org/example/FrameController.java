package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FrameController {
    private boolean menuVisible = true;

    @FXML
    private ToggleButton inputvalues;

    @FXML
    private VBox menu;

    @FXML
    private ToggleButton monitoring;

    @FXML
    private Pane sidepane;

    @FXML
    private ToggleButton temperature;

    private Parent inputValues;
    private Parent monitoringParent;
    private Parent temperatureParent;

    @FXML
    void shrinkMenu(MouseEvent event) {
        if(menuVisible) {
            inputvalues.setText("");
            monitoring.setText("");
            temperature.setText("");
            menu.setPrefSize(21, 355);
            menuVisible = false;
        } else {
            inputvalues.setText("Input Values");
            monitoring.setText("Monitoring");
            temperature.setText("Temperature");
            menu.setPrefSize(172, 355);
            menuVisible = true;
        }
    }
    @FXML
    void toInputValues(MouseEvent event) throws IOException{
        sidepane.getChildren().clear();
        sidepane.getChildren().add(inputValues);
        inputValues.getStyleClass().clear();

    }

    @FXML
    void toMonitoring(MouseEvent event) throws IOException{
        sidepane.getChildren().clear();
        sidepane.getChildren().add(monitoringParent);
    }

    @FXML
    void toTemperature(MouseEvent event) {
        sidepane.getChildren().clear();
        sidepane.getChildren().add(temperatureParent);
    }
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("inputValues.fxml"));
        inputValues = fxmlLoader.load();
        InputValuesController ivc = fxmlLoader.getController();
        sidepane.getChildren().add(inputValues);
        fxmlLoader = new FXMLLoader(App.class.getResource("monitoring.fxml"));
        monitoringParent = fxmlLoader.load();
        MonitoringController mc = fxmlLoader.getController();
        fxmlLoader = new FXMLLoader(App.class.getResource("temperature.fxml"));
        temperatureParent = fxmlLoader.load();
        TemperatureController tc = fxmlLoader.getController();
        ivc.setController(tc, mc);
        mc.setFrameController(this);
    }
    @FXML
    public void reset() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("inputValues.fxml"));
        inputValues = fxmlLoader.load();
        InputValuesController ivc = fxmlLoader.getController();
        sidepane.getChildren().add(inputValues);
        fxmlLoader = new FXMLLoader(App.class.getResource("monitoring.fxml"));
        monitoringParent = fxmlLoader.load();
        sidepane.getChildren().clear();
        sidepane.getChildren().add(inputValues);
        MonitoringController mc = fxmlLoader.getController();
        fxmlLoader = new FXMLLoader(App.class.getResource("temperature.fxml"));
        temperatureParent = fxmlLoader.load();
        TemperatureController tc = fxmlLoader.getController();
        ivc.setController(tc, mc);
        mc.setFrameController(this);
    }
}
