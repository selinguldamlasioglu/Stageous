package gigs;

import gigs.engine.event.user.GeoLocation;
import gigs.engine.event.user.Tag;
import gigs.engine.event.user.User;
import gigs.engine.event.rec.Recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
        	ArrayList<Tag> alltags = Recommendation.weightedTagList(50);
        	request.setAttribute("alltags", alltags);
        	System.out.println("alltags size: " + alltags.size());
        	request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        	return;
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String n=request.getParameter("name");
		String e=request.getParameter("email");
		String p=request.getParameter("password");
		String l=request.getParameter("location");
		String t=request.getParameter("tags");
		
		System.out.println("\n\n\n\n\n\n\n\nTags:" + t);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if(t.length()>0 && ! t.equals("No tag selected yet."))
		{
			tags = readTags(t);
			System.out.println("tagsize: " + tags.size());
		}
			
		// Create user
		// public void addUser(String username, String password, String email, String twauth, String description, String fbauth, GeoLocation loc)
		
		User u = new User();
		if(! u.checkUserName(n))
		{
			System.out.println("Username ERROR");
			response.sendRedirect("?error=USERNAME WRONG");
			return;
		}
		HttpSession session = request.getSession(); 
	    session.setAttribute("USER", n); 
	    GeoLocation geo = new GeoLocation(l);
//	    System.out.println(geo.city + " " + geo.country + " ");
		
	    if(tags.size()>0)
		    u.addUser( n, p, e, tags, geo );
		else	
			u.addUser( n, p, e, geo );
		
		System.out.println("Redirecting to profile...");
	    response.sendRedirect("/profile/" + n); 
	    return;
	}
	
	static ArrayList<Tag> readTags(String s)
	{
		StringTokenizer tokens = new StringTokenizer(s, ",");
		ArrayList<Tag> tags = new ArrayList<Tag>();
		while(tokens.hasMoreTokens())
		{
			String tag = tokens.nextToken();
			tags.add(new Tag(tag, 50));
			System.out.println(tag);
		}
		
		return tags;
	}
}
