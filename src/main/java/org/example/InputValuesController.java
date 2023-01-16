package org.example;

import calc.GrowthContainer;
import com.helena.imageJTest.*;
import calc.*;

import ij.io.Opener;
import ij.process.ImageProcessor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ij.*;
import java.io.IOException;
import java.time.LocalTime;

public class InputValuesController{
    private static Scene scene;
    private static Stage stage;

    @FXML
    private TableColumn<Row, String> action;

    @FXML
    private TableColumn<Row, String> filename;

    @FXML
    private TableColumn<Row, String> picture;

    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row, String> time;

    @FXML
    private TableColumn<Row, String> type;

    public static void discardNewPhotoDialog() {
        stage.close();
    }

    @FXML
    void openDialogPane(MouseEvent event) throws IOException {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        scene = new Scene(loadFXML("newPhoto"));
        stage.setTitle("Upload Picture");
        stage.setScene(scene);
        stage.show();
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void initializeTable(String filename, String filepath, String time, String type) {
        Measurement measure = (new ImageJClass()).analyze(filepath, LocalTime.now());
        GrowthContainer container = GrowthContainer.instance();
        container.addMeasure(measure);
        stage.close();
    }
}
