
public class BufferOutOfBoundsException extends Exception {	
	private boolean isBeyondUpperBound;
	public BufferOutOfBoundsException (String message, boolean isBeyondUpperBound){
		super(message);
		this.isBeyondUpperBound = isBeyondUpperBound;
	}
	
	public boolean getIsBeyondUpperBound(){
		return isBeyondUpperBound;
	}
}
