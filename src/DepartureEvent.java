
public class DepartureEvent extends Event{
	//private double departTime = 0;
	
	public DepartureEvent(double currTime){
		setDepartTime(currTime + randDepart());
	}
	
	public void setDepartTime(double time){
		eventTime = time;
	}
	

	public int getDepartTime(){
		return eventTime;
	}
	
	private double randDepart{
		/*double negative-exponenetially-distributed-time(double rate){
     		double u;
     		u = drand48();
     		return ((-1/rate)*log(1-u));*/
		
		double u = 0;
		return u;
	}
	
	
}
