
public class Ack extends Frame {
	
	public Ack(int srcHostID, int destHostID){
		super(srcHostID, destHostID);
		setSize(64);
	}
	
	public String toString(){
		return "(ACK FRAME -- Src:" + getSrcID() + " Dest: " + getDestID()+")";
	}

}
