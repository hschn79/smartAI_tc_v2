package org.example;

import calc.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.jfree.chart.*;


import calc.GrowthContainer;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class MonitoringController implements Initializable, PropertyChangeListener {

	private XYChart.Series measurements;
	private FrameController frameController;
	private LineChart<String,Number> chartMonitoring;
	private boolean allowMeasurements = true;

	@FXML
	private GridPane gridpane;

	@FXML
	private Text outputField;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	measurements = new XYChart.Series();
    	measurements.setName("Measurements");
        GrowthContainer con = GrowthContainer.instance();
        con.addPropertyChangeListener(this);
		//Create chart
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Datapoint");
		yAxis.setLabel("Confluency in %");
		//creating the chart
		chartMonitoring = new LineChart<String,Number>(xAxis,yAxis);
		chartMonitoring.setTitle("Confluency over Time");
		gridpane.add(chartMonitoring, 1, 1);
    }
	@FXML
	void reset(MouseEvent event)throws IOException {
		frameController.reset();
	}
	public void setFrameController(FrameController fc) {
		frameController = fc;
	}

    // LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) "1",23
    
    /**
     * Problem: PredictionOnUpdatedPhase hat ein Problem
     * Problem: Zeile 49-50: wird nicht aus dem Diagramm gelöscht
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
    	GrowthContainer con = GrowthContainer.instance();
    	if(e.getPropertyName().equals("mlist add")) {
    		MeasureOnAdded(con);
    	}else if (e.getPropertyName().equals("mlist rmv")) {
			//todo: wie Punkte aus diagram entfernen?
    		Measurement removed = (Measurement) e.getOldValue();
			chartMonitoring.getData().clear();
    		chartMonitoring.getData().remove(new XYChart.Data(removed.getTime(),removed.getConf()));
    	}else if (e.getPropertyName().equals("updated Phase to Log")) {
    		PredictionOnUpdatedPhase(e,con);
    	}
	}
    
    public void MeasureOnAdded(GrowthContainer con) {
		if(allowMeasurements) {
			System.out.println("Measurement added");
			int size = con.getMListSize() - 1;
			Measurement x = con.getMeasure(size);
			measurements.getData().add(new XYChart.Data(x.getTimeString(), x.getConf()));
			System.out.println(measurements.getData());
			chartMonitoring.getData().removeAll(measurements);
			chartMonitoring.getData().add(measurements);
		}
    }
    
    
    
    /** gibt nur prediction wenn mindestens 3 elemente enthalten sind
     * @param e
     * @param con
     * macht momentan 100 predictions. 
     * Problem: createPredictions in Prediction hat Problem
     */
    public void PredictionOnUpdatedPhase(PropertyChangeEvent e, GrowthContainer con) throws IllegalArgumentException{
    	try {
			allowMeasurements = false;
    		Prediction pred = new Prediction(0);
    		ArrayList<Prediction> list = pred.createPred(5, con); // I wanted to make createPred static but then it doesnt work for some reason
			XYChart.Series predictions = new XYChart.Series();
        	predictions.setName("Prediction");
        	for(Prediction x : list) {
        		predictions.getData().add(new XYChart.Data(x.getTimeString(), x.getConf()));
			}
			//todo: replace this by the correct function
			XYChart.Series deviation = new XYChart.Series();
			deviation.getData().add(new XYChart.Data("2000-05-06 04:20", 65));

			chartMonitoring.getData().remove(measurements);
        	chartMonitoring.getData().addAll(predictions, deviation);
			GrowthContainer container = GrowthContainer.instance();
			// Set Values of output Field
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			outputField.setText("Your cells will be ready at: " + container.calcFinalTime().format(formatter));
    	} catch (IllegalArgumentException i) {
    		if(i.getMessage().contains("the specified container has not enough elements")){
    			System.out.println("\n in PREDICTION_ON_UPDATED_PHASE: " + i.getMessage() + "\n");
    		} else {
    			throw new IllegalArgumentException("in PREDICTION_ON_UPDATED_PHASE: Sth went wrong here: " + i.getMessage());
    		}
    	}
    }	

}


    
