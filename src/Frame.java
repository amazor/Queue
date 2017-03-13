import mm1.Packet;
import java.util.Random;

public class Frame extends Packet {
	private int srcHost;
	private int destHost;
	private int FrameIDNum;
	private int size;
	private static Random randF = new Random();
	
	public Frame(int srcHostID, int DestHostID){
		super();
		srcHost = srcHostID;
		destHost = DestHostID;
		//randomsize	
		size = randomSize();	
	}
	public void setSize(int newSize){
		size = newSize;
	}
	public int getSize(){
		return size;
	}

	public int getDestID() {
		return destHost;
	}
	public int getSrcID(){
		return srcHost;
	}
	
	public String toString(){
		return "Src:" + srcHost + " Dest: " + destHost;
	}
	
	private int randomSize(){
		double lambda = .1;    
		double u = randF.nextDouble();
		return (int)((-1/lambda)*Math.log(1-u)) + 64;  // The 64 is just there as a base so it's larger than an ACK
	}
	

}
