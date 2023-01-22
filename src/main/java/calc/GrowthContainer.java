package calc;
import java.util.ArrayList;
import java.util.Collections;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.*;


public class GrowthContainer implements Iterable<Measurement> {
	
	private static GrowthContainer unique = null;
	private GrowthPhase phase = GrowthPhase.NOTLOG;
	private ArrayList<Measurement> mlist;
	private static LocalDateTime startTime;
	private static LocalDateTime desiredEndTime;
	private static double rate=0;			// the first measure will have that r=0 assigned, unit is usually % per hour
	
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
	
	
	/**adds a new measurement to the container and updates phase&rate if necessary
	*  notifies all listeners after a measurement has been added
	*  if update is true then the Phase and growth rate are also updated.
	**/ 
    public void addMeasure(Measurement measure, boolean update) throws IllegalArgumentException{
    	int n = mlist.size();
    	if(n==0) {
    		startTime=measure.getTime();
    	} else if(mlist.contains(measure)) {
    		throw new IllegalArgumentException("measure already exists or time is sooner than start time");
    	}
    	
    	mlist.add(measure);
    	Collections.sort(mlist);
    	n=mlist.size();
    	if(update && n==2) {
    		updatePhaseAndRate(threshold);	
    	}
    	changes.firePropertyChange("mlist add",null, measure);
    }
    
    /**
     * same thing as above but with no update
     */
    public void addMeasure(Measurement measure) {
    	addMeasure(measure, false);
    }
    
    
    /**USED FOR TESTING
     * adjusts the time of the measurement to be the time of the last one + 2 days
     * @param conf
     * @param update  use true if you also want to update rate and phase (recommended)
     */
    public void addMeasure(double conf, boolean update) throws IllegalArgumentException{
    	if(conf>100) {
    		throw new IllegalArgumentException("measured confluency is higher than 100");
    	}
    	
    	LocalDateTime temp= mlist.get(mlist.size()-1).getTime().plusDays(2);
    	Measurement measure = new Measurement(conf,temp);
    	mlist.add(measure);
    	if(update) {
    		updatePhaseAndRate(threshold);	
    	}
    	changes.firePropertyChange("mlist add",null, measure);
    	
    }
    
    
    

    /**
     * removes a measurement and updates growth rate and phase
     * @return 				true if the measure was found in the list
     */
    public boolean removeMeasure(Measurement measure) {
    	boolean res=mlist.remove(measure);
    	if(res) {
    		this.updatePhaseAndRate(threshold);
    		changes.firePropertyChange("mlist rmv", measure, null);
    	}
    	return res;
    }
    
    //removes the measurement specified by conf and time
    //returns true if the measure was found in the list
    public boolean removeMeasure(double conf, LocalDateTime time) {
    	return removeMeasure(new Measurement(conf, time));
    }
    
    
    public Measurement getMeasure(int index){
    	return mlist.get(index);
    }
    
    public Measurement getMeasure(LocalDateTime time, double conf) throws IllegalArgumentException{
    	int index = mlist.indexOf(new Measurement(time, conf));
    	if(index == -1) {
    		throw new IllegalArgumentException("specified Measure doesnt exist");
    	}
    	return mlist.get(index);
    }
    
    
    /**
     * estimates the time it takes to reach 90% confluency based on the last measurement and current growth rate
     * @return final time as LocalDateTime
     * @throws IllegalStateException
     * @throws NumberFormatException
     * 
     * the double temp in this method is the difference between the last measure and the final time
     * it is converted into seconds, note that if that difference is e.g. 1 day, then we have ~80000 for temp.
     * that should cause any bufferoverflows but just to keep that in mind, this number can get quite big
     **/
    //für später: die zeit zu ders fertig sein soll ist die erste errechnete Final time (also nachdem zwei datenpunkte eingegeben wurden)
  	public LocalDateTime calcFinalTime() throws IllegalStateException, NumberFormatException{
  		double temp=0;
  		Measurement measure=mlist.get(mlist.size()-1);
  		if(phase == GrowthPhase.NOTLOG) {
  			throw new IllegalStateException("cells have not reached log phase");
  		}
  		
  		temp = Math.log((90)/measure.getConf())/rate;		//90 weil die confluency ist double mit z.B. conf = 58,2 
  		temp*=60*60;										//convert from hours to seconds
  														
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
    
    public void startPredictions() {
        changes.firePropertyChange("start Predictions", false, true);
    }


    public void reset() {
        	rate=0;
        	mlist.clear();
        	phase = GrowthPhase.NOTLOG;
        	startTime=null;
    }
    
    
    
    /**updates phase and rate
    * If new growth rate is higher than some threshold -> log phase
    * Compares the most recent measurements
    **/
    public void updatePhaseAndRate(double threshold) {
    	int n=mlist.size();
    	double oldrate=this.getRate();
    	double temp = Measurement.calcGrowthRate(mlist.get(n-2),mlist.get(n-1)); //calculates growth rate of the most recent measurements
    	this.rate=temp;			
    	changes.firePropertyChange("updated Rate", oldrate, temp);
    	System.out.println("updated Rate:" +  String.valueOf(rate));
    	if(rate > threshold) {
    		phase= GrowthPhase.LOG;
    		changes.firePropertyChange("updated Phase to Log", GrowthPhase.NOTLOG, GrowthPhase.LOG);
    	} else {
    		phase=GrowthPhase.NOTLOG;
    	}
    	
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
	
	public double getThreshold() {
		return threshold;
	}
	
    
    
}
