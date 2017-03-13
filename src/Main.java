import mm1.BufferOutOfBoundsException;

public class Main {
	public static final double DEFINITION = 0.01; //must be <= 0.01 in ms  Defines the size of a Tick
	public static final int NUM_HOSTS = 10;
	public static final double LAMBDA = 0.1;
	public static final double SIM_TIME = 10; //in seconds
	public static final double SPEED = 11; //in mbps
	
	public static int globalTime;
	public static double conversionFactor = 1/DEFINITION;;
	public static double endTime;
	public static WiFiSystem wifi;
	
	public static void main(String[] args) {
		init();
		for(; globalTime < endTime; globalTime++){
			try {
				wifi.tick();
			} catch (BufferOutOfBoundsException e) {
				e.printStackTrace();
			}

		}
		outputStats();
		
	}

	private static void init() {
		globalTime = 0;
		wifi = new WiFiSystem(NUM_HOSTS, SPEED);
		endTime =  conversionFactor * SIM_TIME/1000;
		}

	private static void outputStats() {
		
	}

}
