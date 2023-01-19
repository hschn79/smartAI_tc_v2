package calc;

import java.util.ArrayList;
import java.time.LocalTime;
import java.time.Duration;

// might be worth doing "extends Measurement"
public class Prediction {
	private double conf;
	private LocalTime time;
	
	public Prediction(double confluency, LocalTime time) {
		this.conf=confluency;
		this.time=time;
	}
	
	public Prediction(double confluency) {
		this.conf=confluency;
		this.time=LocalTime.now();
	}
	
	
	/**
	 * 
	 * @param n				Anzahl der erzeugten Punkte
	 * @param container		von dort wird die letzte Messung und Rate ausgelesen
	 * @return				eine Arraylist von berechneten Punkten, die den restlichen Wachstumsverlauf approximieren
	 * 						Punkte sind zeitlich gleichmäßig verteilt
	 * 						Der letzte Punkt ist bei finTime, also bis dorthin wird berechnet
	 */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con, LocalTime finTime) throws IllegalArgumentException, IllegalStateException{
		int size=con.getMListSize();
		double rate=con.getRate();
		Measurement m1= con.getMeasure(size-1);
		
		if(size<2 || rate==0) {
			throw new IllegalArgumentException("the specified container has not enough elements \n");
		} else if(con.getPhase()!=GrowthPhase.LOG){
			throw new IllegalStateException("the cells have not yet reached log phase \n");
		}
		LocalTime m1Time=m1.getTime();
		
		ArrayList<Prediction> list = new ArrayList<Prediction>();
		Duration BigInt = Duration.between(m1Time,finTime);
		Duration SmallInt=BigInt.dividedBy(n);		// time intervall between points = SmallInt
		for(int i=0;i<n;i++) {
			
			LocalTime tempTime= m1.getTime().plus(SmallInt.multipliedBy(i));
			Double tempConf = m1.getConf() * Math.exp(rate * Duration.between(m1Time, tempTime).getSeconds());
			list.add(new Prediction(tempConf,tempTime));
		}
		
		
		return list;
	}
	
	/**
	 * Same thing as the previous one just with finTime=24hrs
	 */
	public ArrayList<Prediction> createPred(int n, GrowthContainer con) throws IllegalArgumentException, IllegalStateException{
		
		return createPred(n,con,con.getMeasure(con.getMListSize()-1).getTime().plusHours(24));
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
}
