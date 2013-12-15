package gigs.db.cloudsql;
import com.google.appengine.api.rdbms.AppEngineDriver;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import gigs.engine.event.user.*;

	public class EventWrapper implements Serializable{
	Connection c;
//	String fname,content;
	
	public EventWrapper()
	{
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ADD EVENT ARTISTS 
    
	public int addEventArtists(int EID, ArrayList<String> artist) throws SQLException
    {    	
		String statement = null;
    	for(int i = 0; i<artist.size(); i++)
	    {
    		statement = "INSERT IGNORE INTO eventArtists (EID, artist, aindex) VALUES (" + EID + ", \'" + artist.get(i) + "\', " + i + ")";
    		
    		c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
	    	System.out.println(statement);  
			stmt.executeUpdate();		
						  
	      }
	      c.close();
    	return 0;
    }
    
    public int deleteEventArtists(int id1) throws SQLException
	{
    	int success = 2;
    	String statement ="DELETE FROM eventArtists WHERE EID =" + id1 + "";
	   
	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);
	    success = stmt.executeUpdate();
	   
	    c.close();
	            
        return success;
	}
    
    public int deleteEventArtists(int id1, String artistName) throws SQLException
	{
    	int success = 2;
    	String statement ="DELETE FROM eventArtists WHERE EID =" + id1 + "" + " AND artist=" + "\'" + artistName + "\'";
	   
	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);
	    success = stmt.executeUpdate();
	   
	    c.close();
	            
        return success;
	}
    public ArrayList<String> getEventArtists(int eventID) throws SQLException{
    	
    	  ArrayList<String> artists = new ArrayList<String>();
    	  String statement ="SELECT artist AS eventArtist FROM eventArtists WHERE EID =" + eventID;				  
	      c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	      PreparedStatement stmt = c.prepareStatement(statement);			    
	      
	      stmt.executeQuery();
	      ResultSet rs = stmt.getResultSet();			 
	      
	      while(rs.next())	  
	    	  artists.add(((String) (rs.getObject("eventArtist"))));				    	  
		      			  
	      c.close();	
	      
		  return artists;
    }
    
  //EVENT TAGS
    public int addEventTags(int eventid, ArrayList<Tag> tags) throws SQLException
    {
    	String statement = null;
    	
	    for(int i = 0; i<tags.size(); i++){
	    	statement = "INSERT IGNORE INTO eventTags (EID, tagName, tindex) VALUES " + "(" + eventid + ",\'" + tags.get(i).name + "\'," + 0 + ")";
	    	System.out.println(statement);
	    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeUpdate();
		    c.close();
	    }
    	return 0;
    }
    
    public int deleteEventTags(int EID) throws SQLException
	{
    		int success = 1;
    		String statement ="DELETE FROM eventTags WHERE EID = \'" + EID + "\'";
	    
	    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    	PreparedStatement stmt = c.prepareStatement(statement);
	    	success = stmt.executeUpdate();
	    	c.close();
	            
	    	return success;

  }
    public ArrayList<Tag> getEventTags(int eventID) throws SQLException{
    	
    	ArrayList<Tag> eventTags = new ArrayList<Tag>();
    	
    	String statement ="SELECT tagName AS eventTagColumn FROM eventTags WHERE EID =" + eventID;				  
	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);			    
	      
	    stmt.executeQuery();
	    ResultSet rs = stmt.getResultSet();			 
	      
	    while(rs.next()){
	    	Tag t = new Tag((String)rs.getObject("eventTagColumn"));
	    	eventTags.add(t);	
	    
	    }			    	  
		      			  
	    c.close();	
	      
    	return eventTags;
    	
    }
    
  // EVENT INTERESTS
    
    public int setEventAttendance(int userID, int eventID, Attend interest ) throws SQLException{
    	
    	int success = 2; 
    	//"INSERT INTO eventArtists (EID, artist) VALUES " + "(" + EID + ",\'" + artist.get(i) + "\')"; 	
    	String statement = "DELETE FROM eventAttendance WHERE UID = " + userID;
    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
    	PreparedStatement stmt = c.prepareStatement(statement);
  
    	stmt.executeUpdate();
    	
    	if(!((interest.toString()).equals("NOTGOING"))){
    		
	    	//Set Attendance
    		statement = "INSERT IGNORE INTO eventAttendance (UID, EID, attend) VALUES" + "(" + userID + "," + eventID + ",\'" + interest + "\')" ;
	    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    	stmt = c.prepareStatement(statement);
	    	stmt.executeUpdate();
	    	
	    	updateTagInterest(userID, eventID, 10);
	    	
    	}
    	
    	else
    		updateTagInterest(userID, eventID, -5);
    	return success;
    }
    
    //Update Tag Interest
    public void updateTagInterest(int userID, int eventID, int value) throws SQLException{
    	
    	String statement;
    	ArrayList<Tag> eventTags = getEventTags(eventID);
    	
    	for(int i=0; i<eventTags.size();i++){
    		statement = "UPDATE tagLikes " +
    					"SET interest = interest + " + value + 
    					" WHERE UID=" + userID + " AND tagName=\'" + eventTags.get(i).name + "\'";
    		
    		System.out.println(statement);
        	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
        	PreparedStatement stmt = c.prepareStatement(statement);
        	stmt = c.prepareStatement(statement);
        	stmt.executeUpdate();
    		
    	}
    	
    	
    	
    }
    public Attend getUserEventInterest(int userID, int eventID) throws SQLException{
    	
    	Attend attendance = Attend.NOTGOING;
    	
    	String statement = "SELECT attend as eventInterest FROM eventAttendance WHERE UID = " + userID + " AND EID=" + eventID ;
    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
    	PreparedStatement stmt = c.prepareStatement(statement);
    	
    	stmt.executeQuery();
    	ResultSet rs = stmt.getResultSet();
    	
    	while(rs.next()){
    		
    		String data = rs.getString("eventInterest");
    		
    		if(data.equals("INTERESTED"))
    			attendance = Attend.INTERESTED;
    		else if(data.equals("GOING"))
    			attendance = Attend.GOING;
    		else if(data.equals("NOTGOING"))
    			attendance = Attend.NOTGOING;
    		
    	}	
    	
    	c.close();
    	
    	return attendance; 	
    	
    }
    
    public ArrayList<Integer> getUsersEvents(int userid) throws SQLException{
    	
    	ArrayList <Integer> userEvents = new ArrayList <Integer>();
    	String statement = "SELECT EID as events FROM eventAttendance WHERE UID = " + userid ;
    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
    	PreparedStatement stmt = c.prepareStatement(statement);
    	
    	stmt.executeQuery();
    	ResultSet rs = stmt.getResultSet();
    	
    	while(rs.next())	
    		userEvents.add((Integer)(rs.getObject("events")));	
    	    	
    	c.close();
	
    	return userEvents;
    	
    }
    
  //EVENTCACHE
    
    public int addEvent(Event event) throws SQLException
    {
    	int success = 2;
     
    	String statement ="INSERT IGNORE INTO eventCache (EID, title, startTime, descr, IID, ticket, VID, lastAccess) VALUES " +
          "(?,?,?,?,?,?,?, CURRENT_TIMESTAMP)";
       
    	System.out.println(statement);
    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
        PreparedStatement stmt = c.prepareStatement(statement);
        stmt.setInt(1, event.eventID);
        stmt.setString(2, event.title);
        
//        if(event.time.toTimestamp().toString().contains("GMT"))
//        {
//        	String time = event.time.toTimestamp().toString().replace("GMT", "");
//        	stmt.setTimestamp(3, event.time.toTimestamp());
//        }
//        else	
        stmt.setString(3, event.time.toString());
        stmt.setString(4, event.description);
        stmt.setString(5, event.image.IID);
        stmt.setString(6, event.ticketLink);
        stmt.setInt(7, event.venueID);
         
      	success = stmt.executeUpdate();
         
//        this.addEventTags(event.eventID, event.tagList);
//        this.addEventArtists(event.eventID, event.artists);
        c.close();
       
        return success;
    } 
    
    //Get ID Event
    public Event getEvent(int eventID) 
   	{
    	
   		Event currentEvent = null;
   		try{		
	   		int eid = -1;
	   		String title = null;
	   		String IID = "";
	   		Timestamp stamp = null;
	   		String description = null;
	   		String ticket = null;
	   		int VID = -1;
	   		
	   		c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	   		String statement ="SELECT * FROM eventCache WHERE EID = " + eventID;
	   	    PreparedStatement stmt = c.prepareStatement(statement);
	   	    stmt.executeQuery();
	   	      
	   	    ResultSet rs = stmt.getResultSet();
	   	 
	   	    while(rs.next()){  
	   	    		eid = (Integer)rs.getObject("EID");
	   	    		title = (String)rs.getObject("title");
	   	    		stamp = rs.getTimestamp("startTime");
	   	    		description = rs.getString("descr");
	   	    		ticket = rs.getString("ticket");	    		
	   	    		VID = (Integer)rs.getObject("VID");
	   	    		IID = rs.getString("IID");
	   	    		System.out.println("*************CALLED BY GETEVENT*************");
	   	    		ArrayList<String> artists = getEventArtists(eid);
	   	    		ArrayList<Tag> tags = getEventTags(eid);	    			    			    	
	   	    		
//	   				String imageURL = "https://dl.dropbox.com/u/4930607/placeholder.jpg";
	   				EventImage eventImage = new EventImage(EventImageType.EVENTFMIMAGE, eventID, IID);
	   	    		currentEvent = new Event(eid,title,stamp, artists, tags, description, eventImage,ticket,VID);
	   	    		
	   		 }
	   	     System.out.println("Event Name: " + title); 
	   	     System.out.println(" "); 
	   	     c.close();
   		}
   		catch(Exception e){
   			e.printStackTrace();
   		}
   	    return currentEvent;
   	  } 
    
    //Get Event Containing given Tag
    public ArrayList<Event> getTagEvent(Tag tag) throws SQLException, MalformedURLException, IOException
	{
		ArrayList<Event> events = new ArrayList<Event>();
				
		c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		String statement ="SELECT EID FROM eventTags WHERE tagName = \'" + tag.name + "\'";
	    PreparedStatement stmt = c.prepareStatement(statement);
	    stmt.executeQuery();
	      
	    ResultSet rs = stmt.getResultSet();
	 
	    while(rs.next()) 
	    		events.add(getEvent(rs.getInt("EID")));
			    	      
	    c.close();
	   
	    return events;
	} 
    
    public int deleteEventCache(int id) throws SQLException
	{
    	int success = 2;
    	String statement ="DELETE FROM eventCache WHERE EID =" + id + "";
	   
	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);
	    success = stmt.executeUpdate();
	    this.deleteEventTags(id);
	    this.deleteEventArtists(id);
	   
	    c.close();
	      
	    return success;
  }
    
    public int addVenue(Venue v) throws SQLException{
    	
    	int success = 2;
    	String statement ="INSERT IGNORE INTO venues (VID, venueName, city, country, street, lat, longt) VALUES (" + v.id + ",\'" + 
    	v.name + "\',\'" + v.city + "\',\'" + v.country + "\',\'" + v.street + "\'," + v.geo.latitude + "," + v.geo.longitude + ")";
	   
	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);
	    success = stmt.executeUpdate();
	    	   
	    c.close();
	      
	    return success;
    }
    
    public Venue getVenue(int venueID) throws SQLException{
    	
    	String statement = "SELECT * FROM venues WHERE VID = " + venueID ;
	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);
	    System.out.println("I'M DA BITCH!!!");
	    System.out.println(statement);
	    stmt.executeQuery();
	    ResultSet rs = stmt.getResultSet();
	    
    	rs.next(); 
	    Venue venue = new Venue(rs.getInt("VID"), rs.getString("venueName"),rs.getString("city"), rs.getString("country"), rs.getString("street"), new GeoLocation(rs.getDouble("lat"), rs.getDouble("longt"),rs.getString("city"), rs.getString("country") ));	
	      
	    c.close();
	      
	    return venue;
    	
    }    
    
    public int deleteVenue(Venue v) throws SQLException{
    	
    	int success = 2;
    	String statement = "DELETE FROM venues WHERE VID = " + v.id;

	    c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    PreparedStatement stmt = c.prepareStatement(statement);
	    success = stmt.executeUpdate();
	    	   
	    c.close();
	      
	    return success;
    	
    }


    //Get Events of the followers of the user
	public ArrayList<Integer> getFollowingEvent(int userid) throws SQLException {
		
		ArrayList <Integer> set = new ArrayList <Integer>();
    	String statement = "SELECT DISTINCT EID as eventIds FROM eventAttendance WHERE UID IN (SELECT followee FROM follow WHERE follow.follower = " + userid + ")";
    	c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
    	PreparedStatement stmt = c.prepareStatement(statement);
    	stmt.executeQuery();
    	
    	ResultSet rs = stmt.getResultSet();
    	
    	while(rs.next())	  
	    	  set.add((Integer)(rs.getObject("eventIds")));	
    	
    	c.close();
	
    	return set;
	}
	
	public ArrayList<Integer> getEventPostIdsByTime(int EID, Timestamp min, Timestamp max)
	  {
	    ArrayList<Integer> result = new ArrayList<Integer>();
	    
	    try{
	     
	      c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	 
	      String statement2 ="SELECT PID FROM posts WHERE ptime BETWEEN \'" + min + "\' AND \'" + max +"\' AND EID = " + EID + " ORDER BY ptime DESC";
	        
	      System.out.println(statement2);
	      PreparedStatement stmt2 = c.prepareStatement(statement2);   
	 
	      stmt2.executeQuery();
	      ResultSet rs2 = stmt2.getResultSet();
	 
	      while(rs2.next()){
	       result.add(rs2.getInt("PID"));
	      }
	 
	      System.out.println("result.size() " + result.size());
	      for(int i = 0; i < result.size(); i++){
	       System.out.println("result[i]: " + result.get(i));
	      }
	      
	      c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	 
	      String statement3 ="SELECT PID FROM eventAttendance WHERE atime BETWEEN \'" + min + "\' AND \'" + max +"\' AND EID = " + EID + " ORDER BY atime DESC";
	        
	      System.out.println(statement3);
	      PreparedStatement stmt3 = c.prepareStatement(statement3);   
	 
	      stmt3.executeQuery();
	      ResultSet rs3 = stmt3.getResultSet();
	 
	      while(rs3.next()){
	       result.add(rs3.getInt("PID"));
	      }
	     
	     System.out.println("result.size() " + result.size());
	      for(int i = 0; i < result.size(); i++){
	       System.out.println("result[i]: " + result.get(i));
	      }
	      c.close();
	     }
	    
	     catch (Exception e) {
	      e.printStackTrace();
	     }
	    return result;
	 }
	 
	 public ArrayList<Integer> getAttendingUserIds(int EID){
		  ArrayList<Integer> result = new ArrayList<Integer>();
		  try{
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

		   String statement2 = "SELECT UID FROM eventAttendance where EID = " + EID + " AND attend = \"GOING\"";
		   PreparedStatement stmt2 = c.prepareStatement(statement2);   

		   stmt2.executeQuery();
		   ResultSet rs2 = stmt2.getResultSet();

		   while(rs2.next()){
		    result.add(rs2.getInt("UID"));

		   }
		   c.close();
		  }

		  catch (Exception e) {
		   e.printStackTrace();
		  }
		  return result;
	}
	public ArrayList<Integer> getInterestedUserIds(int EID){
		  ArrayList<Integer> result = new ArrayList<Integer>();
		  try{
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

		   String statement2 = "SELECT UID FROM eventAttendance where EID = " + EID + " AND attend = \"INTERESTED\"";
		   PreparedStatement stmt2 = c.prepareStatement(statement2);   

		   stmt2.executeQuery();
		   ResultSet rs2 = stmt2.getResultSet();

		   while(rs2.next()){
		    result.add(rs2.getInt("UID"));

		   }
		   c.close();
		  }

		  catch (Exception e) {
		   e.printStackTrace();
		  }
		  return result;
	}
	
	public ArrayList<Event> getVenueEvents(int VID){
	     
	     ArrayList<Event> venueEvents = new ArrayList<Event>();

	     int EID = -1;
	     String IID = "";
	     String title = "";
	     Timestamp time = null;
	     ArrayList<String> artists = null;
	     ArrayList<Tag> tagList = null;
	     String description = "";
	     EventImage eImage = null;
	     String ticketLink = "";
	     int venueID = VID;
	     
	     try{
	      c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	      String statement ="SELECT * FROM eventCache WHERE VID = " + VID;
	         PreparedStatement stmt = c.prepareStatement(statement);
	         stmt.executeQuery();
	           
	         ResultSet rs = stmt.getResultSet();
	         
	      while(rs.next()){  
	          
	          EID = rs.getInt("EID");
	          title = rs.getString("title");
	          time = rs.getTimestamp("startTime");
	          description = rs.getString("descr");
	          ticketLink = rs.getString("ticket");
	          IID = rs.getString("IID");
	          artists = getEventArtists(EID);
	          tagList = getEventTags(EID);
	          
	          Event currentEvent = null;
	          EventImage eventImage = new EventImage(EventImageType.EVENTFMIMAGE, EID, IID);
	          currentEvent = new Event(EID,title,time, artists, tagList, description, eventImage,ticketLink,VID);
	          
	          venueEvents.add(currentEvent);
	         }
	        }
	     catch(Exception e){
	      e.printStackTrace();
	     }
	     return venueEvents;
	}
	    
	public ArrayList<Event> getArtistEvents(String artistName){
	     ArrayList<Event> events = new ArrayList<Event>();
	     ArrayList<Integer> eventIds = new ArrayList<Integer>();
	     try{
	      c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	      String statement ="SELECT * FROM eventArtists WHERE artist = \'" + artistName + "\'";
	         PreparedStatement stmt = c.prepareStatement(statement);
	         stmt.executeQuery();
	           
	         ResultSet rs = stmt.getResultSet();      
	         while(rs.next()){  
	          int id = rs.getInt("EID");
	          eventIds.add(id);
	         }
	                  
	         for(int i = 0; i<eventIds.size(); i++){
	          events.add(getEvent(eventIds.get(i)));
	         }
	         
	         c.close();
	        }
	     
	     catch(Exception e){
	      e.printStackTrace();
	     }
	     return events;
	}
    
}

