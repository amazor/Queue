
public class Main {
	/** EventList **/
	static GEL gel;
	
	/** The current event being looked at **/
	static Event currEvent;
	
	/** Current Time of Program **/
	static double time;
	
	/** Number of packets in buffer**/
	static int length;
	
	static final int MaxBuffer = 1;
	
	static Buffer buffer;
	
	public static void main(String[] args) {
		initialize();
		for (int i = 0; i< 100000; i++){
			currEvent = gel.pop();
			if(currEvent instanceof ArrivalEvent)// pop the GEL and use instanceof
				processArrival();
			else
				processDeparture();
		}
	
		outputStatistics();
	
	}
	
	private static void initialize(){
		time = 0;
		length = 0;
		gel.insert(new ArrivalEvent(time));
		buffer = new Buffer(MaxBuffer);
		//initialize counters for statistics
		
		
	}
	
	private static void processArrival() { //NEED TO IMPLEMENT STATITICS
		ArrivalEvent currentEvent = (ArrivalEvent)(currEvent);
		
		time = currentEvent.getEventTime();  //update current time
		gel.insert(new ArrivalEvent(time)); //insert next arrival event
		
		try {
			if(buffer.increment(new Packet()) == 1){ //if server is free
				length++;
				new DepartureEvent(time);
			} else { //if server is busy
				length++;
			}
		} catch (BufferOutOfBoundsException e) { 
			if(e.getIsBeyondUpperBound()){ // if buffer is full
				//pktdrop
			} else { // if you decrement too much
				e.printStackTrace();
				System.exit(0);
			}
			
		}
		

	}
	
	private static void processDeparture() {
		DepartureEvent currentEvent = (DepartureEvent)(currEvent);
		time = currentEvent.getEventTime();
		//update stats
		Packet currPacket;
		try {
			buffer.decrement();
		} catch (BufferOutOfBoundsException e) {
			e.printStackTrace();
			System.exit(0);
		}
		length--;
		
		if (buffer.getCount() > 0){	
			try {
				currPacket = buffer.decrement();
				gel.insert(new DepartureEvent(time + currPacket.getServiceTime()));
			} catch (BufferOutOfBoundsException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		
	}
	private static void outputStatistics(){
		
	}

}
