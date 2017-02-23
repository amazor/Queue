import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	private int MAX_BUFFER; //set to -1 for infinite buffer size
	private int counter;
	Queue<Packet> packets;
	
	public Buffer(int maxBuffer){
		MAX_BUFFER = maxBuffer;
		counter = 0;
		packets = new LinkedList<Packet>();
	}
	
	public int increment(Packet packet) throws BufferOutOfBoundsException{
		counter++;
		packets.add(packet);
		if(MAX_BUFFER > 0 && counter > MAX_BUFFER)
			throw new BufferOutOfBoundsException(counter + " > " + MAX_BUFFER, true);
		return counter;
	}
	
	public Packet decrement() throws BufferOutOfBoundsException{
		counter--;
		if(counter < 0)
			throw new BufferOutOfBoundsException(counter + " < 0", false);
		

		return packets.poll();
	}
	
	public int getCount(){
		return counter;
	}
	
	
	
	

}
