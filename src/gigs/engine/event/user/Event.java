package gigs.engine.event.user;

import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.ImageWrapper;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Event  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3348270249665368291L;
	public int eventID;
	public String title;
	public EventTime time;
	public ArrayList<String> artists;
	public ArrayList<Tag> tagList;
	public String description;
	public EventImage image;
	
	public String ticketLink;
	public int venueID;
	
	public Event() throws MalformedURLException
	{	
		eventID = -1;
		time = null;
		description = "";
		image= null;
		ticketLink= "http://wiki.answers.com/";
		venueID = -1;
		title = null;
	}

	public Event ( int eventID, String title, Timestamp time, ArrayList<String> artists, ArrayList<Tag> tagList, String description, 
			 EventImage image, String ticketLink, int venueID){
		
		this.eventID = eventID;
		this.title = title;
		this.time = new EventTime(time);
		this.tagList = tagList;
		this.description = description;
		this.image = image;
		this.artists = artists;
		this.ticketLink = ticketLink;
		this.venueID = venueID;
	}
	
	public URL getImageURL()
	{
		return new ImageWrapper().getImageURL(this.image.IID);
	}

	 public int toHash()
	 {
		 return ((Integer) this.eventID).hashCode();
	 }
		 
	 public String toString(){
		  /*return this.eventID + "" + eventID + " " + time.toString() + " " + description + " " + " " 
				  + ticketLink + " " + venueID.toString();*/
		  
		  return this.eventID + "" + eventID + " " + time.toString() + " " 
		  + ticketLink + " " + venueID;
		  
		  //return "" + eventID + time.toString() + tagList.toString() + description + 
		   //  image.toString() + artists.toString() + ticketLink + venueID.toString();
		  //return "" + eventID + time.toString() + description + ticketLink + venueID.toString();
		  //return "" + eventID + time.toString() + tagList.toString() + description + 
		   //  image.toString() + artists.toString() + ticketLink + venueID;
	 }

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EventTime getTime() {
		return time;
	}

	public void setTime(EventTime time) {
		this.time = time;
	}

	public ArrayList<String> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<String> artists) {
		this.artists = artists;
	}

	public ArrayList<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventImage getImage() {
		return image;
	}

	public void setImage(EventImage image) {
		this.image = image;
	}

	public String getTicketLink() {
		return ticketLink;
	}

	public void setTicketLink(String ticketLink) {
		this.ticketLink = ticketLink;
	}

	public int getVenueID() {
		return venueID;
	}

	public void setVenueID(int venueID) {
		this.venueID = venueID;
	}
	 
	 public ArrayList<Integer> getEventThresholdPosts(Timestamp max, int threshold) {
		  EventWrapper iwrap = new EventWrapper();
		  
		  int MAX_SEARCH = 10;
		  int search = 1;
		  Timestamp min = new Timestamp(max.getTime()-60*60*1000);
		  
		  ArrayList<Integer> result = null;
		  result = iwrap.getEventPostIdsByTime(this.eventID, min, max);
		  int minutes = 6*60;
		  long min_t, m;
		    
		  while(result.size() < threshold && search<MAX_SEARCH){
		   min_t = min.getTime();
		      m = minutes * 60 * 1000;
		      max = new Timestamp(min_t - 1); 
		      min = new Timestamp(min_t - m);
		     
		      result.addAll(iwrap.getEventPostIdsByTime(this.eventID, min, max));
		      search++;
		  } 
		  return result;
	}
	 
	public ArrayList<User> getAttendingUsers() {
		  EventWrapper iwrap = new EventWrapper();
		  ArrayList<User> users = new ArrayList<User>();
		  ArrayList<Integer> userIds = iwrap.getAttendingUserIds(this.eventID);
		  for(int i = 0; i < userIds.size(); i ++){
		   users.add(new User(userIds.get(i)));
		  }
		  return users;
	}

	public ArrayList<User> getInterestedUsers() {
		  EventWrapper iwrap = new EventWrapper();
		  ArrayList<User> users = new ArrayList<User>();
		  ArrayList<Integer> userIds = iwrap.getInterestedUserIds(this.eventID);
		  for(int i = 0; i < userIds.size(); i ++){
		   users.add(new User(userIds.get(i)));
		  }
		  return users;
	}
	
	public URL getEventImage(int eid)
	{
	  return new ImageWrapper().getEventImage(eid);
	}
	
}