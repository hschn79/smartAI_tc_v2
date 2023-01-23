package calc;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;import calc.GrowthContainer;

/**
 * similar to Measurement, a prediction has both a predicted confluency and time.
 * For future versions it might be helpful to combine Measurement and Prediction into one abstract class.
 * @author Lukas
 */
public class Prediction {
	private double conf;
	private LocalDateTime time;
	
	public Prediction(double confluency, LocalDateTime time) {
		this.conf=confluency;
		this.time=time;
	}
	
	public Prediction(double confluency) {
		this.conf=confluency;
		this.time=LocalDateTime.now();
	}
	
	/** one prediction based on measurement at time endTime
    *
    * @param m1
    * @param rate
    * @param endTime
    * @return the desired Prediction
    * @throws IllegalArgumentException
    */
   public Prediction createPred(Measurement m1, double rate, LocalDateTime endTime) throws IllegalArgumentException{
           if(rate==0) {
                   throw new IllegalArgumentException("in create Pred(measurement): rate is zero \n");
           }
           LocalDateTime m1Time=m1.getTime();
           double m1Conf=m1.getConf();
           
           Duration duration=Duration.between(m1Time, endTime);
           System.out.println("in Prediction->createPred(Measurement): Checkpoint: m1Conf " + m1Conf);
           System.out.println("in Prediction->createPred(Measurement): Duration: " + duration.toSeconds() + " Rate: "+ rate);
           double tempConf=m1Conf * Math.exp(rate * duration.toSeconds());
           if(tempConf>100) {
        	   tempConf=100;
        	   System.out.println("in Prediction->createPred(Measurement)->tempConf>100, deswegen auf 100 gesetzt");
           }
           Prediction pred = new Prediction(tempConf,endTime);
           return pred;
           
   }
   
   /** generates n predictions based on a measurement, including one at endTime for comparison with a new measurement
   *
   * @param n                                                                number of predictions
   * @param m1                                                        measurement basis for predictions
   * @param rate                                                        reference growth rate
   * @param endTime                                                time where you want to compare the prediction with a new measurement
   * @return                                                                list with pseudo-evenly spaced prediction, the last element is the one to be compared with a new measurement
   * @throws IllegalArgumentException                if the growth rate is zero
   */
  public ArrayList<Prediction> createPred(int n, Measurement m1, double rate, LocalDateTime endTime) throws IllegalArgumentException{
          
          if(rate==0) {
                  throw new IllegalArgumentException("in create Pred(measurement): rate is zero \n");
          }
          LocalDateTime m1Time=m1.getTime();
          LocalDateTime tempTime;
          double tempConf;
          
          ArrayList<Prediction> list = new ArrayList<>();
          Duration BigInt = Duration.between(m1Time, endTime);
          Duration SmallInt=BigInt.dividedBy(n);                // time intervall between points = SmallInt
          System.out.println("IN CREATE_PRED (obere):");
          System.out.println("Small Intervall: In Sekunden " + String.valueOf(SmallInt.toSeconds()) + " In Minuten: " + String.valueOf(SmallInt.toMinutes())+ " in Stunden " + String.valueOf(SmallInt.toHours()));
          System.out.println("Big Intervall: In Sekunden " + String.valueOf(BigInt.toSeconds()) + " In Minuten: " + String.valueOf(BigInt.toMinutes())+ " in Stunden " + String.valueOf(BigInt.toHours()));
          for(int i=1;i<n;i++) {
                  tempTime= m1.getTime().plus(SmallInt.multipliedBy(i));
                  tempConf = m1.getConf() * Math.exp(rate * SmallInt.multipliedBy(i).toSeconds());
                  list.add(new Prediction(tempConf,tempTime));
          }
          //the last entry is done manually to ensure that its time matches endTime
          tempTime=endTime;
          tempConf=m1.getConf()*Math.exp(rate * BigInt.toSeconds());
          list.add(new Prediction(tempConf,tempTime));
          return list;
  }
	
	
	/**
	 *
	 * @paramAnzahl number of predictions
	 *  container used to read current growth rate and phase
	 * @returneine Arraylist of Predictions, approximating the future growth
	 * Points are evenly distributed
	 * The last prediction is at endTime
	 * @throws IllegalArgumentException if the container has less than two measurements or the growth rate is zero
	 * */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con, LocalDateTime endTime) throws IllegalArgumentException{
		int size=con.getMListSize();
		double rate=con.getRate();
		Measurement m1= con.getMeasure(size-1);
		if(size<2 || rate==0) {
            throw new IllegalArgumentException("in create Pred (container): the specified container has not enough elements or rate is zero \n");
		}
		return createPred(n, m1, rate, endTime);
	}
	
	
	
	/**
	 * Same thing as the previous one just with endTime=final Time (confluency~90%)
	 */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con) throws IllegalArgumentException, IllegalStateException{
		/*
		System.out.println("\n Compare Times: \n last measurement: \n");
		System.out.println(con.getMeasure(con.getMListSize()-1).getTime());
		System.out.println("\n calculated Final Time:\n");
		*/
		LocalDateTime temp = con.calcFinalTime();
  		System.out.println("\n In CREATE_PRED (untere): \n final time:");
  		System.out.println(temp);
  		System.out.println("Time of last measurement used for prediction:");
  		System.out.println(con.getMeasure(con.getMListSize()-1).getTime());
		/*
		System.out.println(temp);
		System.out.println("\n End Compare Times\n");
		*/
		return createPred(n,con,temp);
	}
	
	public LocalDateTime getTime() {
        return time;
    }
	public String getTimeString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return time.format(formatter);
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
}
