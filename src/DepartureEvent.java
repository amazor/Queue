
public class DepartureEvent extends Event{
	//private double departTime = 0;
	
	public DepartureEvent(double currTime){
		setDepartTime(currTime);
	}

	public double getDepartTime(){
		return eventTime;
	}
	
	public void setDepartTime(double time){
		eventTime = time;
	}	
	
	/*private double randDepart{
		double u = 0;
		return u;
	}*/
	
	
}
