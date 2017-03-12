
public class WiFiSystem {
	private int numHosts;
	private Host[] hosts;
	private Bus bus;
	
	public WiFiSystem(int numHosts, double speed){
		this.numHosts = numHosts;
		hosts = new Host[numHosts];
		bus = new Bus(speed);
	}
	
	public void tick(){
		bus.tick();
		for(Host h : hosts){
			h.tick();
		}
	}

}
