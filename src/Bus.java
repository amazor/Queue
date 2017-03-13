import mm1.Buffer;
import mm1.BufferOutOfBoundsException;

public class Bus extends Buffer{
	int busyCountdown;
	double capacity;
	private Host[] hosts;

	public Bus(Host[] hosts, double capacity) {
		super(1);
		busyCountdown = 0;
		this.capacity = capacity;
		this.hosts = hosts;
	}
	
	public boolean isIdle(){
		return this.getCount() == 0;
	}
	
	public boolean isBusy(){
		return !isIdle();
	}
	
	public void tick(){
		if(isBusy()){
			busyCountdown--;
			if(busyCountdown == 0){
				sendPacket();
			}
		}
	}
	
	public void insertFrame(Frame frame) throws BufferOutOfBoundsException{
		super.increment(frame);
		busyCountdown = (int) (Main.conversionFactor * frame.getSize()/capacity);
	}
	
	public Frame popFrame() throws BufferOutOfBoundsException{
		return (Frame)(super.decrement());
	}
	private void sendPacket() throws BufferOutOfBoundsException{
		Frame frame = popFrame();
		hosts[frame.getDestID()].recieveFrame(frame);
		}
	
	
	

}
