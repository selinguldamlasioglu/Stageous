package gigs.engine.event.rec;

import java.util.Comparator;

import gigs.engine.event.user.Event;

public class EventComparator implements Comparator<Event> {
	@Override
	public int compare(Event o1, Event o2) {
        return o1.eventID - o2.eventID;
    }
}
