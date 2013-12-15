package gigs.db.cloudsql;

import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.EventImageType;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class ImageWrapper {

	Connection c;
	String fname,content;
	private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
	public ImageWrapper()
	{
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int addImage(EventImage i)
	{
		int success = 2;
		
		try{
			String keyString = (i.key.getKeyString());
//			if(i.toString().length()>12)
//				keyString = ((i.key).toString()).substring(10, (i.key).toString().length()-1);
			System.out.println("Blob key: " + keyString);
			String statement ="INSERT IGNORE INTO images (IID, blobkey, imgtype, OID) VALUES " +
					"( \'" +i.IID + "\', \'" + keyString + "\', \'" + i.type + "\', " + i.ownerID + ")"; 
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
//		    System.out.println(statement);
			PreparedStatement stmt = c.prepareStatement(statement);
		    stmt.executeUpdate();
		   
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return success;
	}
	
	public int deleteImage(String str)
  	{
  		int success = 2;
  		try{
	    	String statement ="DELETE FROM images WHERE IID = \'" + str + "\'";
		     
		    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		    PreparedStatement stmt = c.prepareStatement(statement);
		    success = stmt.executeUpdate();
		    c.close();
  		} catch (Exception e) {
			e.printStackTrace();
		}         
		return success;
	 }
	
	public String getImageId(int PID){
		
		String statement ="SELECT * FROM posts WHERE PID = \'" + PID + "\'";
		String IID = "";     
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
				 
			while(rs.next()){  				
					 IID = rs.getString("IID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return IID;
	}
	
	public int getImageOwner(EventImage ei){
		
		String statement ="SELECT OID FROM images WHERE IID = \'" + ei.IID + "\'";
		int oid = 0;
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeUpdate();
			ResultSet rs = stmt.getResultSet();
				 
			while(rs.next()){  				
					 oid = rs.getInt("OID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return oid;
	}
	
	public int getImageOwner(String IID){
		
		String statement ="SELECT OID FROM images WHERE IID = \'" + IID + "\'";
		int oid = 0;
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeUpdate();
			ResultSet rs = stmt.getResultSet();
				 
			while(rs.next()){  				
					 oid = rs.getInt("OID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return oid;
	}
	
	public String getOwnersImage(int id){
		
		String statement ="SELECT IID FROM images WHERE OID = \'" + id + "\'";
		String IID = "";
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeUpdate();
			ResultSet rs = stmt.getResultSet();
				 
			while(rs.next()){  				
					IID = rs.getString("IID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return IID;
	}
	
	public BlobKey getBlobKey(String IID){
		String statement ="SELECT * FROM images WHERE IID = \'" + IID + "\'";
		BlobKey key = null;     
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
				 
			while(rs.next()){  				
					 key = new BlobKey(rs.getString("blobkey"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return key;
		
	}
	public EventImage getImage(String IID){
		  
		  BlobKey key = new BlobKey("");
		  EventImageType type;
		  int oid;
		  String statement ="SELECT * FROM images WHERE IID = \'" + IID + "\'";
		     EventImage ei = null;
		     
		     try {
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		   PreparedStatement stmt = c.prepareStatement(statement);
		      stmt.executeQuery();
		      ResultSet rs = stmt.getResultSet();
		        
		      key = new BlobKey(rs.getString("blobkey")); 
		      type = (EventImageType) rs.getObject("imgtype");
		      oid = rs.getInt("OID");
		      ei = new EventImage(key, type, oid, IID);
		      
		  }catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }     
		   
		  return ei;
		 }

	//	TODO
//	public BlobKey getOwnersImageBlob(int id){
//		
//		String statement ="SELECT IID FROM images WHERE OID = \'" + id + "\'";
//		String IID = "";
//		try {
//			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
//			PreparedStatement stmt = c.prepareStatement(statement);
//			stmt.executeUpdate();
//			ResultSet rs = stmt.getResultSet();
//				 
//			while(rs.next()){  				
//					IID = rs.getString("IID");
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return IID;
//	}

	public URL getImageURL(String IID){
		
		URL imageURL = null;
		BlobKey key = new BlobKey("");
		String statement ="SELECT * FROM images WHERE IID = \'" + IID + "\'";
	     
	    try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
		    stmt.executeQuery();
		    ResultSet rs = stmt.getResultSet();
			 
			 while(rs.next()){  				
				 key = new BlobKey(rs.getString("blobkey"));			 	
			 }
			 
			//Transform Blob Key into URL
			ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(key);	
			System.out.println("Blob key: " + key);
			imageURL = new URL(imagesService.getServingUrl(key));
			 			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageURL;
	}
	
	public URL getPostImage(int pid)
	{
		URL url = null;
		BlobKey key = null;
		try {
		if(this.isImagePost(pid))
		{
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT blobkey FROM images WHERE OID = " + pid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			
			while(rs.next())
			{
				key = new BlobKey(rs.getString("blobkey"));
			}  

			c.close();
			
			if(key!=null){
			
				ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(key);	
				System.out.println("Blob key: " + key);
				try{
				url = new URL(imagesService.getServingUrl(options));
				} catch (IllegalArgumentException e)
				{
					url= new URL("https://dl.dropboxusercontent.com/u/4930607/placeholder.jpg");
				}
			}
			else url = new URL("https://dl.dropboxusercontent.com/u/4930607/placeholder.jpg");

		}
		else url = new URL("stageous.com");
		} catch (Exception e) {
			e.printStackTrace();
			URL eurl = null;
			try {
				eurl = new URL("https://dl.dropboxusercontent.com/u/4930607/placeholder.jpg");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return eurl;
		}
		return url;
	}
	
	
	public boolean isImagePost(int pid){

		int OID = -1;
		try{

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM images WHERE OID = " + pid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			while(rs.next()){             
				OID = rs.getInt("OID");
			}
			c.close();            
		}catch (Exception e) {
			e.printStackTrace();
		}

		if(OID>-1)
			return true;
		return false;
	}
	
	//Returns the Images of a certain post
	public EventImage getEventImage(int PID, int EID){
		
		EventImage eImage = null;
		BlobKey key = new BlobKey("");
		String imageType = "";
		String iid = "";
		EventImageType type = null;
		 
  		try{
  			
	    	String statement ="SELECT * FROM images WHERE OID = \'" + PID + "\'";
		     
		    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		    PreparedStatement stmt = c.prepareStatement(statement);
		    stmt.executeQuery();
		    ResultSet rs = stmt.getResultSet();
			
			 while(rs.next()){  				
				 key = new BlobKey(rs.getString("blobkey"));
				 imageType = rs.getString("imgtype");
				 iid = rs.getString("IID");
			 }
			 
			//Specify the image type onto the enum
			 if(imageType.equals("EVENTUSERIMAGE"))
				 type = EventImageType.EVENTUSERIMAGE;	
			 else if(imageType.equals("PROFILEIMAGE"))
				 type = EventImageType.PROFILEIMAGE;	
			 else /*if(imageType.equals("EVENTFMIMAGE"))*/
				 type = EventImageType.EVENTFMIMAGE;
			 
			 //Transform Blob Key into URL
//			 ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(key);
//			 URL imageURL = new URL(imagesService.getServingUrl(options));

//			 EventImage(BlobKey key, EventImageType t, int OID, boolean without) 
			 eImage = new EventImage(key, type, EID, iid);
			 
		    c.close();
  		} catch (Exception e) {
			e.printStackTrace();
		}         
		    return eImage;			 
	}
	
	public URL getEventImage(int eid)
	 {
	  URL url = null;
	  BlobKey key = null;
	  try 
	  {
	  
	   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	   String statement ="SELECT blobkey FROM images WHERE OID = " + eid;
	   PreparedStatement stmt = c.prepareStatement(statement);
	   stmt.executeQuery();

	   ResultSet rs = stmt.getResultSet();
	   
	   while(rs.next())
	   {
	    key = new BlobKey(rs.getString("blobkey"));
	   }  

	   c.close();
	   
	   if(key!=null)
	   {
	   
	    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(key); 
	    System.out.println("Blob key: " + key);
	    try
	    {
	     url = new URL(imagesService.getServingUrl(options));
	    } catch (IllegalArgumentException e)
	    {
	     url= new URL("https://dl.dropboxusercontent.com/u/4930607/placeholder.jpg");
	    }
	   }
	   else 
	    url = new URL("https://dl.dropboxusercontent.com/u/4930607/placeholder.jpg");
	  } catch (Exception e) {
	   e.printStackTrace();
	   URL eurl = null;
	   try {
	    eurl = new URL("https://dl.dropboxusercontent.com/u/4930607/placeholder.jpg");
	   } catch (MalformedURLException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	   }
	   return eurl;
	  }
	  return url;
	 }

	//	TODO
//	public BlobKey getOwnersImageBlob(int id){
//		
//		String statement ="SELECT IID FROM images WHERE OID = \'" + id + "\'";
//		String IID = "";
//		try {
//			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
//			PreparedStatement stmt = c.prepareStatement(statement);
//			stmt.executeUpdate();
//			ResultSet rs = stmt.getResultSet();
//				 
//			while(rs.next()){  				
//					IID = rs.getString("IID");
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return IID;
//	}
}
