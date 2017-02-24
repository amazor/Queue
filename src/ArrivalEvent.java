import java.util.Random;

public class ArrivalEvent extends Event {
	//private double arrivalTime = 0;
	private static Random rand = new Random();
	
	public ArrivalEvent(double currTime){
		setArrivalTime(currTime + randArrival());
	}
	
	public double getArrivalTime(){
		return eventTime;
	}
	
	public void setArrivalTime(double time){
		eventTime = time;
	}
	

	private double randArrival(){
		double lambda = .1;
		double u = rand.nextDouble();
		return ((-1/lambda)*Math.log(1-u));
	}

}
