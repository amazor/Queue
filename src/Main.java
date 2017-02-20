
public class Main {
	
	public static void main(String[] args) {
		initialize();
		GEL gel;
		for (int clock = 0; clock < 100000; ){
		//1. get the first event from the GEL;
		//2. If the event is an arrival then process-arrival-event;
			if(arrival)// pop the GEL and use instanceof
				processArrival();
			else
				processDeparture();
		}
	
		outputStatistics();
	
	}
	
	private static void processDeparture() {
		
	}

	private static void processArrival() {
		
	}


	private static void outputStatistics(){
		
	}

	public static void initialize(){
		
	}
}
