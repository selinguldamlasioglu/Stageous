package gigs.engine.event.user.custom;

import java.net.URL;

import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.ImageWrapper;
import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.*;

public final class Functions {
	public static Event getPostEvent(Post p) {
		return p.getPostEvent();
	}
	
	public static String getPosterName(Post p) {
		return p.getPoster().username;
	}

	public static int getPostEventID(Post p) {
		return p.getPostEvent().eventID;
	}
	
	public static String getPostEventName(Post p) {
		return p.getPostEvent().title;
	}

	public static URL getImageURL(Post p) {
		ImagePost ip = new UserWrapper().getImagePost(p.PID);
		return ip.getImageURL();
	}
	
	public static String getProfileImageURL(User u) {
		return u.getImageURL();
	}
	
	public static boolean isImagePost(Post p) {
		return new ImageWrapper().isImagePost(p.PID);
	}
	
	public static URL getPostImage(Post p) {
		return new ImageWrapper().getPostImage(p.PID);
	}
	
	public static String getText(Post p) {
		return p.text;
	}
	
	public static String getTime(EventTime t)
	{
		return t.toString();
	}
	
	public static boolean isFollowPost(Post p) {
		return new UserWrapper().isFollowPost(p);
	}
	
	public static boolean isAttendPost(Post p) {
		return new UserWrapper().isAttendPost(p);
	}

//	public static String getFollowingUser(Post p) {
//		return "";
//	}
//	
//	public static String getFollowerUser(Post p) {
//		return "";
//	}
	
	public static String getFollowingName(Post p) {
		UserWrapper uw = new UserWrapper();
		FollowPost fp = uw.getFollowPost(p.PID);
		return uw.getUser(fp.followedID).username;
	}
	
	public static String getFollowerName(Post p) {
		UserWrapper uw = new UserWrapper();
		FollowPost fp = uw.getFollowPost(p.PID);
		System.out.println(p.PID+": " + fp.UID );
		return uw.getUser(fp.UID).username;
	}
	
	public static String getProfileImage(User u) {
		return u.getProfileImage();
	}
	
	public static String getPosterProfileImage(Post p) {
		return p.getPoster().getProfileImage();
	}
	
	public static Venue getVenue(Event e) throws Exception{
		return new EventWrapper().getVenue(e.venueID);
	}
	
	public static String getVenueName(Event e) throws Exception{
		return new EventWrapper().getVenue(e.venueID).name;
	}
	
	public static String getAttendState(Post p){
		UserWrapper uw = new UserWrapper();
		AttendPost ap = uw.getAttendPost(p.PID);
		Attend at = ap.attendance;
		
		if(at == Attend.GOING)
		{
			return " is going to ";
		}
		if(at == Attend.INTERESTED)
		{
			return " is interested in ";
		}
		return " isn't going to ";
	}
	
	public static String getAttenderName(Post p){
		UserWrapper uw = new UserWrapper();
		AttendPost ap = uw.getAttendPost(p.PID);
		
		return uw.getUser(ap.UID).username;
	}
	
	public static URL getEventPoster (Event e) {
		return new ImageWrapper().getEventImage(e.eventID);
	}
	
}