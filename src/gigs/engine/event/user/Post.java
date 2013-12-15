 package gigs.engine.event.user;
import gigs.db.cloudsql.*;
import java.io.Serializable;
import java.sql.Timestamp;
 
public class Post implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4261569010375599145L;
	public String text;
	public int PID;
	public int UID;
	public int eventID;
	public Timestamp timestmp;
	
	public Post() {
		text = null;
		PID = -1;
		UID = -1;
		eventID = -1;
		timestmp = null;
	}
	
	public Post(int postID, int uID, int eventID, String text, Timestamp timestamp) 
	{
		this.text = text;
		this.PID = postID;
		this.UID = uID;
		this.eventID = eventID;		
		this.timestmp = timestamp;
	}
	
	public boolean post(){
		return true;
	}
	
	public User getPoster() {
		return new UserWrapper().getUser(UID);
	}
	
	public Event getPostEvent(){
		return new EventWrapper().getEvent(eventID);
	}
	
	
	public String toString(){
		return "Post id: " + PID + " user id: " + UID;  
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public Timestamp getTimestmp() {
		return timestmp;
	}

	public void setTimestmp(Timestamp timestmp) {
		this.timestmp = timestmp;
	}
}