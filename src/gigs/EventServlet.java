package gigs;


import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.rec.PostComparator;
import gigs.engine.event.rec.Recommendation;
import gigs.engine.event.user.Attend;
import gigs.engine.event.user.Event;
import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.EventImageType;
import gigs.engine.event.user.Post;
import gigs.engine.event.user.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class EventServlet extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		User sessuser = null;
		Event thisevent = null;
		String userstate = "";
		UserWrapper uwrap = new UserWrapper();
		EventWrapper ewrap = new EventWrapper();
		
		int threshold = 10;
		int recthreshold = 5;
    	boolean sess = false;
    	
    	HttpSession session = request.getSession();
    	String n = (String)session.getAttribute("USER");
		
    	String req = request.getRequestURI();
    	String m = req.substring(req.lastIndexOf("event/"), req.length());
		m = m.replaceAll("event", "");
    	m = m.replaceAll("/", "");
		
		if(m != null)
    	{
        	thisevent = ewrap.getEvent(Integer.parseInt(m));
        	
        	ArrayList<Event> recevents = Recommendation.recRelatedEvents(thisevent, recthreshold);
        	request.setAttribute("recevents", recevents);
    	
			ArrayList<Post> posts = new ArrayList<Post>();
			ArrayList<Integer> postids = thisevent.getEventThresholdPosts(new Timestamp(new Date().getTime()), threshold);
			
			for(int i=0; i<postids.size();i++)
	    	{
	    		posts.add(uwrap.getPostObject(postids.get(i)));
	    	}
//			Collections.sort(posts, new PostComparator());
        	request.setAttribute("posts", posts);
	    	
	    	ArrayList<User> attending = thisevent.getAttendingUsers();
	    	request.setAttribute("attending", attending);
	    	
	    	ArrayList<User> interested = thisevent.getInterestedUsers();
	    	request.setAttribute("interested", interested);
	    	
	    	request.setAttribute("event", thisevent);
	    	request.setAttribute("eventartists", thisevent.artists);
    	}
		
		if(n != null && thisevent!= null)
    	{
        	sessuser = uwrap.getUser(n);
        	System.out.println("MARABA TELEVOLE" + sessuser);
        	
        	String uploadURL = blobstoreService.createUploadUrl("/event/" + m);
        	request.setAttribute("uploadURL", uploadURL);

    		if( uwrap.getEventAttendance(sessuser.UID, thisevent.eventID) == Attend.GOING )
        	{
        		userstate = "attending";
        	}
        	else if( uwrap.getEventAttendance(sessuser.UID, thisevent.eventID) == Attend.INTERESTED )
        	{
        		userstate = "interested"; 
        	}
        	else 
        	{ 
        		userstate = "notgoing";  
        	}
    		request.setAttribute("userstate", userstate);
    		sess = true;
    		request.setAttribute("username", n);
    	}
    	request.setAttribute("sess", sess);
    	try {
    		request.getRequestDispatcher("/jsp/event.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession();
		String n = (String) session.getAttribute("USER");
		
		// TODO Handle checkbox input
		if(n!=null)
		{
			User sessuser = new User(n);
			
			String req = request.getRequestURI();
	    	String m = req.substring(req.lastIndexOf("event/"), req.length());
			m = m.replaceAll("event", "");
	    	m = m.replaceAll("/", "");
			
			Event thisevent = new EventWrapper().getEvent(Integer.parseInt(m));
			
			if (request.getParameter("going") != null)
			{
				try
				{
					System.out.println("request.getParameter(going): " + (String) request.getParameter("going"));
					Attend newstate = Attend.valueOf((String) request.getParameter("going"));
					Attend oldstate = sessuser.getEventAttendance(thisevent.eventID);
					UserWrapper uw = new UserWrapper();
					
					uw.deleteEventAttendance(sessuser.UID, thisevent.eventID);
					
					if(!newstate.equals(oldstate))
						uw.addEventAttendance(sessuser.UID, thisevent.eventID, newstate);
					
					response.sendRedirect("/event/" + m);
					return;
				} catch (Exception e) { response.sendRedirect("#" + e.getStackTrace().toString()); }
			}
			
			else if (request.getParameter("textpost") != null)
			{
				System.out.println("request.getParameter(textpost): " + (String) request.getParameter("textpost"));
				String text = request.getParameter("textpost");
				sessuser.addPost(thisevent.eventID, text, new Timestamp(new Date().getTime()));
				System.out.println("Adding text post...");

				System.out.println("Redirecting...");
				response.sendRedirect("/event/" + m);
				return;
			}
			
			else if ( request.getParameter("photopost") != null )
			{
				String text = request.getParameter("photopost");
	        	
	        	Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
				 if (blobs.keySet().isEmpty()) {
					 response.sendRedirect("/?error=" + URLEncoder.encode("No file uploaded", "UTF-8"));
					 return;
				 }
				Iterator<String> names = blobs.keySet().iterator();
				String blobName = names.next();
				BlobKey blobKey = blobs.get(blobName);
				
	        	try{
	        		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
	    			BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
	    			
	    			response.setContentType(blobInfo.getContentType());
	    			
	    			
	    			EventImage ei = null;
	    			
	    			if (blobKey != null) 
	    			{
	    				System.out.println("Blobkey has been found: " + blobKey);
	    				ei = new EventImage(blobKey, EventImageType.EVENTUSERIMAGE, sessuser.UID);
	    				response.setHeader("Content-Disposition", "attachment; filename=\"" + ei.IID + "\"");
	    			   System.out.println("/serve?blob-key=" + blobKey.getKeyString());
	    			}
	    			
	    			if(ei!=null)
	    			{
	    				System.out.println("request.ei: " + ei.toString());
	    				System.out.println("text is: " + text);
	    				// TODO Add post: Event ids
	    				sessuser.addPost(thisevent.eventID, text, new Timestamp(new Date().getTime()), ei);
	    				System.out.println("Adding image post...");
	    			}
	    			
	    			System.out.println(ei.toURL());
	    			request.setAttribute("preview", ei.toThumbURL(64).toExternalForm());
	    			request.setAttribute("postimage", ei);
	    			System.out.println("Redirecting...");
//	    			request.getRequestDispatcher("/jsp/feed.jsp").forward(request, response);
	    			response.sendRedirect("/event/" + m);
	    			return;
	        	}
	        	catch (Exception e) {
					 blobstoreService.delete(blobKey);
					 e.printStackTrace();
					 response.sendRedirect("/?error=" + URLEncoder.encode("Object save failed: " + e.getMessage(), "UTF-8"));
				}
			}

		}
		
	}
	

}
