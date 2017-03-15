import mm1.BufferOutOfBoundsException;

public class Main {
	public static final double DEFINITION = 0.01; //must be <= 0.01 in ms  Defines the size of a Tick
	public static double conversionFactor = 1/DEFINITION;
	public static final int NUM_HOSTS = 10;
	public static final double LAMBDA_Arrive = 0.9;
	public static final double LAMBDA_SIZE = 0.9;
	public static final double SIM_TIME = 10; //in seconds
	public static final double SPEED = 11*conversionFactor/10; //in mbps
	public static final double TIMEOUT = 5; //in ms      //CHANGED
	
	public static int globalTime;
	
	public static int endTime;
	public static WiFiSystem wifi;
	
	
	public static double Throughput = 0;
	public static double transDelay = 0;
	public static double queueDelay = 0; // In Ticks
	public static double avgDelay = 0;

	
	private static int collisions;
	
	
	
	public static void main(String[] args) {
		init();
		for(; globalTime < endTime; globalTime++){
			try {
				wifi.tick();
			} catch (BufferOutOfBoundsException e) {
				collisions++;
			}

		}
		outputStats();
		
	}

	private static void init() {
		globalTime = 0;
		endTime =  (int) (conversionFactor * SIM_TIME*1000);
		System.out.println("End time =" + endTime);
		wifi = new WiFiSystem(NUM_HOSTS, SPEED);
		}

	private static void outputStats() {
		System.out.println("STATISTICS");
		System.out.println("Collisions: " + collisions);

		System.out.println("Throughput = " + Throughput/SIM_TIME);
		System.out.println("Transmission Delay = " + transDelay);
		//Convert Que Delay to S
		queueDelay  = queueDelay /(1000*conversionFactor);
		System.out.println("Queuing Delay = " + queueDelay);
		avgDelay = (queueDelay + transDelay)/ NUM_HOSTS;
		System.out.println("Average Delay = " + avgDelay);
		
		
		
		
		
	}

}
