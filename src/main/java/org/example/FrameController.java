package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FrameController {
    private boolean menuVisible = true;

    @FXML
    private Button inputvalues;

    @FXML
    private VBox menu;

    @FXML
    private Button monitoring;

    @FXML
    private Pane sidepane;

    @FXML
    private Button temperature;
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
        Parent inputValues = loadFXML("inputValues");
        sidepane.getChildren().clear();
        sidepane.getChildren().add(inputValues);
    }

    @FXML
    void toMonitoring(MouseEvent event) throws IOException{
        Parent monitoring = loadFXML("sidepane");
        sidepane.getChildren().clear();
        sidepane.getChildren().add(monitoring);
    }

    @FXML
    void toTemperature(MouseEvent event) {

    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
