package org.example;

import ImageAnalysis.ImageJClass;
import calc.GrowthContainer;
import ImageAnalysis.*;
import calc.*;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class InputValuesController{
    private static Scene scene;
    private static Stage stage;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;
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

    private ObservableList<Row> listRows = FXCollections.observableArrayList();
    private ObservableList<Row> selectedRows = FXCollections.observableArrayList();
    private Map<Row, Measurement> rowMeasurementMap = new HashMap<>();
    // Use this controller to call methods in temperatureController
    private TemperatureController temperatureController;
    // Use this controller to call methods in monitoringController
    private MonitoringController monitoringController;
    public static void discardNewPhotoDialog() {
        stage.close();
    }

    @FXML
    void startMeasurement(MouseEvent event) {
        // Todo: starte prediction hier!
        // Property Change support changes
        if(listRows.size() < 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Add at least 3 pictures");
            alert.show();
        } else {
            addButton.setDisable(true);
            deleteButton.setDisable(true);
            GrowthContainer con = GrowthContainer.instance();
            con.startPredictions();
        }

    }
    @FXML
    void openDialogPane(MouseEvent event) throws IOException {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource( "newPhoto.fxml"));
        scene = new Scene(fxmlLoader.load());
        NewPhotoController npc = fxmlLoader.getController();
        npc.setInputValuesController(this);
        stage.setTitle("Upload Picture");
        stage.setScene(this.scene);
        stage.show();
    }
    @FXML
    void deletePictures(MouseEvent event) {
        GrowthContainer container = GrowthContainer.instance();
        Measurement measurement;
        for(Row row : selectedRows) {
            listRows.remove(row);
            measurement = rowMeasurementMap.get(row);
            container.removeMeasure(measurement);
        }
        if (listRows.size() < 4) {
            addButton.setDisable(false);
        }
        table.getItems().clear();
        table.getItems().addAll(listRows);
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Right now when you input a file, we adjust the time for testing
     * @param filename
     * @param file
     * @param time
     */
    public void initializeTable(String filename, File file, LocalDateTime time) {
        System.out.println("Inside initialize Table");
        CheckBox cb = new CheckBox();
        ImageView iv = new ImageView();
        Image image = new Image(file.toURI().toString(), 50, 50, false, false);
        iv.setImage(image);
        Row row = new Row(filename, time, iv, cb);
        cb.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            selectedRow(row);
        });
        listRows.add(row);
        if(listRows.size() > 2) {
            addButton.setDisable(true);
        }
        table.getItems().clear();
        table.getItems().addAll(listRows);
        ImageJClass ij = new ImageJClass();
        Measurement measure = ij.analyze(file.getPath(), time);
        rowMeasurementMap.put(row, measure);
        GrowthContainer container = GrowthContainer.instance();
        container.addMeasure(measure,true);
        stage.close();
    }
    public void initialize(){
        action.setCellValueFactory(new PropertyValueFactory<Row, String>("action"));
        filename.setCellValueFactory(new PropertyValueFactory<Row, String>("filename"));
        picture.setCellValueFactory(new PropertyValueFactory<Row, String>("picture"));
        time.setCellValueFactory(new PropertyValueFactory<Row, String>("time"));
        table.getItems().addAll(listRows);
        table.setPlaceholder(new Label("No content added"));
    }
    private void selectedRow(Row row) {
        if(selectedRows.contains(row)) {
            selectedRows.remove(row);
        }else {
            selectedRows.add(row);
        }
        System.out.println(selectedRows);
    }
    public void setController(TemperatureController tc, MonitoringController mc) {
        temperatureController = tc;
        monitoringController = mc;
    }
}
