package calc;
import java.time.Duration;
import java.time.LocalTime;

public class Measurement {
	
	private LocalTime time;
	private double conf;
	private double rate = 0;	//GrowthContainer ALSO HAS A RATE! We probably dont need this
	
	
	//use for after you have reached log phase and input the current growth rate
	public Measurement(LocalTime time, double conf, double rate) {
        this.time=time;
        this.conf=conf;
        this.rate=rate;
        
    }
	
	public Measurement(LocalTime time, double conf) {
        this.time=time;
        this.conf=conf;
        
    }
	
	public static double calcGrowthRate(Measurement m1, Measurement m2) {
    	double C1= m1.getConf();
    	double C2= m2.getConf();
    	LocalTime t1= m1.getTime();
    	LocalTime t2= m2.getTime();
    	Duration duration = Duration.between(t1, t2);
    	return ((C2-C1)/(duration.getSeconds()));
		//here we have confluency / seconds
    }
	
	
	
	//sets time as current time
	public Measurement(double conf) { 
        this.time=LocalTime.now();
        this.conf=conf;
    }
	
	public Measurement(double conf, LocalTime time) { 
        this.time=time;
        this.conf=conf;
    }
	

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
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

	
}

