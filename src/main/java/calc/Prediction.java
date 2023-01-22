package calc;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;import calc.GrowthContainer;

// might be worth doing "extends Measurement"
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
	
	
	/**
	 *
	 * @paramAnzahl der erzeugten Punkte
	 *  container von dort wird die letzte Messung und Rate ausgelesen
	 * @returneine Arraylist von berechneten Punkten, die den restlichen Wachstumsverlauf approximieren
	 * Punkte sind zeitlich gleichmäßig verteilt
	 *Der letzte Punkt ist bei finTime, also bis dorthin wird berechnet
	 */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con, LocalDateTime endTime) throws IllegalArgumentException, IllegalStateException{
		int size=con.getMListSize();
		double rate=con.getRate();
		Measurement m1= con.getMeasure(size-1);
		rate/= 60*60;							//intern brauchen wir hier die Rate als %/sec
		
		if(size<2 || rate==0) {
			throw new IllegalArgumentException("in create Pred: the specified container has not enough elements or rate is zero \n");
		} else if(con.getPhase()!=GrowthPhase.LOG){
			throw new IllegalStateException("in create Pred: the cells have not yet reached log phase \n");
		}
		LocalDateTime m1Time=m1.getTime();
		
		ArrayList<Prediction> list = new ArrayList<>();
		Duration BigInt = Duration.between(m1Time, endTime);
		Duration SmallInt=BigInt.dividedBy(n);		// time intervall between points = SmallInt
		System.out.println("IN CREATE_PRED (obere):");
		System.out.println("Small Intervall: In Sekunden " + String.valueOf(SmallInt.toSeconds()) + " In Minuten: " + String.valueOf(SmallInt.toMinutes())+ " in Stunden " + String.valueOf(SmallInt.toHours())); 
		System.out.println("Big Intervall: In Sekunden " + String.valueOf(BigInt.toSeconds()) + " In Minuten: " + String.valueOf(BigInt.toMinutes())+ " in Stunden " + String.valueOf(BigInt.toHours()));
		for(int i=1;i<n;i++) {
			LocalDateTime tempTime= m1.getTime().plus(SmallInt.multipliedBy(i));
			double tempConf = m1.getConf() * Math.exp(rate * SmallInt.multipliedBy(i).toSeconds());
			list.add(new Prediction(tempConf,tempTime));
		}
		return list;
	}
	
	/**
	 * Same thing as the previous one just with endTime=final Time (also wenn confluency~90%)
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
