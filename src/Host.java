import mm1.*;

import java.util.Random;

public class Host {
	int repetitionCount; //keeps track if there is repetition, and numOfTimeOuts in current Pkt
	boolean isDIFS;
	boolean isBackOff;
	private boolean isWaitingTimeout;
	
	int DIFSCounter = 0;		//DIFS to TICKS
 	int BackCounter = 0;
	private static int T = 5;    //Vary T in the random backoff algorithm to find out the effect of T on throughput and average network delay by keeping λ constant.
	
	private int SIFSCounter = 0;   //SIFS to TICKS
	private int timeoutCounter = 0;  //Set this to Largest Pckt + DIFS + SIFS   
	
	private Bus sharedBus;
	private Buffer buff;
	private Buffer ackBuffer;
	
	int tickCounter = 0;
	int ptr = 0;
	private Queue<Packet> arrivals;
	private double[] arrivalEvents;
	private Random rand = new Random();
	
	public void tick() throws BufferOutOfBoundsException{
		tickCounter ++;
		int ITSOWNNUMBER = 0;
		
		//Adds an arrival event to the Buffer
		if(arrivalEvents[ptr] == tickCounter){
			Frame theFrame = new Frame(ITSOWNNUMBER,(tickCounter % NUM_HOSTS) );
			buff.increment(theFrame);
			ptr++;
		}
		
		
		
		if(isDIFS){ // is it waiting on DIFS
			DIFSCounter--;
			if (DIFSCounter == 0){
				isDIFS = false;
				sendFrame(); // does not pop buff
			}
		} else if(isBackOff){ //is it waiting on BackOFF
			if(sharedBus.isIdle()){
				BackCounter--;
				if(BackCounter == 0){
					isBackOff = false;
					sendFrame(); //does not pop buff
				}
			}
		} else if (isWaitingTimeout){
			timeoutCounter--;
			if(timeoutCounter == 0){
				isWaitingTimeout = false;
				repetitionCount++;
				initBackoffCounter();
				isBackOff = true;
			}
		} else { //it is NOT waiting for anything
			
			if(isFrameToSend()){ //is there a frame to send
				if(repetitionCount > 0){ // is it after the first instance o
					initBackoffCounter();
					isBackOff = true;
				} else { // it is the first instance
					if(sharedBus.isIdle()){
						isDIFS = true;
						
						// Set the DIFS counter here in TICKS
					}
				}
			}
		}
		
		if(isAckToSend()){
			SIFSCounter--;
			if(SIFSCounter == 0){
				sendAck(); //pop the ackBuffer ALWAYS
			}
		}
	
	}

	public Host(Bus bus){
		buff = new Buffer(-1);
		ackBuffer = new Buffer(-1);
		sharedBus = bus;
		repetitionCount = 0;
		
		//Made it an array after all, so we could have a record of when items were added to the Buffer
		arrivalEvents = new double[1.5*SIM_TIME];    // The size of this array determines the total number of Events that will ever occur
		arrivalEvents[0] = randomArrival()*1000/(DEFINITION);
		for(int index = 1; index < arrivalEvents.length; index++){ 
			arrivalEvents[index] = arrivalEvents[index-1] + randArrival();
		}	
	}
	
	private static double randomomArrival(){
		double lambda = .9;        //As Lambda increases the time between arrivals decreases
		double u = rand.nextDouble();
		return ((-1/lambda)*Math.log(1-u));
	}
	
	

	public boolean isFrameToSend(){
		return buff.getCount() != 0;
	}
	
	private void sendFrame() {
		System.out.println("Frame sent");
		isWaitingTimeout = true;
		initTimeout();
		try {
			sharedBus.insertFrame((Frame)(buff.peek()));
		} catch (BufferOutOfBoundsException e) {
			System.out.println("collison occured with Packet:" + (Frame)(buff.peek()));
		}
	}


	private void initTimeout() {
		timeoutCounter = 120;  //120 = 1544*8/(11*10)
		//timeoutCounter = timeourCounter + DIFS + SIFS; 
	}


	private void sendAck() throws BufferOutOfBoundsException {
		sharedBus.insertFrame((Ack)(ackBuffer.decrement()));
	}


	private boolean isAckToSend() {
		return ackBuffer.getCount() != 0;
	}


	public void initBackoffCounter(){
		
		//needs to implement repetitioncounter
		//needs to implement backoff time from distrinution
		
		//This right now will just be in Seconds, convert to TICKS later
		
		
	}


	public void recieveFrame(Frame frame) throws BufferOutOfBoundsException {
		if(frame instanceof Ack){
			timeoutCounter = 0;
			buff.decrement();
			if(isFrameToSend())
				repetitionCount = 1;
			else 
				repetitionCount = 0;
		} else { // Receives frame from another host
			initSIFS();
			ackBuffer.increment(new Ack(frame.getDestID(), frame.getSrcID()));
			
		}
		
	}


	private void initSIFS() {
		// TODO Auto-generated method stub
		
	}
}
