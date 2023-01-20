package calc;

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
	 * Der letzte Punkt ist bei finTime, also bis dorthin wird berechnet
	 */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con, LocalDateTime finTime) throws IllegalArgumentException, IllegalStateException{
		int size=con.getMListSize();
		double rate=con.getRate();
		Measurement m1= con.getMeasure(size-1);
		
		if(size<2 || rate==0) {
			System.out.println("rate= " + String.valueOf(con.getRate())+", size= " + String.valueOf(con.getMListSize()));
			throw new IllegalArgumentException("the specified container has not enough elements \n");
		} else if(con.getPhase()!=GrowthPhase.LOG){
			throw new IllegalStateException("the cells have not yet reached log phase \n");
		}
		LocalDateTime m1Time=m1.getTime();
		
		ArrayList<Prediction> list = new ArrayList<>();
		Duration BigInt = Duration.between(m1Time,finTime);
		Duration SmallInt=BigInt.dividedBy(n);		// time intervall between points = SmallInt
		System.out.println("Small Intervall: " + String.valueOf(SmallInt.toHours()) + "\n" + "Big Intervall " + String.valueOf(BigInt.toHours()) +"\n");
		for(int i=1;i<n;i++) {
			
			LocalDateTime tempTime= m1.getTime().plus(SmallInt.multipliedBy(i));
			double tempConf = m1.getConf() * Math.exp(rate * Duration.between(m1Time, tempTime).toHours());
			list.add(new Prediction(tempConf,tempTime));
		}
		return list;
	}
	
	/**
	 * Same thing as the previous one just with finTime=critical Time (also wenn confluency~90%)
	 * Problem: final Time ist vor der Zeit der letzten Messung...
	 */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con) throws IllegalArgumentException, IllegalStateException{
		System.out.println("\n Compare Times \n");
		System.out.println(con.getMeasure(con.getMListSize()-1).getTime());
		LocalDateTime temp = con.calcFinalTime();
		System.out.println(temp);
		System.out.println("\n End Compare Times\n");
		return createPred(n,con,temp);
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

    public void setConf(double conf) {
        this.conf = conf;
    }
}
