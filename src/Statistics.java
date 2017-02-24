import java.util.ArrayList;

public class Statistics {
	private ArrayList<Double> list;
	public Statistics(){
		list = new ArrayList<Double>();
		list.add(new Double(0));
	}
	
	public void add(int numInBuff, double timeToAdd){
		if(list.size() - 1 < numInBuff){
			list.add(new Double(timeToAdd));
		} else {
			list.set(numInBuff, list.get(numInBuff) + timeToAdd);
		}
	}
	
	public double calcMeanQueueLength(double totalTime){
		int length = list.size();
		double mean = 0;
		for(int i = 1; i < length; i ++){
			mean += list.get(i) * i;
		}
		return mean/totalTime;
	}
	
	public double calcUtil(double totalTime){
		return (totalTime-list.get(0))/totalTime;
	}
	
	public double checkTime(double totalTime){
		double sum = 0;
		int length = list.size();
		for(int i = 0; i < length; i ++){
			sum += list.get(i);
		}
		return sum/totalTime;
	}
	
	public void print(){
		int length = list.size();

		for(int i = 0; i < length; i ++){
			System.out.println("[" + i +  "]: " + list.get(i));
		}
		

	}

}
