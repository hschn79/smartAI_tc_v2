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
	private static double threshold= 0.0000001; //when is log phase?
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
	
	public int getMListSize(){
		return mlist.size();
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
	*  if update is true then the Phase and growth rate are also updated
	**/ 
    public void addMeasure(Measurement measure, boolean update) throws IllegalArgumentException{
    	//define the point where you start measuring
    	//henceforth this will mark the point t=0
    	if(mlist.size()==0) {
    		startTime=measure.getTime();	
    	}
    	if(mlist.contains(measure) || measure.getTime().compareTo(startTime) < 0) {
    		throw new IllegalArgumentException("measure already exists or time is sooner than start time");
    	}
    	mlist.add(measure);
    	updatePhaseAndRate(threshold);
    	changes.firePropertyChange("mlist",null, measure);
    }
    
    /**
     * same thing as above but with no update
     */
    public void addMeasure(Measurement measure) {
    	addMeasure(measure, false);
    }

    /**
     * removes a measurement and updates growth rate and phase
     * @return 				true if the measure was found in the list
     */
    public boolean removeMeasure(Measurement measure) {
    	boolean res=mlist.remove(measure);
    	if(res) {
    		updatePhaseAndRate(threshold);
    	}
    	return res;
    }
    
    //removes the measurement specified by conf and time
    //returns true if the measure was found in the list
    public boolean removeMeasure(double conf, LocalTime time) {
    	return removeMeasure(new Measurement(conf, time));
    }
    
    
    public Measurement getMeasure(int index){
    	return mlist.get(index);
    }
    
    public Measurement getMeasure(LocalTime time, double conf) throws IllegalArgumentException{
    	int index = mlist.indexOf(new Measurement(time, conf));
    	if(index == -1) {
    		throw new IllegalArgumentException("specified Measure doesnt exist");
    	}
    	return mlist.get(index);
    }
    
    
    /**
     * estimates the time it takes to reach 90% confluency based on the last measurement and current growth rate
     * @return final time as LocalTime
     * @throws IllegalStateException
     * @throws NumberFormatException
     **/
    //die zeit zu ders fertig sein soll ist die erste errechnete Final time (also nachdem zwei datenpunkte eingegeben wurden)
  	public LocalTime calcFinalTime() throws IllegalStateException, NumberFormatException{
  		double temp=0;
  		Measurement measure=mlist.get(mlist.size());
  		if(phase == GrowthPhase.NOTLOG) {
  			throw new IllegalStateException("cells have not reached log phase");
  		}
  		
  		temp = Math.log((0.9)/measure.getConf())/rate;
  		if (Double.isNaN(temp)||temp==0){
  			throw new NumberFormatException("error calculating the final time");
  		}
  		
  		return measure.getTime().plusSeconds((long) temp);
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
    public void updatePhaseAndRate(double threshold) {
    	int n=mlist.size();
    	if(!(n<2)) {	// if there are enough data points
    		rate = Measurement.calcGrowthRate(mlist.get(n),mlist.get(n-1)); //calculates growth rate of the most recent measurements			
    		if(rate > threshold) {
    			phase= GrowthPhase.LOG;
    		} else {
    			phase=GrowthPhase.NOTLOG;
    		}
    	}
    	
    }
    
	
}
