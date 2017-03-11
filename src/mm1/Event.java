package mm1;

public abstract class Event {
	protected double eventTime = 0;
	
	public double getEventTime(){
		return eventTime;
	}
	
	public void setEventTime(double time){
		eventTime = time;
	}

}
