package gigs.engine.event.user;


import java.io.Serializable;
import java.sql.Timestamp;

public class AttendPost extends Post implements Serializable{
	
	private static final long serialVersionUID = -3022096617728343884L;
	
	public Attend attendance;
	
	public AttendPost(){
		super.eventID = -1;
		super.UID = -1;
		attendance = Attend.NOTGOING;
		super.timestmp = null;
		super.PID = -2;
	}
	
	public AttendPost(int eventID, int senderID, Attend attendance, Timestamp time){
		super.eventID = eventID;
		super.UID = senderID;
		this.attendance = attendance;
		super.timestmp = time;
		super.PID = -2;
	}

	public Attend getAttendance() {
		return attendance;
	}

	public void setAttendance(Attend attendance) {
		this.attendance = attendance;
	}
	
	public String toString(){
		return super.UID + " " + super.eventID + " " + attendance.toString() + " " + super.timestmp; 
	}
	
}
