package calc;
import java.time.Duration;
import java.time.LocalDateTime;

public class Measurement implements Comparable<Measurement>{
	
	private LocalDateTime time;
	private double conf;
	private double rate = 0;	//GrowthContainer ALSO HAS A RATE! We probably dont need this
	
	
	//use for after you have reached log phase and input the current growth rate
	public Measurement(LocalDateTime time, double conf, double rate) {
        this.time=time;
        this.conf=conf;
        this.rate=rate;
        
    }
	
	public Measurement(LocalDateTime time, double conf) {
        this.time=time;
        this.conf=conf;
        
    }
	
	/**Calculates the growth rate by calculating the slope between DataPoints m1,m2 in % per hour
	 * 
	 * @param m1 first measurement
	 * @param m2 2nd measurement
	 * @return the growth rate in % per hour
	 */
	public static double calcGrowthRate(Measurement m1, Measurement m2) {
    	double C1= m1.getConf();
    	double C2= m2.getConf();
    	LocalDateTime t1= m1.getTime();
    	LocalDateTime t2= m2.getTime();
    	Duration duration = Duration.between(t1, t2);
    	return ((C2-C1)/(duration.toHours()));
		//here we have confluency / hours
    }
	//sets time as current time
	public Measurement(double conf) { 
        this.time=LocalDateTime.now();
        this.conf=conf;
    }
	public Measurement(double conf, LocalDateTime time) {
        this.time=time;
        this.conf=conf;
    }
	
    public LocalDateTime getTime() {
        return time;
    }
    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public double getConf() {
        return conf;
    }

    public void setType(double conf) {
        this.conf = conf;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     *  note that we are only comparing by Time, not confluency and Time
     */
	@Override
	public int compareTo(Measurement o) {
		return this.time.compareTo(o.getTime());
	}

	
}

