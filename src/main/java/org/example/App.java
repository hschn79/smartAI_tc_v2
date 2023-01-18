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

/** What I have(nt) done so far:
 * upon saving an image, it will be analyzed by imageJ and the time+confluency = measurement will be added to the GrowthContainer
 * the container itself also has a GrowthPhase (see enum) and the current growth rate, 
 * both of which are updated after you add a new measurement. 
 * Additionally there is a constant threshhold parameter, which is used as a threshold between phases.
 * We definetely need to calculate& update that threshhold, right now its just a magic number
 * I didnt change anything about the UI itself (I suck at those things)
 * I didnt implement any form of prediction yet
 * I didnt check that the input is okay everytime
 * Very important: 	1. The GrowthContainer has all the measurements, so you can build graphs, tables,... based on that
 * 					2. You can use beans to update the list of all measurements,... the container already implmements PropertyChangeListener
 * 					3. In the InputValuesController there is an important error which I didnt yet solve

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