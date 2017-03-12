import mm1.*;

public class Host {
	boolean isRepitition = false;
	boolean isDIFS;
	boolean isBackOff;
	int DIFSCounter;
	int BackCounter;
	
	private GEL;
	private Buffer buff;
	
	public Host(){
		buff = new Buffer(-1);
	}
	
	
	public void tick(){
	/*
	 * 	Push the tick to the GEL to let the Host's Buffer know if there's any new arrivals 
	 *		{Inside the GEL if tick time == an arrival time, send an arrival to buffer}
	 *
	 *	if Buffer == Empty //turn into an else
	 		Do Nothing  
			
		if Buffer != Empty & (isRep == false)
			We're doing DIFS
				Since we're using isRep to determine if it's the first time or not, we can just use Backoff = DIFS
			isRep = true;
		
		if Buffer != Empty & (isRep == true){
			if (Bus == Idle) {
				if (Backoff != 0)
					Dec Back off time by 1
					
				else if(Backoff == 0)
					this.sendFrame();
					if (Buffer == empty)
					 	isRep == false;	
				}
			if (Bus != Idle)
				Do nothing
			}
	 */
	
	
	}

	
	public Frame broadcast(){
		return new Frame();
		
	}
}
