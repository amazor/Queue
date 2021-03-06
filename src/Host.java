import mm1.*;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class Host {
	int repetitionCount; //keeps track if there is repetition, and numOfTimeOuts in current Pkt
	boolean isDIFS;
	boolean isBackOff;
	private boolean isWaitingTimeout;
	
	int DIFSCounter = 0;		//DIFS to TICKS
 	int BackCounter = 0;
	//private static int T = 5;    //Vary T in the random backoff algorithm to find out the effect of T on throughput and average network delay by keeping λ constant.
	
	private int SIFSCounter = 0;   //SIFS to TICKS
	private int timeoutCounter = 0;  //Set this to Largest Pckt + DIFS + SIFS   
	
	private Bus sharedBus;
	private Buffer buff;
	private Buffer ackBuffer;
	private int lastArrival = 0;
	
	private int tempQueueDelay = 0;

	int tickCounter = 0;
	int ptr = 0;
	private ArrayList<Integer> arrivalEvents;
	private static Random rand = new Random();
	private static int hostCount = 0;
	private int hostID;
	public Host(Bus bus){
			buff = new Buffer(-1);
			ackBuffer = new Buffer(-1);
			sharedBus = bus;
			repetitionCount = 0;
			hostID = hostCount++;
			
			//Made it an array after all, so we could have a record of when items were added to the Buffer
	//		arrivalEvents = new int[(int) (1.5*Main.SIM_TIME)];    // The size of this array determines the total number of Events that will ever occur
			arrivalEvents = new ArrayList<Integer>();
			arrivalEvents.add((int) (randomArrival()*Main.conversionFactor));
			addArrivalEvents(1);
		}

	private void addArrivalEvents(int sizeOfCurrent) {
		if(Main.endTime <= 100){
			for(int index = sizeOfCurrent; index < sizeOfCurrent + 100; index++){ 
				arrivalEvents.add( (int) (arrivalEvents.get(index-1) + randomArrival()*Main.conversionFactor));
			}	
	
		}
		for(int index = sizeOfCurrent; index < sizeOfCurrent + Main.endTime/100; index++){ 
			arrivalEvents.add( (int) (arrivalEvents.get(index-1) + randomArrival()*Main.conversionFactor));
		}	
		
	}

	public void tick() throws BufferOutOfBoundsException{
		tickCounter ++;
		
		//Adds an arrival event to the Buffer
		if(ptr >= arrivalEvents.size())
			addArrivalEvents(arrivalEvents.size());
		if(arrivalEvents.get(ptr) == tickCounter){
			int j = tickCounter % Main.NUM_HOSTS;
			if (j == hostID)
				j++;
			if (j >= Main.NUM_HOSTS){ 
				j-=3;
			}
			Frame theFrame = new Frame(hostID,(j));
			buff.increment(theFrame);
			
			
			ptr++;
			
			
		}
		
		
		
		if(isDIFS){ // is it waiting on DIFS
			DIFSCounter--;
			if (DIFSCounter == 0){
				isDIFS = false;
				sendFrame();
				 // does not pop buff
			}
		} else if(isBackOff){ //is it waiting on BackOFF
			if(sharedBus.isIdle()){
				BackCounter--;
				if(BackCounter == 0){
					isBackOff = false;
					try{
						sendFrame(); //does not pop buff
					} catch (NullPointerException e) {
						e.printStackTrace();
						System.out.println("BACKOFF TIME: " + BackCounter);
						System.exit(1);
					}

				}
			}
		} else if (isWaitingTimeout){
			timeoutCounter--;
			if(timeoutCounter <= 0){
				isWaitingTimeout = false;
				repetitionCount++;
				if(repetitionCount >= 4){
					timeoutCounter = 0;
					isWaitingTimeout = false;
					isBackOff = false;
					isDIFS = false;
					buff.decrement();
					//System.out.println("Is Wait Buffer: " + buff.getCount());
					lastArrival++;
					if(isFrameToSend())
						repetitionCount = 1;
					else 
						repetitionCount = 0;

				} else {
				initBackoffCounter();
				isBackOff = true;
				}
			}
		} else { //it is NOT waiting for anything
			
			if(isFrameToSend()){ //is there a frame to send
				if(repetitionCount > 0){ // is it after the first instance o
					initBackoffCounter();
					isBackOff = true;
				} else { // it is the first instance
					if(sharedBus.isIdle()){
						isDIFS = true;
						initDIFS();
						
						// Set the DIFS counter here in TICKS
					}
				}
			}
		}
		
		if(isAckToSend()){
			SIFSCounter--;
			if(SIFSCounter <= 0){
				sendAck(); //pop the ackBuffer ALWAYS
			}
		}
	
	}

	private static double randomArrival(){

		//double lambda = .1;        //As Lambda increases the time between arrivals decreases
		double u = rand.nextDouble();
		return ((-1/Main.LAMBDA_Arrive)*Math.log(1-u));
	}
	
	

	public boolean isFrameToSend(){
		return buff.getCount() > 0;
	}
	
	private void sendFrame() throws BufferOutOfBoundsException {
		System.out.println("Host: " + hostID + " Sent Frame" + buff.peek());
		isWaitingTimeout = true;
		initTimeout();
		try {
			sharedBus.insertFrame((Frame)(buff.peek()));
			
			tempQueueDelay = (tickCounter  - arrivalEvents.get(lastArrival));
			//lastArrival++;
			
		} catch (BufferOutOfBoundsException e) {
			System.out.println("collison occured with Packet:" + (Frame)(buff.peek()));
			throw e;
		} catch (NullPointerException e ){
			System.out.println("PROBLEM");
			e.printStackTrace();
			System.exit(1);
		}
	}


	private void initTimeout() {
		timeoutCounter =  (int)(Main.TIMEOUT * Main.conversionFactor);  //120 = 1544*8/(11*10)
		//timeoutCounter = timeourCounter + DIFS + SIFS; 
	}


	private void sendAck() throws BufferOutOfBoundsException {
		Ack ack = (Ack)ackBuffer.decrement();
		System.out.println("Host: " + hostID + " Sent ACK " + ack );
		try {
			sharedBus.insertFrame(ack);
		} catch (BufferOutOfBoundsException e) {
			System.out.println("collison occured with Packet: " + ack);
			throw e;
		}
		
	}


	private boolean isAckToSend() {
		return ackBuffer.getCount() != 0;
	}


	public void initBackoffCounter(){
		this.BackCounter = (int)(rand.nextDouble()*Main.T * repetitionCount + 1);
		//needs to implement repetitioncounter
		//needs to implement backoff time from distrinution
		
		//This right now will just be in Seconds, convert to TICKS later
		
		
	}


	public void recieveFrame(Frame frame) throws BufferOutOfBoundsException {
		if(frame instanceof Ack){
			Main.queueDelay += tempQueueDelay;
			System.out.println("Host: " + hostID + " Recieved ACK " + frame);
			timeoutCounter = 0;
			isWaitingTimeout = false;
			isBackOff = false;
			buff.decrement();
			isDIFS = false;
			//System.out.println("Rcv Frame Buffer: " + buff.getCount());
			lastArrival++;
			if(isFrameToSend())
				repetitionCount = 1;
			else 
				repetitionCount = 0;
		} else { // Receives frame from another host
			System.out.println("Host: " + hostID + " Recieved Frame " + frame);
			initSIFS();
			ackBuffer.increment(new Ack(frame.getDestID(), frame.getSrcID()));
			
		}
		
		Main.Throughput += frame.getSize();
		Main.numPkts++;
		
	}

	private void initSIFS() {
		this.SIFSCounter = (int)(5 * Main.conversionFactor);		//Changed
	}
	
	private void initDIFS() {
		this.DIFSCounter = (int)(10 * Main.conversionFactor);		//Changed
	}
}
