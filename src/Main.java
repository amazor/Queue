
public class Main {
	
	static final int MaxBuffer = -1;
	
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
	
	
	/** Statistics Variables **/
	static int numPktsDropped = 0;
	static Statistics stats;
	
	
	public static void main(String[] args) {
		initialize();
		for (int i = 0; i< 10000; i++){
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
		stats = new Statistics();
		
		
	}
	
	private static void processArrival() { //NEED TO IMPLEMENT STATITICS
		ArrivalEvent currentEvent = (ArrivalEvent)(currEvent);
		double prevTime = time;
		time = currentEvent.getEventTime();  //update current time
		System.out.println("Packet has arrived at time: " + time);
		gel.insert(new ArrivalEvent(time)); //insert next arrival event
		
		try {
			Packet newPacket = new Packet();
			if(buffer.increment(newPacket) == 1){ //if server is free
				stats.add(buffer.getCount()-1, time - prevTime);
				length++;
				gel.insert(new DepartureEvent(time + newPacket.getServiceTime()));
			} else { //if server is busy
				stats.add(buffer.getCount()-1, time - prevTime);
				length++;
			}
		} catch (BufferOutOfBoundsException e) { 
			if(e.getIsBeyondUpperBound()){ // if buffer is full
				numPktsDropped++;
				stats.add(buffer.getCount(), time - prevTime);
				System.out.println("Packet has been dropped *****************");
			} else { // if you decrement too much
				e.printStackTrace();
				System.exit(0);
			}
			
		}
		

	}
	
	private static void processDeparture() {
		DepartureEvent currentEvent = (DepartureEvent)(currEvent);
		double prevTime = time;
		time = currentEvent.getEventTime();
		//update stats
		Packet currPacket;
		try {
			stats.add(buffer.getCount(), time - prevTime);
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
		System.out.println();
		System.out.println("STATISTICS");
		System.out.println("Number of Packets Dropped: " + numPktsDropped);
		System.out.println("Mean queue length:" +stats.calcMeanQueueLength(time));
		System.out.println("Utility Percentage:" +stats.calcUtil(time));
	}


}
