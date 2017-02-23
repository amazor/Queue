
public class Packet {
	private double serviceTime;
	
	
	public Packet (){}

	public Packet (double serviceTime){
		this.serviceTime = serviceTime;
	}
	
	public double getServiceTime(){
		return serviceTime;
	}

}
