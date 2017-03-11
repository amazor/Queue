package mm1;
import java.util.LinkedList;

public class GEL {
	
private LinkedList<Event> list;

public GEL(){
	 list = new LinkedList<Event>();
}

public void insert(Event e){
	int index = 0;
	for(Event currEvent: list){
		if (currEvent.getEventTime() > e.getEventTime()){
			list.add(index, e);
			return;
		}
		index++;
	}
	list.add(e);
}

public Event pop(){
	return list.pop();
}


}
