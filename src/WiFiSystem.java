
public class WiFiSystem {
	private int numHosts;
	private Host[] hosts;
	private Bus bus;
	
	public WiFiSystem(int numHosts, double speed){
		this.numHosts = numHosts;
		bus = new Bus(speed);
		hosts = new Host[numHosts];
		for(int i = 0; i < numHosts; i++){
			hosts[i] = new Host(bus);
		}
	}
	
	public void tick(){
		bus.tick();
		for(Host h : hosts){
			h.tick();
		}
	}

}
