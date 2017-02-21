
public class ArrivalEvent extends Event {
	private int processingTime = 0;
	
	public ArrivalEvent(){
		//set random times here
	}
	
	public ArrivalEvent(int processingTime){
		setEventTime(0);
		setProcessingTime(processingTime);
	}

	
	public int getProcessingTime(){
		return processingTime;
	}
	
	public void setProcessingTime(int time){
		processingTime = time;
	}

}
