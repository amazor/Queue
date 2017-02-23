
public class ArrivalEvent extends Event {
	//private double arrivalTime = 0;
	
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
		/*double negative-exponenetially-distributed-time(double rate){
     		double u;
     		u = drand48();
     		return ((-1/rate)*log(1-u));*/
		
		double u = 0;
		return u;
	}

}
