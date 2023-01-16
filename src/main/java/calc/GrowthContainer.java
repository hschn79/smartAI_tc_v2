package calc;

import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.*;


// should be called when a) at the start of the programm , b) right after you added an image, ...
public class GrowthContainer implements Iterable<Measurement> {
	
	private static GrowthContainer unique = null;
	private GrowthPhase phase = GrowthPhase.NOTLOG;
	private ArrayList<Measurement> mlist;
	private static LocalTime startTime;
	private static LocalTime desiredEndTime;
	private static double rate=0;			// the first measure will have that r=0 assigned, maybe instead null?
	
	//need to calculate these somehow
	private static double threshhold= 0.0000001; //when is log phase?
	private static double precision= 3;
	
	//you can use beans to get notified everytime the contents in the container change
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	
	private GrowthContainer() {
		mlist = new ArrayList<Measurement>();
	}
	
	public static GrowthContainer instance() {
		if(unique==null) {
			unique = new GrowthContainer();
		}
		return unique;
	}
	
	
	public Iterator<Measurement> iterator() {
		return this.mlist.iterator();
	}

	public ArrayList<Measurement> getMList() {
        return mlist;
    }
	
	public double getRate() {
		return rate;
	}

	public GrowthPhase getPhase() {
    	return phase;
    }
	
	/**adds a new measurement to the container and updates phase&rate if necessary
	*  notifies all listeners after a measurement has been added
	**/ 
    public void addMeasure(Measurement measure) throws IllegalArgumentException{
    	//define the point where you start measuring
    	//henceforth this will mark the point t=0
    	if(mlist.size()==0) {
    		startTime=measure.getTime();	
    	}
    	if(mlist.contains(measure) || measure.getTime().compareTo(startTime) < 0) {
    		throw new IllegalArgumentException("measure already exists or time is sooner than start time");
    	}
    	mlist.add(measure);
    	updatePhaseAndRate(threshhold);
    	changes.firePropertyChange("mlist",null, measure);
    }
    
    //removes a measurement
    //returns true if the measure was found in the list
    public boolean removeMeasure(Measurement measure) {
    	return mlist.remove(measure);
    }
    
    //removes the measurement specified by conf and time
    //returns true if the measure was found in the list
    public boolean removeMeasure(double conf, LocalTime time) {
    	return removeMeasure(new Measurement(conf, time));
    }
    
    /**
     * estimates the time it takes to reach 90% confluency based on what you saw in my pdf under a)
     * @return final time as LocalTime
     * @throws IllegalStateException
     * @throws NumberFormatException
     **/
  	public LocalTime calcFinalTime() throws IllegalStateException, NumberFormatException{
  		double temp=0;
  		if(phase == GrowthPhase.NOTLOG) {
  			throw new IllegalStateException("cells have not reached log phase");
  		}
  		temp = Math.log((0.9)/mlist.get(mlist.size()).getConf())/rate;
  		if (Double.isNaN(temp)){
  			throw new NumberFormatException("error calculating the final time");
  		}
  		
  		return startTime.plusSeconds((long) temp);
  	}

  	
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
    	changes.addPropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l) {
    	changes.removePropertyChangeListener(l);
    }
    
    
    
    
    /**updates phase and rate
    * If new growth rate is higher than some threshold -> log phase
    * Compares the most recent measurements
    **/
    public void updatePhaseAndRate(double threshhold) {
    	int n=mlist.size();
    	if(!(n<2)) {	// if there are enough data points
    		rate = Measurement.calcGrowthRate(mlist.get(n),mlist.get(n-1)); //calculates growth rate of the most recent measurements			
    		if(rate > threshhold) {
    			phase= GrowthPhase.LOG;
    		} else {
    			phase=GrowthPhase.NOTLOG;
    		}
    	}
    	
    }
    
	
}
