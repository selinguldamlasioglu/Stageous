package gigs;


import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.rec.PostComparator;
import gigs.engine.event.rec.Recommendation;
import gigs.engine.event.user.Attend;
import gigs.engine.event.user.Event;
import gigs.engine.event.user.Post;
import gigs.engine.event.user.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ProfileServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		User sessuser = null;
		User thisuser = null;
		String userstate = "";
		UserWrapper uwrap = new UserWrapper();
		
		int threshold = 20;
		int recthreshold = 10;
    	boolean sess = false;
    	
    	HttpSession session = request.getSession();
    	String n = (String)session.getAttribute("USER");
		
		String req = request.getRequestURI();
		String m = req.substring(req.lastIndexOf("profile/"), req.length());
		m = m.replaceAll("profile", "");
		m = m.replaceAll("/", "");
		
		if(m != null)
    	{
        	thisuser = uwrap.getUser(m);
    	
			ArrayList<Post> posts = new ArrayList<Post>();
			ArrayList<Integer> postids = thisuser.getUserThresholdPosts(new Timestamp(new Date().getTime()), threshold);
	    	for(int i=0; i<postids.size();i++)
	    	{
	    		posts.add(uwrap.getPostObject(postids.get(i)));
	    	}
	    	
//	    	Collections.sort(posts, new PostComparator());
        	request.setAttribute("posts", posts);
	    	
	    	ArrayList<User> followed = thisuser.getFollowedUsers();
	    	request.setAttribute("followed", followed);
	    	
	    	ArrayList<User> following = thisuser.getFollowingUsers();
	    	request.setAttribute("following", following);
	    	
	    	request.setAttribute("profile", thisuser);
	    	
	    	ArrayList<Event> userlatevents = thisuser.getLatestEventAttendance(10);
        	request.setAttribute("userlatevents", userlatevents);
    	}
		
		if(n != null && thisuser != null)
    	{
        	sessuser = uwrap.getUser(n);
        	sess = true;
//        	System.out.println("MARABA TELEVOLE" + sessuser);
        	try{
	        	if( sessuser.followedUsers.contains(thisuser.UID) )
	        	{
	        		userstate = "followed";
	        	}
	        	else if( sessuser.UID== thisuser.UID )
	        	{
	        		userstate = "thatsme"; 
	        	}
	        	else
	        	{
	        		userstate = "notfollowed"; 
	        	}	
	        	request.setAttribute("userstate", userstate); 
	        	request.setAttribute("sessuser", sessuser);
	        	ArrayList<Event> recevents = Recommendation.recUserNewEvents(thisuser, recthreshold);
	        	request.setAttribute("recevents", recevents);
		    	
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
//        		response.sendRedirect("#" + e.getStackTrace().toString());
        	}
    	}
    	
    	request.setAttribute("sess", sess);
        try {
			request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
			if(!response.isCommitted())
				request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
			return;
		} catch (ServletException e) { e.printStackTrace(); }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession();
		User sessuser = new User((String) session.getAttribute("USER"));
		
		String req = request.getRequestURI();
    	String m = req.substring(req.lastIndexOf("profile/"), req.length());
    	m = m.replaceAll("profile", "");
		m = m.replaceAll("/", "");
		
		UserWrapper uw = new UserWrapper();
		
		User thisuser = uw.getUser(m);
		
		try
		{
			
			System.out.println("\n\n\n\n\n\nPOST METHOD:");
			System.out.println("Follow is " + request.getParameter("follow") + " " + request.getParameter("follow").isEmpty());
			if (request.getParameter("follow").equals("1"))
			{
				uw.addFollow(sessuser.UID, thisuser.UID);
				System.out.println("Following..");
//				return;
			}
			
			else if (request.getParameter("follow").equals("0"))
			{
				uw.deleteFollow(sessuser.UID, thisuser.UID);
				System.out.println("Unfollowing..");
			}
//			if(sessuser.isFollowing(thisuser.UID) )
//				uw.deleteFollow(sessuser.UID, thisuser.UID);
//			else
//				uw.addFollow(sessuser.UID, thisuser.UID);
//			System.out.println("Adding..");
			System.out.println("/n/n/n/n/n/n/n/n/nREDIRECTING:");
			response.sendRedirect("../profile/" + m);

		} catch (Exception e) { response.sendRedirect("#" + e.getStackTrace().toString()); }
	
		return;
	}
}
