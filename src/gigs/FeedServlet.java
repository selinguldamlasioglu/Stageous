package gigs;

import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.rec.PostComparator;
import gigs.engine.event.rec.Recommendation;
import gigs.engine.event.user.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class FeedServlet extends HttpServlet {
	 private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
        	User sessuser = null;
        	
        	int threshold = 30;
        	int recthreshold = 10;
        	System.out.println("0");
        	HttpSession session = request.getSession();
        	String n = (String)session.getAttribute("USER");
        	
        	String uploadURL = blobstoreService.createUploadUrl("/home");
        	request.setAttribute("uploadURL", uploadURL);
        	   
        	if(n != null)
        	{
	        	sessuser = new UserWrapper().getUser(n);
	        	request.setAttribute("profile", n);
        	}
        	
        	if(sessuser != null)
        	{
	        	ArrayList<Post> posts = new ArrayList<Post>();

	        	ArrayList<Integer> postids = sessuser.getThresholdPosts(new Timestamp(new Date().getTime()), threshold);
	        	for(int i=0; i<postids.size();i++)
	        	{
	        		posts.add(new UserWrapper().getPostObject(postids.get(i)));
	        	}
//	        	Collections.sort(posts, new PostComparator());
	        	request.setAttribute("posts", posts);
	        	
	        	ArrayList<Event> latevents = sessuser.getLatestEventAttendance(threshold);
//	        	Collections.sort(posts, new PostComparator());
	        	request.setAttribute("latevents", latevents);
	        	
//	        	for(int i =0; i<posts.size(); i++)
//	        		System.out.println(posts.get(i));
	        	
	        	ArrayList<User> followed = sessuser.getFollowedUsers();
	        	request.setAttribute("followed", followed);
	        	
	        	ArrayList<Event> recevents = Recommendation.recFollowEvents(sessuser.UID, recthreshold);
	        	request.setAttribute("recevents", recevents);
	        	
//	        	ServletContext context = this.getServletContext();
	        	request.getRequestDispatcher("/jsp/feed.jsp").forward(request, response);
        	}
        	else
        		response.sendRedirect("/logout");
		} catch (ServletException e) { e.printStackTrace(); }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessuser = new User((String) session.getAttribute("USER"));
		Post newpost = null;
		
		if (request.getParameter("textpost") != null)
		{
			int comboid = Integer.parseInt(request.getParameter("textcombobox"));
			System.out.println("request.getParameter(textpost): " + (String) request.getParameter("textpost"));
			String text = request.getParameter("textpost");
			sessuser.addPost(comboid, text, new Timestamp(new Date().getTime()));
			System.out.println("Adding text post...");

			System.out.println("Redirecting...");
			response.sendRedirect("/home");
		}
		else if ( request.getParameter("searchKey") != null )
		{
			System.out.println("request.getParameter(searchkey): " + (String) request.getParameter("searchKey"));
			String query = request.getParameter("searchKey");
			query = query.replaceAll(" ", "+");
			response.sendRedirect("/search/" + query);
		}
		else if ( request.getParameter("photopost") != null )
		{
			int comboid = Integer.parseInt(request.getParameter("photocombobox"));
			
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
    				sessuser.addPost(comboid, text, new Timestamp(new Date().getTime()), ei);
    				System.out.println("Adding image post...");
    			}
    			
    			System.out.println(ei.toURL());
    			request.setAttribute("preview", ei.toThumbURL(64).toExternalForm());
    			request.setAttribute("postimage", ei);
    			System.out.println("Redirecting...");
//    			request.getRequestDispatcher("/jsp/feed.jsp").forward(request, response);
    			response.sendRedirect("/home");
        	}
        	catch (Exception e) {
				 blobstoreService.delete(blobKey);
				 e.printStackTrace();
				 response.sendRedirect("/?error=" + URLEncoder.encode("Object save failed: " + e.getMessage(), "UTF-8"));
			}
			
		}

	}
	
	
		

}

