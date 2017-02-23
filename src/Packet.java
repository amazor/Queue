import java.util.Random;


public class Packet {
	private double serviceTime;
	
	/** Create Random Distribution**/
	private static Random packetRand = new Random();

	
	public Packet (){
	serviceTime = randService();
	}

	public Packet (double serviceTime){
		this.serviceTime = serviceTime;
	}
	
	public double getServiceTime(){
		return serviceTime;
	}
	
	
	private double randService(){
		double mu = 1;
		double u = rand.nextDouble();
		return ((-1/mu)*Math.log(1-u));
	}

}
