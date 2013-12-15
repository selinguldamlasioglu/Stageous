package test;

import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.EventImageType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class ImageTestServlet extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		out.println("hi");	
		
		EventImage ei = new EventImage(new URL("http://userserve-ak.last.fm/serve/252/50605937.jpg"), EventImageType.EVENTFMIMAGE, 335987);
		
		//long time = System.currentTimeMillis();
		//System.out.println("Sleeping... zzz...");
		
		//while(System.currentTimeMillis() < time+100)
		//{
			//System.out.print("z");
		//}
		
		//System.out.println("Woke up.");
		
//		out.println(ei.toURL());
//
//		out.println(ei.toThumbURL());
		
//		blobstoreService.serve(ei.key, resp);
		
		out.println(ei.toURL());
	}
}

