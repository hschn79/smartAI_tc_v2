package org.example;

import calc.GrowthContainer;
import com.helena.imageJTest.*;
import calc.*;

import ij.io.Opener;
import ij.process.ImageProcessor;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ij.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ObservableList<Row> listRows = FXCollections.observableArrayList();
    private ObservableList<Row> selectedRows = FXCollections.observableArrayList();
    private Map<Row, Measurement> rowMeasurementMap = new HashMap<>();
    public static void discardNewPhotoDialog() {
        stage.close();
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
     * @param type
     */
    public void initializeTable(String filename, File file, String time, String type) {
        System.out.println("Inside initialize Table");
        CheckBox cb = new CheckBox();
        ImageView iv = new ImageView();
        Image image = new Image(file.toURI().toString(), 50, 50, false, false);
        iv.setImage(image);
        Row row = new Row(filename, time, type, iv, cb);
        cb.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            selectedRow(row);
        });
        listRows.add(row);
        table.getItems().clear();
        table.getItems().addAll(listRows);
        ImageJClass ij = new ImageJClass();
        Measurement measure = ij.analyze(file.getPath(), LocalDateTime.now());
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
        type.setCellValueFactory(new PropertyValueFactory<Row, String>("type"));
        table.getItems().addAll(listRows);
        //npc.setInputValuesController(this);
    }
    private void selectedRow(Row row) {
        if(selectedRows.contains(row)) {
            selectedRows.remove(row);
        }else {
            selectedRows.add(row);
        }
        System.out.println(selectedRows);
    }
}
