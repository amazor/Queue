import mm1.*;

public class Host {
	int repetitionCount; //keeps track if there is repetition, and numOfTimeOuts in current Pkt
	boolean isDIFS;
	boolean isBackOff;
	int DIFSCounter;
	int BackCounter;
	
	private Bus sharedBus;
	private GEL gel;
	private Buffer buff;
	private Buffer ackBuffer;
	private int SIFSCounter;
	private int timeoutCounter;
	private boolean isWaitingTimeout;
	
	public Host(Bus bus){
		buff = new Buffer(-1);
		ackBuffer = new Buffer(-1);
		sharedBus = bus;
	}
	

	public boolean isFrameToSend(){
		return buff.getCount() != 0;
	}
	
	public void tick(){
		if(isDIFS){ // is it waiting on DIFS
			DIFSCounter--;
			if (DIFSCounter == 0){
				sendFrame(); // does not pop buff
			}
		} else if(isBackOff){ //is it waiting on BackOFF
			if(sharedBus.isIdle()){
				BackCounter--;
				if(BackCounter == 0){
					sendFrame(); //does not pop buff
				}
			}
		} else if (isWaitingTimeout){
			timeoutCounter--;
			if(timeoutCounter == 0){
				isWaitingTimeout = false;
				repetitionCount++;
				isBackOff = true;
			}
		} else { //it is NOT waiting for anything
			
			if(isFrameToSend()){ //is there a frame to send
				if(repetitionCount > 0){ // is it after the first instance o
					isBackOff = true;
					calcBackOffTime();
				} else { // it is the first instance
					if(sharedBus.isIdle()){
						isDIFS = true;
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
	/*
	 * 	Push the tick to the GEL to let the Host's Buffer know if there's any new arrivals 
	 *		{Inside the GEL if tick time == an arrival time, send an arrival to buffer}
	 *
	 *	if Buffer == Empty //turn into an else
	 		Do Nothing  
			
		if Buffer != Empty & (isRep == false)
			We're doing DIFS
				Since we're using isRep to determine if it's the first time or not, we can just use Backoff = DIFS
			isRep = true;
		
		if Buffer != Empty & (isRep == true){
			if (Bus == Idle) {
				if (Backoff != 0)
					Dec Back off time by 1
					
				else if(Backoff == 0)
					this.sendFrame();
					if (Buffer == empty)
					 	isRep == false;	
				}
			if (Bus != Idle)
				Do nothing
			}
	 */
	
	
	}

	
	private boolean isAckToSend() {
		return ackBuffer.getCount() != 0;
	}


	public Frame calcBackOffTime(){
		
	}


	public void recieveFrame(Frame frame) throws BufferOutOfBoundsException {
		if(frame instanceof Ack){
			timeoutCounter = 0;
			buff.decrement();
			if(isFrameToSend())
				repetitionCount = 1;
			else 
				repetitionCount = 0;
		} else {
			ackBuffer.increment(new Ack(frame.getDestID(), frame.getSrcID));
			
		}
		
	}
}
