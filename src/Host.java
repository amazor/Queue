import mm1.*;

public class Host {
	int repetitionCount; //keeps track if there is repetition, and numOfTimeOuts in current Pkt
	boolean isDIFS;
	boolean isBackOff;
	private boolean isWaitingTimeout;


	int DIFSCounter;
	int BackCounter;
	private int SIFSCounter;
	private int timeoutCounter;
	
	private Bus sharedBus;
	private GEL gel;
	private Buffer buff;
	private Buffer ackBuffer;
	
	public void tick() throws BufferOutOfBoundsException{
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
	}
	

	public boolean isFrameToSend(){
		return buff.getCount() != 0;
	}
	
	private void sendFrame() {
		isWaitingTimeout = true;
		initTimeout();
		try {
			sharedBus.insertFrame((Frame)(buff.peek()));
		} catch (BufferOutOfBoundsException e) {
			System.out.println("collison occured with Packet:" + (Frame)(buff.peek()));
		}
	}


	private void initTimeout() {
		// TODO Auto-generated method stub
		
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
