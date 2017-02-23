
public class Main {
	
	static final int MaxBuffer = 4;
	
	/** EventList **/
	static GEL gel;
	
	/** main buffer **/
	static Buffer buffer;
	
	/** Current Time of Program **/
	static double time;
	
	/** The current event being looked at **/
	static Event currEvent;
	
	
	/** Number of packets in buffer**/
	static int length;
	
	static int numPktsDropped = 0;
	
	
	
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
		gel = new GEL();
		gel.insert(new ArrivalEvent(time));
		buffer = new Buffer(MaxBuffer);
		//initialize counters for statistics
		
		
	}
	
	private static void processArrival() { //NEED TO IMPLEMENT STATITICS
		ArrivalEvent currentEvent = (ArrivalEvent)(currEvent);
		time = currentEvent.getEventTime();  //update current time
		System.out.println("Packet has arrived at time: " + time);
		gel.insert(new ArrivalEvent(time)); //insert next arrival event
		
		try {
			Packet newPacket = new Packet();
			if(buffer.increment(newPacket) == 1){ //if server is free
				length++;
				gel.insert(new DepartureEvent(time + newPacket.getServiceTime()));
			} else { //if server is busy
				length++;
			}
		} catch (BufferOutOfBoundsException e) { 
			if(e.getIsBeyondUpperBound()){ // if buffer is full
				numPktsDropped++;
				System.out.println("Packet has been dropped *****************");
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
			System.out.println("Packet Departed at time: " + time);
		} catch (BufferOutOfBoundsException e) {
			e.printStackTrace();
			System.exit(0);
		}
		length--;
		
		if (buffer.getCount() > 0){	
			currPacket = buffer.peek();
			gel.insert(new DepartureEvent(time + currPacket.getServiceTime()));

		}

		
	}
	private static void outputStatistics(){
		System.out.println("Number of Packets Dropped: " + numPktsDropped);
	}

}
