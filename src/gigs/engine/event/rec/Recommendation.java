package gigs.engine.event.rec;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;

import gigs.db.cloudsql.*;
import gigs.engine.auth.lastfm.LastfmGetter;
import gigs.engine.event.user.*;

public class Recommendation {
	
	private static LastfmGetter lg;
	private static EventWrapper ewrap;
	
	public static ArrayList<Event> recRelatedEvents(Event e, int threshold)
	{
		ArrayList<Event> rec = new ArrayList<Event>();
		XMLClipper xml;
		
		if(ewrap == null)
			ewrap = new EventWrapper();
		
		rec.addAll( ewrap.getArtistEvents(e.artists.get(0)) );
		
		if(rec.size()<threshold)
			rec.addAll(ewrap.getVenueEvents(e.venueID));
		else return rec;
		
		for(int i = 1; i<e.artists.size(); i++ )
		{
			String artist = e.artists.get(i);
			if(rec.size()<threshold)
				rec.addAll( ewrap.getArtistEvents(artist) );
			else return rec;
		}
		
		try {
			//Get Headliner's CURRENT Events
			String artist = e.artists.get(0);
			
			lg = new LastfmGetter();
			xml = new XMLClipper(lg.getArtistEventsXML(artist));
			if(rec.size()<threshold)
				rec.addAll(xml.XML2Events());
			else return rec;
			
			//Get Venue's CURRENT Events
			int VID = e.venueID;
			lg = new LastfmGetter();
			xml = new XMLClipper(lg.getVenueEventsXML(VID));
			if(rec.size()<threshold)
				rec.addAll(xml.XML2Events());
			else return rec;
			
			//Get Other Artists' CURRENT Events
			for(int i = 1; i<e.artists.size(); i++ )
			{
				artist = e.artists.get(i);
				lg = new LastfmGetter();
				xml = new XMLClipper(lg.getArtistEventsXML(artist));
				if(rec.size()<threshold)
					rec.addAll(xml.XML2Events());
				else return rec;
			}	
		} catch (Exception ex) { ex.printStackTrace(); }
		return rec;
	}
	
	public static ArrayList<Event> recFollowEvents(int userid, int threshold) 
	{
		ArrayList<Event> rec = new ArrayList<Event>();
		XMLClipper xml;
		try{
			if(lg==null)
				lg = new LastfmGetter();
			if(ewrap == null)
				ewrap = new EventWrapper();
			
			ArrayList<Integer> eventids = ewrap.getFollowingEvent(userid);
			
			for(int i =0; i<eventids.size(); i++)
			{
				Event e = ewrap.getEvent(eventids.get(i));
				if(e == null || e.title == "" || e.title == null)
				{
					xml = new XMLClipper(lg.getEventInfoXML(eventids.get(i), 0));
					e = xml.XML2Event();
				}
				if(rec.size()<threshold)
					rec.add(e);
				else return rec;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return rec;		
	}
	
	public static ArrayList<Event> recUserNewEvents(User u, int threshold)
	{
		ArrayList<Event> rec = new ArrayList<Event>();
		XMLClipper xml;
		EventWrapper ew = new EventWrapper();
		
		try {
			if(lg == null)
				lg = new LastfmGetter();
			
			int cloudsize=0;
			if(u.tagCloud != null && u!=null)
			{
				cloudsize = u.tagCloud.size();
				Collections.sort(u.tagCloud, new TagComparator());
			}
			else return rec;
			
			
			// TODO - Implement tag interest ratess
			
			for(int i =0; i<cloudsize; i++)
			{
				// Implement DB Search
				rec.addAll(ew.getTagEvent( u.tagCloud.get(i%cloudsize) ) );
				if(rec.size()<threshold)
				{
					xml = new XMLClipper(lg.getGeoTagEventsXML(u.location, u.tagCloud.get(i%cloudsize)));
					rec.addAll(xml.XML2Events());
					System.out.println("Recsize: " +  rec.size());
				}
			}
			
			int currtag = 0;
			int distance = 100;
			while(rec.size()<threshold)
			{		
				xml = new XMLClipper(lg.getGeoTagEventsXML(u.location, u.tagCloud.get(currtag%cloudsize), distance));
				rec.addAll(xml.XML2Events());
				System.out.println("Recsize: " +  rec.size());
				distance+=100;
				currtag++;
			}
			
			ArrayList<Integer> degrees = new ArrayList<Integer>();
			degrees.add(rec.get(0).eventID);
			
			for(int i=1; i<rec.size(); i++)
			{
				if(! degrees.contains(rec.get(i).eventID) )
					degrees.add(rec.get(i).eventID);
				else
				{
					rec.remove(i);
					i--;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new ArrayList<Event>(rec);		
	}

	public static ArrayList<Tag> weightedTagList() {  
		  
		LastfmGetter getter;
		ArrayList<Tag> tags = null;
		try {
			getter = new LastfmGetter();
			XMLClipper clip = new XMLClipper(getter.getWeightedTags());  
			tags = clip.XML2WeighedTagList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		return tags;
	}
	
	public static ArrayList<Tag> weightedTagList(int max) {  
		  
		LastfmGetter getter;
		ArrayList<Tag> tags = null;
		try {
			getter = new LastfmGetter();
			XMLClipper clip = new XMLClipper(getter.getWeightedTags());  
			tags = clip.XML2WeighedTagList(max);
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		return tags;
	}
	
}
