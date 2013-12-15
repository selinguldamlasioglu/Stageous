package gigs;

/* Copyright (c) 2009 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.EventImageType;
import gigs.engine.event.user.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
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
public class UploadPhotoServlet extends HttpServlet {

	 private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
 
	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		User sessuser;
     	int threshold = 2;
     	System.out.println("0");
     	HttpSession session = request.getSession();
     	String n = (String)session.getAttribute("USER");
     	if(n!=null)
    	{
        	sessuser = new UserWrapper().getUser(n);   
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
    			
    			
    			if (blobKey != null) {
    				System.out.println("Blobkey has been found: " + blobKey);
    				ei = new EventImage(blobKey, EventImageType.EVENTUSERIMAGE, sessuser.UID );
    				response.setHeader("Content-Disposition", "attachment; filename=\"" + ei.IID + "\"");
    			   System.out.println("/serve?blob-key=" + blobKey.getKeyString());
    			}
    			
//    			if(ei!=null)
//    			{
//    				System.out.println("request.ei: " + ei.toString());
//    				System.out.println("text is: " + text);
//    				// TODO Add post: Event ids
//    				sessuser.addPost(0, text, new Timestamp(new Date().getTime()), ei);
//    				System.out.println("Adding image post...");
//    			}
    			
    			System.out.println(ei.toURL().toExternalForm());
    			request.setAttribute("preview", ei.toThumbURL(64).toExternalForm());
    			request.setAttribute("postimage", ei);
    			System.out.println("Redirecting...");
    			request.getRequestDispatcher("/jsp/feed.jsp").forward(request, response);
//    			response.sendRedirect("/home");
        	}
        	catch (Exception e) {
				 blobstoreService.delete(blobKey);
				 response.sendRedirect("/?error=" + URLEncoder.encode("Object save failed: " + e.getMessage(), "UTF-8"));
			}
        	return;
    	} 
	 }
}