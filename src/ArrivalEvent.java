
public class ArrivalEvent extends Event {
	private int processingTime = 0;
	
	public ArrivalEvent(int eventTime){
		//set random times here
	}
	
	public ArrivalEvent(int eventTime, int processingTime){
		//set random times here
		setProcessingTime(processingTime);
	}

	
	public int getProcessingTime(){
		return processingTime;
	}
	
	public void setProcessingTime(int time){
		processingTime = time;
	}

}
