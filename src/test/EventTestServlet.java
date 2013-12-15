package test;

import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.Comment;
import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.EventImageType;
import gigs.engine.event.user.EventTime;
import gigs.engine.event.user.GeoLocation;
import gigs.engine.event.user.Message;
import gigs.engine.event.user.Post;
import gigs.engine.event.user.Tag;
import gigs.engine.event.user.User;
import gigs.engine.event.user.Venue;
import gigs.engine.event.user.Event;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.*;

import org.xml.sax.SAXException;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;

@SuppressWarnings("serial")
public class EventTestServlet extends HttpServlet {
//	public void doGet(HttpServletRequest req, HttpServletResponse resp)
//			throws IOException {
//		PrintWriter out = resp.getWriter();
//		
//		resp.setContentType("text/plain");
//		out.println("Hello, world - eventTest");
//	
//		//public Event ( int eventID, String title, Timestamp time, ArrayList<String> artists, ArrayList<Tag> tagList, String description, 
//			//	 EventImage image, String ticketLink, Venue venue)
//		int uid = 1111;
//		int eventid = 1112;
//		String attend = "GOING";
//		String tagName= "pop";
//		int interest = 4;
//		ArrayList<String>artist = new ArrayList<String>();
//		artist.add("ali");
//		artist.add("veli");
//		Timestamp timestamp = new EventTime("Wed, 03 Apr 2013 08:10:01").getTimestamp();
//		String iURL = "http://m.thelmagazine.com/imager/b/mobileimage/1586558/62da/book_covers.jpg";
//		EventImage ei1 = new EventImage(new URL(iURL), EventImageType.EVENTFMIMAGE, 0);
//		Venue v = null;
//		try {
//			
//			v = new Venue (90, "izmirvenue", "izmir", "turkey", "kordon", new GeoLocation(-1, -1));
//		} catch (SAXException e3) {
//			// TODO Auto-generated catch block
//			e3.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		ArrayList<Tag> tags = new ArrayList<Tag>();
//		//tags.add(new Tag("pop"));
//	
//		Event eventt = new Event(12433, "E1", timestamp, artist, tags, "description", ei1, "ticketLink", v);
//		out.println(eventt.toString());
//
//		
//		
//		resp.setContentType("text/plain");
//		out.println("ok1");
//		
//		try {
//			UserWrapper e1 = new UserWrapper();
//			EventWrapper e2 = new EventWrapper();
//			
//			//e1.addTagLike(uid, tagName, interest);
//			//e1.editTagLike(uid, tagName, 7);
//			//e1.deleteTagLike(uid, tagName);
//			
//			
//			/*e1.addEventAttendance(uid, eventid, "INTERESTED");
//			e1.addEventAttendance(6, 7, "INTERESTED");
//			e1.addEventAttendance(89, 13, "INTERESTED");
//			e1.deleteEventAttendance(6,7);
//			e1.editEventAttendance(89, 13, "GOING");
//			e1.editEventAttendance(89,13,"INTERESTED");*/
//			out.println("baglandým2");
//			e2.addEvent(eventt);
//			out.println("baglandým");
//			
//			//e1.addEventArtists(56,artist);
//			//e1.addEventTags(eventid, tags);
//			
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			out.println("caca");
//		}
//	}

}
