package test;


import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.Comment;
import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.EventImageType;
import gigs.engine.event.user.EventTime;
import gigs.engine.event.user.GeoLocation;
import gigs.engine.event.user.ImagePost;
import gigs.engine.event.user.Message;
import gigs.engine.event.user.MessageState;
import gigs.engine.event.user.Post;
import gigs.engine.event.user.User;
import gigs.engine.event.user.VideoPost;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.*;

import org.xml.sax.SAXException;

@SuppressWarnings("all")
public class DBTestServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter out = resp.getWriter();
		
		resp.setContentType("text/plain");
		out.println("Hello, world - DBTEST23");
		
		int UID = 8;
		  User user = new User(8,"u","p");  
		  UserWrapper uWrap = new UserWrapper();
		  Timestamp timeStamp = new EventTime("Wed, 05 May 2013 03:00:00").getTimestamp();
		  
		  int PID = uWrap.getNewPostID();
		  
		  URL imageURL1 = new URL("http://upload.wikimedia.org/wikipedia/en/thumb/f/fe/MiaDevotion.jpg/220px-MiaDevotion.jpg");
		  EventImage eImage1 = new EventImage(imageURL1,EventImageType.EVENTUSERIMAGE, PID);  
		  user.addPost(new ImagePost(PID, UID, 1111165, "Awesome, Mia Martina is coming to Hacettepe",timeStamp, eImage1));
		  
		  PID = uWrap.getNewPostID();
		  URL imageURL2 = new URL("http://www.israeli-t.com/images/options/16011.jpg");
		  EventImage eImage2 = new EventImage(imageURL2,EventImageType.EVENTUSERIMAGE, PID); 
		  user.addPost(new ImagePost(PID, UID, 3547156, "Wish I could find tickets to Yasmin Levy concert... ",timeStamp, eImage2));
		  
		  PID = uWrap.getNewPostID();
		  URL imageURL3 = new URL("http://www.inflexwetrust.com/wp-content/uploads/2013/01/Michael-Jackson.jpg");
		  EventImage eImage3 = new EventImage(imageURL3,EventImageType.EVENTUSERIMAGE, PID);
		  user.addPost(new ImagePost(PID, UID, 3547156, "RIP MJ.",timeStamp, eImage3));
		  
		  PID = uWrap.getNewPostID();
		  URL imageURL4 = new URL("http://www.irock109.com/img/albums/T/The%20Mamas%20And%20The%20Papas%20-%20The%20Papas%20&%20The%20Mamas.jpg");
		  EventImage eImage4 = new EventImage(imageURL4,EventImageType.EVENTUSERIMAGE, PID);
		  user.addPost(new ImagePost(PID, UID, 3569102, "One song that I can never have enough of: California Dreamin.",timeStamp, eImage4));
		  
		  PID = uWrap.getNewPostID();
		  URL imageURL5 = new URL("http://pixhost.me/avaxhome/2007-05-27/Cover_651.jpg");
		  EventImage eImage5 = new EventImage(imageURL5,EventImageType.EVENTUSERIMAGE, PID);
		  user.addPost(new ImagePost(PID, UID, 3504799, "Scorpions - Still Loving You <3",timeStamp, eImage5));
		  
		  PID = uWrap.getNewPostID();
		  URL imageURL6 = new URL("http://ml.naxos.jp/sharedfiles/images/cds/hires/V4954.jpg");
		  EventImage eImage6 = new EventImage(imageURL6,EventImageType.EVENTUSERIMAGE, PID);
		  user.addPost(new ImagePost(PID, UID, 1111165, "Was a fantastic concert!" ,timeStamp, eImage6));
	
	}
}