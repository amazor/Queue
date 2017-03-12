import mm1.Packet;

public class Frame extends Packet {
	private int srcHost;
	private int destHost;
	private int size;
	private int FrameIDNum;
	
	public Frame(int srcHostID, int DestHostID){
		super();
		srcHost = srcHostID;
		destHost = DestHostID;
		//randomsize	
	}
	
	public int getSize(){
		return size;
	}

}
