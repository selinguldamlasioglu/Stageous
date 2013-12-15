package gigs.engine.event.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2031953386434799580L;
	public int senderID;
	public ArrayList<Integer> recepientIDs;
	public String message;
	public int MID;
	DateFormat dateFormat; 
	Date date; 
	EventTime time;	
	
	boolean reply;
	MessageState state;
	

	public int parentMID;
	public Timestamp timestmp;

	public Message(){
		senderID = -1;
		parentMID = -1;
		recepientIDs = new ArrayList<Integer>();
		message = "No message body";
		date= new Date();
		timestmp = new Timestamp(date.getTime());
	}
	
	public Message(int MID, int senderID, int parentMID, ArrayList<Integer> recepientIDs, String messageData, Timestamp timeStamp){
		this.senderID = senderID;
		this.MID = MID;
		this.parentMID = parentMID;
		this.recepientIDs = recepientIDs;
		this.message = messageData;
		this.timestmp = timeStamp;
	}

}