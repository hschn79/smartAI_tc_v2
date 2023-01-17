package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class NewPhotoController {

    private File file;

    private ComboBox comboBox;

    @FXML
    private HBox hbox_phototype;
    @FXML
    private ImageView fotodepictor;

    @FXML
    private TextField timeInput;

    @FXML
    private Label fileNamePanel;

    private InputValuesController ivc;


    @FXML
    void selectFile(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(imageFilter);
        file = fc.showOpenDialog(stage);
        if (file != null) {
            fotodepictor.setImage(new Image(file.toURI().toString()));
            fileNamePanel.setText(file.getName());
            fileNamePanel.setTextFill(Color.color(0,0,0));
        }
    }
    @FXML
    void save(MouseEvent event) {
        if(timeInput.getText().isEmpty()|| fileNamePanel.getText().isEmpty() || comboBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File or Time Input Missing!");
            alert.show();
        } else {
            this.ivc.initializeTable(fileNamePanel.getText(), file, timeInput.getText(), comboBox.getValue().toString());
        }

    }
    public void initialize(){
        ObservableList<String> options = FXCollections.observableArrayList(
                "Reference Picture",
                "Measurement Picture"
        );
        comboBox = new ComboBox(options);
        hbox_phototype.getChildren().add(comboBox);
        hbox_phototype.setMargin(comboBox, new Insets(0, 0, 0, 5));

    }
    @FXML
    void discard(MouseEvent event) {
        InputValuesController.discardNewPhotoDialog();
    }

    public void setInputValuesController(InputValuesController ivc) {
        this.ivc = ivc;
    }
}
