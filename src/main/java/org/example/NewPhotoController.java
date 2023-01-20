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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NewPhotoController {

    private File file;

    private ComboBox comboBox;

    @FXML
    private ImageView fotodepictor;

    @FXML
    private TextField timeInput;

    @FXML
    private Label fileNamePanel;

    @FXML
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
        String time = timeInput.getText();
        if(time == null || fileNamePanel.getText().isEmpty() || file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File or Time Input Missing!");
            alert.show();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            try {
                LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                ivc.initializeTable(fileNamePanel.getText(), file, dateTime, "test");
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Date must be in format dd-MM-yyyy HH:mm");
                alert.show();
                return;
            }
        }

    }
    @FXML
    void discard(MouseEvent event) {
        InputValuesController.discardNewPhotoDialog();
    }

   public void setInputValuesController(InputValuesController controller) {
        ivc = controller;
    }
}
