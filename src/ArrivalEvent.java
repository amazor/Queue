
public class ArrivalEvent extends Event {
	private double arrivalTime = 0;
	
	public ArrivalEvent(double currTime){
		setArrivalTime(currTime + randArrival());
		
	}
	
	public int getArrivalTime(){
		return arrivalTime;
	}
	
	public void setArrivalTime(double time){
		arrivalTime = time;
	}
	
	
	private double randArrival{
		/*double negative-exponenetially-distributed-time(double rate){
     		double u;
     		u = drand48();
     		return ((-1/rate)*log(1-u));*/
		
		double u = 0;
		return u;
	}

}
