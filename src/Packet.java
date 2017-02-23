import java.util.Random;


public class Packet {
	private double serviceTime;
	
	/** Create Random Distribution**/
	private static Random packetRand = new Random();
	double mu = 1;

	
	public Packet (){
	double u = packetRan.nextDouble();
	serviceTime = ((-1/mu)*Math.log(1-u)); 
		
	}

	public Packet (double serviceTime){
		this.serviceTime = serviceTime;
	}
	
	public double getServiceTime(){
		return serviceTime;
	}

}
