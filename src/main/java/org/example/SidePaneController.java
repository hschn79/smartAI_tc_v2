package org.example;

import calc.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import calc.GrowthContainer;

public class SidePaneController implements Initializable, PropertyChangeListener {

	private XYChart.Series measurements;
	
    @FXML
    private LineChart<?, ?> chartMonitoring;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	measurements = new XYChart.Series();
    	measurements.setName("Measurements");
        
        GrowthContainer con = GrowthContainer.instance();
        con.addPropertyChangeListener(this);
    }
    
    // LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) "1",23
    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
    	GrowthContainer con = GrowthContainer.instance();
    	if(e.getPropertyName().equals("mlist add")) {
    		MeasureOnAdded(con, measurements);
    	}else if (e.getPropertyName().equals("mlist rmv")) {
    			Measurement removed = (Measurement) e.getOldValue();
    			chartMonitoring.getData().remove(new XYChart.Data(removed.getTime(),removed.getConf()));
    	}else if (e.getPropertyName().equals("updated Phase to Log")) {
    		//PredictionOnUpdatedPhase(e,con);
    	}
    		
    		
    		
    	}
    
    public void MeasureOnAdded(GrowthContainer con, XYChart.Series measurements) {
		int size = con.getMListSize() -1;
    	Measurement x = con.getMeasure(size);
    	measurements.getData().add(new XYChart.Data(x.getTime().toString(),x.getConf()));
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
    		
    		for(Prediction p : list) {
    			System.out.println("\n predicted confluency at " + p.getTime().toString() + " is: " + String.valueOf(p.getConf()));
    		}
    		/*
    		XYChart.Series predictions = new XYChart.Series();
        	predictions.setName("Prediction");
        	for(Prediction x : list) {
        		predictions.getData().add(new XYChart.Data(x.getTime().toString(),x.getConf()));
            	chartMonitoring.getData().add(predictions);
            	System.out.println("\n Checkpoint add predictions to Chart\n");		
        		}
        	*/
    	} catch (IllegalArgumentException i) {
    		if(i.getMessage().contains("the specified container has not enough elements")){
    			System.out.println("\n" + i.getMessage() + "\n");
    		} else {
    			throw new IllegalArgumentException("Sth went wrong here: " + i.getMessage());
    		}
    	}
    }	

}


    
