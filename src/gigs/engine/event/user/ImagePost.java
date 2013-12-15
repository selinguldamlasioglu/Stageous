package gigs.engine.event.user;


import gigs.db.cloudsql.ImageWrapper;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

public class ImagePost extends Post implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1379951907492467783L;
	public EventImage image;
	
	
	public ImagePost() {
		super();
	}

	public ImagePost(int postID, int uID, int eventID,String text,Timestamp timestamp, EventImage image) {
		super(postID,uID,eventID,text,timestamp);
		this.image = image;
	}	
	
	public URL getImageURL()
	{
//		try {
//			return new URL("http://stageous.com/" + this.image.IID);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		return null;
		return new ImageWrapper().getImageURL(this.image.IID);
	}
	 
}