package gigs.engine.event.user;

import java.io.Serializable;
import java.sql.Timestamp;

public class FollowPost extends Post implements Serializable{
	
	private static final long serialVersionUID = -6864554176251808780L;
	public int followedID;
	
	public FollowPost(){
		super.UID = -1;
		followedID = -1;
		super.timestmp = null;
		PID = -1;
	}
	
//	public FollowPost(int followerID, int followedID, Timestamp t, int PID){
//		super.UID = followerID;
//		this.followedID = followedID;
//		super.timestmp = t;
//		this.PID = PID;
//	}
	
	public int getFollowerID() {
		return super.UID;
	}

	public void setFollowerID(int followerID) {
		super.UID = followerID;
	}

	public int getFollowedID() {
		return followedID;
	}

	public void setFollowedID(int followedID) {
		this.followedID = followedID;
	}

	public Timestamp getTime() {
		return super.timestmp;
	}

	public void setTime(Timestamp time) {
		super.timestmp = time;
	}	
	
	public String toString(){
		return super.UID + " is following " + followedID + " starting from " + super.timestmp.toString();
	}
	
}
