package gigs.engine.event.user;

import java.io.Serializable;

public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7774677859203088643L;
	public int UID;
	public int CID;
	public int PID; // ID of the post comment belongs to
	public String comment;
	
	public Comment(){ }
	
	public Comment(int UID, int CID, int PID, String comment){
		this.UID = UID;
		this.CID = CID;
		this.PID = PID;
		this.comment = comment;
		
	}
	
	public String toString(){
		return "" + UID + " " + CID + " " + PID + " " + comment;
	}


}