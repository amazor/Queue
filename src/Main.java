
public class Main {
	public static final double DEFINITION = 0.01; //must be <= 0.01 in ms
	public static final int NUM_HOSTS = 10;
	public static final double LAMBDA = 0.1;
	public static final double SIM_TIME = 100; //in seconds
	
	public static int globalTime;
	public static Host hosts[];
	public static double conversionFactor;
	public static double endTime;
	
	public static void main(String[] args) {
		init();
		for(; globalTime < endTime; globalTime++){
			Host.tick();
			for(Host h: hosts)
				h.tick();
			
		}
		outputStats();
		
	}

	private static void init() {
		globalTime = 0;
		hosts = new Host[NUM_HOSTS];
		for(int i = 0; i < NUM_HOSTS; i++){
			hosts[i] = new Host();
		}
		conversionFactor = 1/DEFINITION;
		endTime =  conversionFactor * SIM_TIME/1000;
		}

	private static void outputStats() {
		
	}

}
