package calc;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	 * @param m1 1st measurement
	 * @param m2 2nd measurement, i.e. has to be later
	 * @return the growth rate in % per hour
	 */
	public static double calcGrowthRate(Measurement m1, Measurement m2) throws IllegalArgumentException{
		if (m1.getTime().isAfter(m2.getTime())) {
			throw new IllegalArgumentException("error calculating growth rate: m1 has to be before m2");
		}
    	double C1= m1.getConf();
    	double C2= m2.getConf();
    	LocalDateTime t1= m1.getTime();
    	LocalDateTime t2= m2.getTime();
    	Duration duration = Duration.between(t1, t2);
    	return (Math.log(C2-C1)/(duration.toSeconds()));
		//here we have confluency / Seconds
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
    public String getTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");;
        return time.format(formatter);
    }
    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public double getConf() {
        return conf;
    }

    public void setConf(double conf) {
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
     *  returns -1 of this.time is before o.getTime, 0 if they are the same (up to at least minutes), 1 if this.time is after o.getTime()
     */
	@Override
	public int compareTo(Measurement o) {
		return this.time.compareTo(o.getTime());
	}

	
}

