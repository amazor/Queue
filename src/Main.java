
public class Main {
	/** EventList **/
	static GEL gel;
	
	/** The current event being looked at **/
	static Event currEvent;
	
	/** Current Time of Program **/
	static int time;
	
	/** Number of packets in buffer**/
	static int length;
	
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
		//initialize counters for statistics
		
		
	}
	
	
	/* 
	 1.
  Find the time of the next arrival, which is the current time 
  (which is maintained by the time variable) plus a randomly 
  generated time drawn from a negative exponentially distributed
   random variable with rate λ.
	 2.
  Create a new packet and determine its service time which is
   a randomly generated time drawn from a negative exponentially
   distributed random variable with rate μ.
    
	 3.
  	Create the new arrival event.   
	 4.
  Insert the event into the event list. Note that there can be
   other events in the event list. The newly created event must
   be placed in the right place so that the events are ordered
   in time.  
	 */
	private static void processArrival() {
		ArrivalEvent currentEvent = (ArrivalEvent)(currEvent);
		
		time = currentEvent.getEventTime();  //update current time
		gel.insert(new ArrivalEvent(time)); //insert next arrival event
		
		if (length == 0){ //if buffer is empty / server is free
			length++;
			//insert new Departure event at the processing time for this arrival event
			gel.insert(new DepartureEvent(currentEvent.getProcessingTime()));
		} else if(Buffer.MAX_BUFFER < 0){ //infinite buffer
			length++;
		} else if (length-1 < Buffer.MAX_BUFFER && length > 0  ){ //if server is busy and buffer available
			length++;
		} else { //server is busy and no buffer available
			//pkt drop
		}
	}
	
	private static void processDeparture() {
		DepartureEvent currentEvent = (DepartureEvent)(currEvent);
		time = currentEvent.getEventTime();
		length--;
		
		if (length > 0){
			/*
			 1. Dequeue the first packet from the buffer;
			 2. Create a new departure event for a time which is
 				the current time plus the time to transmit the 
				packet.  
			 3. Insert the event at the right place in the GEL. 
			 */
			
			gel.insert(new DepartureEvent(time));
		}

		
	}



	private static void outputStatistics(){
		
	}

}
