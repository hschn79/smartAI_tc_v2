package org.example;

import calc.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import calc.GrowthContainer;

public class MonitoringController implements Initializable, PropertyChangeListener {

	private XYChart.Series measurements;
	private FrameController frameController;

    @FXML
    private LineChart<?, ?> chartMonitoring;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	measurements = new XYChart.Series();
    	measurements.setName("Measurements");
        GrowthContainer con = GrowthContainer.instance();
        con.addPropertyChangeListener(this);
    }
	@FXML
	void reset(ActionEvent event)throws IOException {
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
    		MeasureOnAdded(con, measurements);
    	}else if (e.getPropertyName().equals("mlist rmv")) {
			//todo: wie Punkte aus diagram entfernen?
    		Measurement removed = (Measurement) e.getOldValue();
    		chartMonitoring.getData().remove(new XYChart.Data(removed.getTime(),removed.getConf()));
    	}else if (e.getPropertyName().equals("updated Phase to Log")) {
    		PredictionOnUpdatedPhase(e,con);
    	}
	}
    
    public void MeasureOnAdded(GrowthContainer con, XYChart.Series measurements) {
		int size = con.getMListSize() -1;
    	Measurement x = con.getMeasure(size);
    	measurements.getData().add(new XYChart.Data(x.getTime().toString(),x.getConf()));
		System.out.println(measurements.getData());
    	chartMonitoring.getData().clear();
    	chartMonitoring.getData().add(measurements);
    }
    
    
    
    /** gibt nur prediction wenn mindestens 3 elemente enthalten sind
     * @param e
     * @param con
     * macht momentan 100 predictions. 
     * Problem: createPredictions in Prediction hat Problem
     */
    public void PredictionOnUpdatedPhase(PropertyChangeEvent e, GrowthContainer con) throws IllegalArgumentException{
    	try {
    		Prediction pred = new Prediction(0);
    		ArrayList<Prediction> list = pred.createPred(5, con); // I wanted to make createPred static but then it doesnt work for some reason
			XYChart.Series predictions = new XYChart.Series();
        	predictions.setName("Prediction");
        	for(Prediction x : list) {
        		predictions.getData().add(new XYChart.Data(x.getTime().toString(),x.getConf()));
			}
			System.out.println(predictions.getData());
			// x.getConf is null so nothing is depicted
        	chartMonitoring.getData().add(predictions);
    	} catch (IllegalArgumentException i) {
    		if(i.getMessage().contains("the specified container has not enough elements")){
    			System.out.println("\n in PREDICTION_ON_UPDATED_PHASE: " + i.getMessage() + "\n");
    		} else {
    			throw new IllegalArgumentException("in PREDICTION_ON_UPDATED_PHASE: Sth went wrong here: " + i.getMessage());
    		}
    	}
    }	

}


    
