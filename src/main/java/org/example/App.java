package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import calc.GrowthContainer;


/*
 * Zum Testen:
 * cells_10	bei 	20-01-2023 17:47
 * cells_30	bei 	23-01-2023 17:47
 * cells_50	bei		23-01-2023 23:47
 * cells_90	bei		24-01-2023 03:40
*/
/** What I have(nt) done so far:
 * upon saving an image, it will be analyzed by imageJ and the time+confluency = measurement will be added to the GrowthContainer
 * the container itself also has a GrowthPhase (see enum) and the current growth rate, 
 * both of which are updated after you add a new measurement. 
 * Additionally there is a constant threshhold parameter, which is used as a threshold between phases.
 * We definetely need to calculate& update that threshhold, right now its just a magic number
 * Very important: 	1. The GrowthContainer has all the measurements, so you can build graphs, tables,... based on that
 * 					2. You can use beans to update the list of all measurements,... the container already implmements PropertyChangeListener

**/


/**
 * JavaFX App
 */
public class App extends Application {

	// once you start the app this process should also start
	
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("frame"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
		launch();

	}
	

}