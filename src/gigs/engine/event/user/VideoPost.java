package gigs.engine.event.user;

import java.io.Serializable;
import java.sql.Timestamp;

public class VideoPost extends Post implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7310497751734364874L;
	public String embedLink;
	
	public VideoPost(){
		super();
		embedLink = null;				
	}
	
	public VideoPost(int postID,int uID,int eventID,String text,Timestamp timestamp, String embedLink)
	{
		super(postID,uID,eventID,text,timestamp);
		this.embedLink = embedLink;				
	}
}