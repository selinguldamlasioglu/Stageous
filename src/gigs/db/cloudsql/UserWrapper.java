package gigs.db.cloudsql;
import com.google.appengine.api.rdbms.AppEngineDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

import gigs.engine.event.user.*;

public class UserWrapper{
	Connection c;
	String fname,content;

	public UserWrapper()
	{		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(" New user wrapper...");			
	}

	public boolean validateUser(String username, String password){

		String statement = "SELECT COUNT(UID) as foundCount FROM users WHERE username = \'" + username + "\' AND password = \'" + password + "\'";
		int count = 0;	
		PreparedStatement stmt;
		try {
			stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			rs.next();
			count = rs.getInt("foundCount");
			System.out.println("Result is : " + count);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(count>0)
			return true;
		return false;
	}
    
    public boolean validateUsername(String username){
        
        String statement = "SELECT * FROM users WHERE username = \'" + username + "\'";
        //System.out.println(statement);
        int size = 5;
        try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            
            PreparedStatement stmt = c.prepareStatement(statement);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            size = rs.getFetchSize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(size>0)
            return false;
        return true;
	}

	public boolean userExists(int id){

		String statement = "SELECT COUNT(UID) as foundCount FROM users WHERE UID =" + id;
		int count = 0;
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			rs.next();
			count = rs.getInt("foundCount");
			System.out.println("Result is : " + count);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(count>0)
			return true;
		return false;
	}

	// USER
	public int addUser(String username, String password, String IID, String twauth, String fbauth, String description, GeoLocation location, String email) 
	{
		int success = 2;

		String statement ="INSERT IGNORE INTO users (UID, username, password, IID, twauth, fbauth, description, lat, longt, city, country, email) " +
				"VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setInt(1, this.getNewUserid());
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.setString(4, IID);
			stmt.setString(5, twauth);
			stmt.setString(6, fbauth);
			stmt.setString(7, description);
			stmt.setDouble(8, location.latitude);
			stmt.setDouble(9, location.longitude);
			stmt.setString(10, location.city);
			stmt.setString(11, location.country);
			stmt.setString(12, email);
			stmt.executeUpdate();
			c.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}	    

		return success;
	}

	public int getNewUserid(){

		int newUID = -1;
		int oldUID = -1;
		try {
			String statement = "Select max(UID) as maxUID from users";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				oldUID = rs.getInt("maxUID");
			}
			newUID = oldUID + 1;
			c.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newUID;		
	}

	public User getUser(int userId) {

		User currentUser = new User();

		double lat = 0.0;
		double longt = 0.0;
		String city = null;
		String country = null;

		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM users WHERE UID = " + userId;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){  

				currentUser.UID = (Integer)rs.getObject("UID");
				currentUser.username = rs.getString("username");
				currentUser.password = rs.getString("password");
				currentUser.imageURL = rs.getString("IID");
				currentUser.twauth = rs.getString("twauth");
				currentUser.fbauth = rs.getString("fbauth");
				currentUser.description = rs.getString("description");

				lat = rs.getDouble("lat");
				longt = rs.getDouble("longt");
				city = rs.getString("city");
				country = rs.getString("country");
				GeoLocation geo = new GeoLocation(lat, longt, city, country);
				currentUser.location = geo;
				currentUser.email = rs.getString("email"); 	
				
				currentUser.tagCloud = this.getTagCloud(currentUser.UID);
				currentUser.followedUsers = this.getFollowedIds(currentUser.UID);	

				//		currentUser = new User(UID, username, password, email, IID, twauth, description, fbauth, new GeoLocation(lat, longt, city, country));

			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return currentUser;
	}
	public String getImageID(int userid){	
		String IID = null;

		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT IID FROM users WHERE UID = " + userid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){ 
				IID = rs.getString("IID");
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return IID;
	}

	public User getUser(String name){

		User currentUser = new User();

		double lat = 0.0;
		double longt = 0.0;
		String city = null;
		String country = null;

		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM users WHERE username = \'" + name + "\'";
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){  
				currentUser.UID = (Integer)rs.getObject("UID");
				currentUser.username = rs.getString("username");
				currentUser.password = rs.getString("password");
				currentUser.imageURL = rs.getString("IID");
				currentUser.twauth = rs.getString("twauth");
				currentUser.fbauth = rs.getString("fbauth");
				currentUser.description = rs.getString("description");

				lat = rs.getDouble("lat");
				longt = rs.getDouble("longt");
				city = rs.getString("city");
				country = rs.getString("country");
				GeoLocation geo = new GeoLocation(lat, longt, city, country);
				currentUser.location = geo;
				currentUser.email = rs.getString("email");
				
				currentUser.tagCloud = this.getTagCloud(currentUser.UID);
				currentUser.followedUsers = this.getFollowedIds(currentUser.UID);	

			}

			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return currentUser;
	}

	public int editUser(int id, User u){
		int success = 2;
		try{
			// (UID, username, password, IID, twauth, fbauth, description, lat, longt, city, country, email) " +
//			"VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			
			String statement ="UPDATE users SET UID=?, username=?, password=?, IID=?, twauth=?, fbauth=?, description=?, lat=?, longt=?, city=?, country=?, email=? WHERE UID=" + id + ";";
			System.out.println(statement);
			
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			
			stmt.setInt(1, u.UID);
			stmt.setString(2, u.username);
			stmt.setString(3, u.password);
			stmt.setString(4, u.imageURL);
			stmt.setString(5, u.twauth);
			stmt.setString(6, u.fbauth);
			stmt.setString(7, u.description);
			stmt.setDouble(8, u.location.latitude);
			stmt.setDouble(9, u.location.longitude);
			stmt.setString(10, u.location.city);
			stmt.setString(11, u.location.country);
			stmt.setString(12, u.email);
			success = stmt.executeUpdate();    

			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public int deleteUser(int id) 
	{
		int success = 2;
		try{
			String statement ="DELETE FROM users WHERE UID =" + id + "";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();   

			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	//COMMENT
	//TODO: check!
	public int addComment(Comment com)
	{
		int success = 2;
		try{
			String statement ="INSERT IGNORE INTO comments (CID, UID, PID, comment) VALUES( ?, ?, ?, ?)";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setInt(1, com.CID);
			stmt.setInt(2, com.UID);
			stmt.setInt(3, com.PID);
			stmt.setString(4, com.comment);
			success = stmt.executeUpdate();		
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public int getNewCommentid(){
		int newCID = -1;
		int oldCID = -1;
		try {
			String statement = "Select max(CID) as maxCID from comments";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				oldCID = rs.getInt("maxCID");
			}
			newCID = oldCID + 1;
			c.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newCID;		
	}
	//tested
	public int editComment(int cid, String comment)
	{
		int success = 2;
		try{
			String statement = "UPDATE comments SET comment = ? WHERE CID = ?";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setString(1, comment);
			stmt.setInt(2, cid);
			success = stmt.executeUpdate();
			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	} 

	public Comment getComment(int cid){

		Comment comment = new Comment();

		try{   
			int UID = -1;
			int CID = -1;
			int PID = -1;
			String com = "";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM comments WHERE CID = " + cid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){  
				comment.UID = rs.getInt("UID");
				comment.CID = rs.getInt("CID");
				comment.PID = rs.getInt("PID");
				comment.comment = rs.getString("comment");

				//comment = new Comment(UID, CID, PID, com);
			}
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return comment;
	}

	public int deleteComment(int id) 
	{
		int success = 2;	    	

		try{
			String statement ="DELETE FROM comments WHERE CID =" + id + "";			 
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();			      
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	//MESSAGE  
	public int getNewMessageid(){
		int newMID = -1;
		int oldMID = -1;
		try {
			String statement = "Select max(MID) as maxMID from messages";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				oldMID = rs.getInt("maxMID");
			}
			newMID = oldMID + 1;
			c.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newMID;		
	}
	public int addMessage(Message m) {
		int success = 2;

		try{
			String statement ="INSERT IGNORE INTO messages (MID, senderID, parentMID, message, messageTime) VALUES" + "(?,?,?,?,?)";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setInt(1, m.MID);
			stmt.setInt(2, m.senderID);
			stmt.setInt(3, m.parentMID);
			stmt.setString(4, m.message);
			stmt.setTimestamp(5, m.timestmp);
			success = stmt.executeUpdate();
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
	//tested  
	public int editMessage(int mid, String message){
		int success = 2;

		try{

			String statement = "UPDATE messages SET message = ? WHERE MID = ?";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setString(1, message);
			stmt.setInt(2, mid);
			success = stmt.executeUpdate();

			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public Message getMessage(int mid){

		Message message = new Message();

		try{
			int MID = -1;
			int senderID = -1;
			int parentMID = -1; 
			String msg = "";
			Timestamp msgTime = null;
			ArrayList<Integer> recepientIds = null;

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM messages WHERE MID = " + mid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){  
				message.MID = rs.getInt("MID");
				message.senderID = rs.getInt("senderID");
				message.parentMID = rs.getInt("parentMID");
				message.message = rs.getString("message");
				message.timestmp = rs.getTimestamp("messageTime");

				//message = new Message(MID, senderID, parentMID, recepientIds, msg, msgTime);
			}
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	} 

	public int deleteMessage(int id) 
	{
		int success = 2;
		try{
			String statement ="DELETE FROM messages WHERE MID =" + id + "";
			//			  
			//			  ALTER TABLE `advertisers`
			//			  ADD CONSTRAINT `advertisers_ibfk_1` FOREIGN KEY (`advertiser_id`) 
			//			      REFERENCES `jobs` (`advertiser_id`);

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();	
		}	

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	//POST		  
	public int addPost(Post p){

		int success = 2;

		try{
			String statement ="INSERT IGNORE INTO posts (PID, UID, EID, text, ptime) VALUES" + "(?, ?, ?, ?, ?)";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);

			stmt.setInt(1, p.PID);
			stmt.setInt(2, p.UID);
			stmt.setInt(3, p.eventID);
			stmt.setString(4, p.text);
			stmt.setTimestamp(5, p.timestmp);

			success = stmt.executeUpdate();
			c.close();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
	//tested		
	public int editPost(String postText, int pid) {

		int success = 2;

		try{
			String statement = "UPDATE posts SET text = ? WHERE PID = ?" ;

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setString(1, postText);
			stmt.setInt(2, pid);				  
			success = stmt.executeUpdate();

			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public int deletePost(int id) {

		int success = 2;

		try{
			String statement ="DELETE FROM posts WHERE PID =" + id + "";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();

			//Delete if its also in ImagePost table
			statement ="DELETE FROM images WHERE OID =" + id + "";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();

			//Delete if its also in VideoPost table
			statement ="DELETE FROM videoPosts WHERE PID =" + id + "";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}


	public int addPostImage(ImagePost p){
		//IID	blobkey	imgtype	OID
		int success = 2;
		try{
			String keyString = ((p.image.key).toString());
			if(p.image.key.toString().length()>12)
				keyString = ((p.image.key).toString()).substring(10, (p.image.key).toString().length()-1);
			System.out.println("Blob key: " + keyString);
			String statement ="INSERT IGNORE INTO images (IID, blobkey, imgtype, OID) VALUES( \'" + p.image.IID + "\', \'" + keyString + "\', \'" + EventImageType.EVENTUSERIMAGE + "\'," + p.PID + ")";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();  
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	} 


	public ImagePost getImagePost(int pid) {

		  ImagePost iPost = new ImagePost();

		  try{

		   int postID = pid; 
		   int UID = -1; 
		   int eventID = -1;
		   String text = "";
		   Timestamp timestmp = null;
		   EventImage image = null;
		   String IID = null;

		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		   String statement ="SELECT * FROM images WHERE OID = " + pid;
		   PreparedStatement stmt = c.prepareStatement(statement);
		   stmt.executeQuery();

		   ResultSet rs = stmt.getResultSet();

		   while(rs.next()){  
		    iPost.PID = rs.getInt("OID");
		    IID = rs.getString("IID");
		   }

		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		   String statement2 ="SELECT * FROM posts WHERE PID = " + pid;
		   PreparedStatement stmt2 = c.prepareStatement(statement2);
		   stmt2.executeQuery();

		   ResultSet rs2 = stmt2.getResultSet();

		   while(rs2.next()){
		    iPost.UID = rs2.getInt("UID");
		    iPost.eventID = rs2.getInt("EID");
		    iPost.timestmp = rs2.getTimestamp("ptime");   
		   }

		   ImageWrapper w = new ImageWrapper();
		   image = w.getEventImage(postID, eventID);
		   System.out.println("GET EVENT IMAGE IID " + image.IID + " URL:" + image.toURL());
		   iPost = new ImagePost(postID, UID, eventID, text, timestmp, image); 
		   c.close();
		  }

		  catch (Exception e) {
		   e.printStackTrace();
		  }

		  return iPost;
	}

	public int deleteImagePost(int userID, int id){

		int success = 2;

		try{
			String statement ="DELETE FROM images WHERE OID =" + id + "";				  
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();

			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public int addVideoPost(VideoPost p)
	{
		int success = 2;
		try{
			String statement ="INSERT IGNORE INTO videoPosts (PID,embedLink ) VALUES( " + p.PID + ", \'" + p.embedLink +"\')";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	public int getNewPostID(){

		int newPID = -1;
		int oldPID = -1;
		try {
			String statement = "Select max(PID) as maxPID from posts";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				oldPID = rs.getInt("maxPID");
			}
			newPID = oldPID + 1;
			c.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return newPID;
	}


	public VideoPost getVideoPost(int pid) {

		VideoPost vPost = new VideoPost();

		try{
			int postID = -1; 
			int UID = -1; 
			int eventID = -1;
			String embedLink = "";
			String text = "";
			Timestamp timestmp = null;


			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM videoPosts WHERE PID = " + pid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){  
				vPost.PID = rs.getInt("PID");
				vPost.embedLink = rs.getString("embedLink");	
			}

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement2 ="SELECT * FROM posts WHERE PID = " + pid;
			PreparedStatement stmt2 = c.prepareStatement(statement2);
			stmt2.executeQuery();

			ResultSet rs2 = stmt2.getResultSet();

			while(rs2.next()){
				vPost.UID = rs2.getInt("UID");
				vPost.eventID = rs2.getInt("EID");
				vPost.text = rs2.getString("text");
				vPost.timestmp = rs2.getTimestamp("ptime");

			}

			//vPost = new VideoPost(postID, UID, eventID, text, timestmp, embedLink);
			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return vPost;

	}

	public int deleteVideoPost(int id) {

		int success = 2;

		try{
			String statement ="DELETE FROM videoPosts WHERE PID =" + id + "";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();
		}   
		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public Post getPostObject(int postid){

		Post p = new Post();

		try{

			int PID = -1;
			int UID = -1;
			int EID = -1;
			String text = "";
			Timestamp ptime = null;

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM posts WHERE PID = " + postid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while(rs.next()){

				p.PID = rs.getInt("PID");
				p.UID = rs.getInt("UID");
				p.eventID = rs.getInt("EID");
				p.text = rs.getString("text");
				p.timestmp = rs.getTimestamp("ptime");

			}  

			c.close();

			//return new Post(PID, UID, EID, text, ptime);   
			return p;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public FollowPost getFollowPost(int pid){
    	
    	FollowPost post = new FollowPost();
    	
    	try{
    		String statement ="SELECT * FROM follow WHERE PID = " + pid;
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            stmt.executeQuery();
            
            ResultSet rs = stmt.getResultSet();
            post.PID = pid;
            while(rs.next()){             	
            	post.UID = rs.getInt("follower");
	            post.followedID = rs.getInt("followee");
	            post.timestmp = (Timestamp)(rs.getObject("ftime"));  	            
            } 
            System.out.println("getfollow: " + post.PID);
    	}
    	catch (Exception e) {
            e.printStackTrace();
        }
        
        return post; 		
    	
    }
		
	public int generateFollowID(){
		
		int followID = -1;
		try{
    		String statement ="SELECT MIN(PID) as minId FROM follow";
            //System.out.println(statement);
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            stmt.executeQuery();
            
            ResultSet rs = stmt.getResultSet();
            if(rs==null)
            	return followID;
            while(rs.next()){ 
            	int id = rs.getInt("minId");  
            	if(id!=0)
            		followID = id -2; 
            	else
            		followID = -1;
            } 
        }
		
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return followID;
	}


	public String getEmbedLink(int postid){

		String link = null;

		try{

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			String statement ="SELECT * FROM videoPosts WHERE PID = " + postid;
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			while(rs.next()){
				//public EventImage(BlobKey key, EventImageType t, int OID, boolean without)
				link = rs.getString("embedLink");
			}
			c.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

		return link;
	}
	
    // FOLLOW
	public int addFollow(int id1, int id2)
    {
        int success = 2;
        int PID = generateFollowID();
        try{
            if(!(userExists(id1)) ){
                System.out.println("user 1 does not exist");
                return 0;
            }
            
            else if (!userExists(id2)){
                System.out.println("user 2 does not exist");
                return 0;
            }
                        
            String statement ="INSERT IGNORE INTO follow (follower, followee, ftime, PID ) VALUES( " + id1 + ", " + id2 + ", CURRENT_TIMESTAMP," + PID + ")";
            
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            success = stmt.executeUpdate();
            
            c.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return success;
    }
    
    
    public ArrayList<Integer> getFollowers(int userId){
        
        ArrayList<Integer> followers = new ArrayList<Integer>();
        try{
            
            String statement ="SELECT follower AS followerColumn FROM follow WHERE followee =" + userId;
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            
            System.out.println("Fetch size: " + rs.getFetchSize());
            while(rs.next())
                followers.add(((Integer)(rs.getObject("followerColumn"))));
            
            c.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return followers;
    }
    
    //Returns the Users followed by the given user
    public ArrayList<User> getFollowed(int userId){
        
        ArrayList<User> followed = new ArrayList<User>();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        
        try{
            ids = getFollowedIds(userId);
            for(int i=0; i<ids.size();i++){
                followed.add(getUser(ids.get(i)));
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return followed;
    }
    
    //Returns the User which follow the given user
    public ArrayList<User> getFollowingUsers(int userId){
        
        ArrayList<User> following = new ArrayList<User>();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        
        try{
            ids = getFollowers(userId);
            for(int i=0; i<ids.size();i++){
                following.add(getUser(ids.get(i)));
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return following;
    }
    
    public ArrayList<Integer> getFollowedIds(int userId){
        
        ArrayList<Integer> followers = new ArrayList<Integer>();
        try{
            
            String statement ="SELECT followee AS followerColumn FROM follow WHERE follower =" + userId;
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            
            System.out.println("Fetch size: " + rs.getFetchSize());
            while(rs.next())
                followers.add(((Integer)(rs.getObject("followerColumn"))));
            
            c.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return followers;
    }
    
    
    public int deleteFollow(int id1, int id2)
    {
        int success = 2;
        try{
            String statement ="DELETE FROM follow WHERE follower =" + id1 +" AND followee = " + id2;
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            success = stmt.executeUpdate();
            c.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }				         
        return success;
    }

	// MESSAGE TO USERS
	public int addMessageToUsers(int mid, int receiverid, MessageState state)
	{
		int success = 2;
		try{
			String statement ="INSERT IGNORE INTO messageToUsers (MID, receiverID, status) VALUES( ?, ?, ?)";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setInt(1, mid);
			stmt.setInt(2, receiverid);
			stmt.setString(3, state.name());
			success = stmt.executeUpdate();
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	} 

	public int editMessageToUsers(int mid, int uid, MessageState status )
	{
		int success = 2;
		try{
			String statement ="UPDATE messageToUsers SET status = ? WHERE MID = ? AND receiverID = ?";

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setString(1, status.name());
			stmt.setInt(2, mid);
			stmt.setInt(3, uid);
			success = stmt.executeUpdate();

			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public int deleteMessageToUsers(int mid, int receiverid)
	{
		int success = 2;
		try{
			String statement ="DELETE FROM messageToUsers WHERE MID =" + mid + " AND receiverID =" + receiverid + "";
			//  String statement ="DELETE FROM messageToUsers WHERE MID =" + id1+"";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}            
		return success;

	}

	// TAGLIKE

	public int addTagLike(int id, String str, int interest) 
	{
		int success = 2;
		try{
			String statement ="INSERT IGNORE INTO tagLikes (UID, tagName, interest) VALUES( " + id +  ", \'" + str  +"\', " + interest + ")";
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();	
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	} 

	public int editTagLike(int id, String str, int interest )
	{
		int success = 2;
		try{
			String statement ="UPDATE tagLikes SET interest =  " + interest + "WHERE UID =" + id + " AND tagName = \'" + str + "\'" ;

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public int deleteTagLike(int id, String str)
	{
		int success = 2;

		try{
			String statement ="DELETE FROM tagLikes WHERE UID =" + id + " AND tagName = \'" + str + "\'" ;

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public ArrayList<Tag> getTagCloud(int userId){

		ArrayList<Tag> tags = new ArrayList<Tag>();

		try{				  
			//int success = 2;
			String statement ="SELECT * FROM tagLikes WHERE UID = " + userId;				   
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			ResultSet rs = null;
			stmt.executeQuery();				      
			rs = stmt.getResultSet();					

			while(rs.next()){
				tags.add( new Tag(rs.getString("tagName"), rs.getInt("interest")));
				rs.next();
			}			      
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return tags;
	}


	// EVENT ATTENDANCE   
	public int generateAttendanceID(){
		
		int followID = 0;
		try{
    		String statement ="SELECT MIN(PID) as minId FROM eventAttendance";
            //System.out.println(statement);
            c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
            PreparedStatement stmt = c.prepareStatement(statement);
            stmt.executeQuery();
            
            ResultSet rs = stmt.getResultSet();
            if(rs==null)
            	return followID;
            while(rs.next()){ 
            	int id = rs.getInt("minId");  
            	if(id!=0)
            		followID = id -2; 
            	else
            		followID = -2;
            } 
        }
		
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return followID;
	}

	public int addEventAttendance(int uid, int eid, Attend state){
		int success = 2;
		int PID = generateAttendanceID();
		try{
			if(!state.equals(Attend.NOTGOING)){
				String statement ="INSERT IGNORE INTO eventAttendance (UID, EID, attend, atime, PID) VALUES( "+ uid + ", " + eid + ", \'" + state.name() + "\', CURRENT_TIMESTAMP, "+ PID + " )";

				c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
				PreparedStatement stmt = c.prepareStatement(statement);
//				stmt.setInt(1, uid);
//				stmt.setInt(2, eid);
//				stmt.setString(3, state.name());
//				stmt.setInt(5, PID);
				success = stmt.executeUpdate();
				c.close();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public int deleteEventAttendance(int uid, int eid)
	{
		int success = 2;

		try{
			String statement ="DELETE FROM eventAttendance WHERE UID =" + uid + " AND EID = " + eid + "" ;

			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();

			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	//GETPOST

	public ArrayList<Integer> getUserPosts(int userId) 
	{	        
		ArrayList<Integer> postIds = new ArrayList<Integer>();

		try{  
			//Get posts from the posts table
			String statement = "SELECT PID AS PIDColumn FROM posts WHERE UID =" + userId + "";
			//System.out.println(statement);
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			while(rs.next())   
				postIds.add(((Integer) (rs.getObject("PIDColumn"))));        			
			
			//Get posts from the follow table
			String statement1 = "SELECT PID FROM follow WHERE follower =" + userId + "";
			//System.out.println(statement1);
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

			PreparedStatement stmt1 = c.prepareStatement(statement1);
			stmt1.executeQuery();
			ResultSet rs1 = stmt1.getResultSet();

			while(rs1.next())   
				postIds.add(((Integer) (rs1.getObject("PID"))));           
			
			//Get posts from the eventAttendance table
			String statement2 = "SELECT PID FROM eventAttendance WHERE UID =" + userId + "";
			//System.out.println(statement2);
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

			PreparedStatement stmt2 = c.prepareStatement(statement2);
			stmt2.executeQuery();
			ResultSet rs2 = stmt2.getResultSet();

			while(rs2.next())   
				postIds.add(((Integer) (rs2.getObject("PID"))));           

			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return postIds;
	}

	//TODO TEST 
	public ArrayList<Integer> getFollowerPostIdsByTime(int userId, Timestamp min, Timestamp max){

		ArrayList<Integer> result = new ArrayList<Integer>();
		try{
			//Get posts from the posts table
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

			String statement2 ="SELECT PID FROM posts WHERE ptime BETWEEN \'" + min + "\' AND \'" + max +"\' AND UID IN " +
					"(SELECT followee AS UID FROM follow WHERE follower = " + userId +" UNION SELECT DISTINCT follower FROM follow WHERE follower=" + userId +") ORDER BY ptime DESC";
			////System.out.println(statement2);
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
			
			//Get posts from the follow table
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

			String statement3 ="SELECT PID FROM follow WHERE ftime BETWEEN \'" + min + "\' AND \'" + max +"\' AND follower IN " +
					"(SELECT followee AS UID FROM follow WHERE follower = " + userId +" UNION SELECT DISTINCT follower FROM follow WHERE follower=" + userId +") ORDER BY ftime DESC";
			////System.out.println(statement3);
			PreparedStatement stmt3 = c.prepareStatement(statement3);   

			stmt3.executeQuery();
			ResultSet rs3 = stmt3.getResultSet();

			while(rs3.next()){
				result.add(rs3.getInt("PID"));
			}

			System.out.println("After adding follow posts, result.size() of follow posts " + result.size());
			for(int i = 0; i < result.size(); i++){
				System.out.println("result[i]: " + result.get(i));
			}
			
			//Get posts from the eventAttendance table
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

			String statement4 ="SELECT PID FROM eventAttendance WHERE atime BETWEEN \'" + min + "\' AND \'" + max +"\' AND UID IN " +
					"(SELECT followee AS UID FROM follow WHERE follower = " + userId +" UNION SELECT DISTINCT follower FROM follow WHERE follower=" + userId +") ORDER BY atime DESC";
			//System.out.println(statement4);
			PreparedStatement stmt4 = c.prepareStatement(statement4);   

			stmt4.executeQuery();
			ResultSet rs4 = stmt4.getResultSet();

			while(rs4.next()){
				result.add(rs4.getInt("PID"));
			}

			System.out.println("After adding attend posts, result.size() of attend posts " + result.size());
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
	
	
	// TODO TO BE TESTED BY CEREN
	
	 public ArrayList<Integer> getUserPostIdsByTime(int userId, Timestamp min, Timestamp max){
		 
		  ArrayList<Integer> result = new ArrayList<Integer>();
		  try{
			  
		   //Posts from posts table
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

		   String statement2 = "SELECT PID FROM posts WHERE ptime BETWEEN \'" + min + "\' AND \'" + max +"\' AND UID = " + userId + " ORDER BY ptime DESC" ;
		      
		   //System.out.println(statement2);
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
		   
		   //Posts from follow table
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

		   String statement3 = "SELECT PID FROM follow WHERE ftime BETWEEN \'" + min + "\' AND \'" + max +"\' AND follower = " + userId + " ORDER BY ptime DESC" ;
		      
		   //System.out.println(statement3);
		   PreparedStatement stmt3 = c.prepareStatement(statement3);   

		   stmt3.executeQuery();
		   ResultSet rs3 = stmt3.getResultSet();

		   while(rs3.next()){
		    result.add(rs3.getInt("PID"));
		   }

		   System.out.println("After follow table, follow data result.size() " + result.size());
		   for(int i = 0; i < result.size(); i++){
		    System.out.println("result[i]: " + result.get(i));
		   }
		   
		   //Posts from event attendance table
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");

		   String statement4 = "SELECT PID FROM eventAttendance WHERE atime BETWEEN \'" + min + "\' AND \'" + max +"\' AND UID = " + userId + " ORDER BY ptime DESC" ;
		      
		   //System.out.println(statement4);
		   PreparedStatement stmt4 = c.prepareStatement(statement4);   

		   stmt3.executeQuery();
		   ResultSet rs4 = stmt4.getResultSet();

		   while(rs4.next()){
		    result.add(rs4.getInt("PID"));
		   }

		   System.out.println("After attendance table, follow data result.size() " + result.size());
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

	public ArrayList<Integer> getFollowerPostIds(int userId){
		ArrayList<Integer> followerPostIds = new ArrayList<Integer>();

		try{
			ArrayList<Integer> followerIds = getFollowers(userId);
			for(int i = 0; i < followerIds.size(); i ++){
				followerPostIds = getUserPosts(followerIds.get(i));
			}  
		}   
		catch (Exception e) {
			e.printStackTrace();
		}
		return followerPostIds;
	}

	public String blobToString(String blob){
		String s = blob.substring(10, blob.length()-1);
		return s;
	}

	 public Attend getEventAttendance(int uID, int eventID) {
		 
		  Attend state = Attend.NOTGOING; //by default
		  String at = "";
		  
		  try {
		   String statement = "SELECT attend FROM eventAttendance WHERE UID = " + uID + " AND EID = " + eventID;
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		   PreparedStatement stmt = c.prepareStatement(statement);
		   stmt.executeQuery();
		   ResultSet rs = stmt.getResultSet();
		   
		   while (rs.next()) {
		    at = rs.getString("attend");
		   }
		   
		   if(at.equals("GOING"))
             	state = Attend.GOING;
             else if(at.equals("INTERESTED"))
             	state = Attend.INTERESTED;
             else
             	state = Attend.NOTGOING;
		   
		   c.close();
		  }
		  catch (SQLException e) {
		   e.printStackTrace();
		  }

		  return state; 
	}
	 
	 public ArrayList<Integer> getLatestEventAttendance(int uID, int threshold) {
		 
		 ArrayList<Integer> res = new ArrayList<Integer>();
		 int i = 0;
		  try {
		   String statement = "SELECT EID FROM eventAttendance WHERE UID = " + uID + " ORDER BY atime DESC";
		   c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
		   PreparedStatement stmt = c.prepareStatement(statement);
		   stmt.executeQuery();
		   ResultSet rs = stmt.getResultSet();
		   
		   while (rs.next() && i<threshold) 
		   {
			    res.add(rs.getInt("EID"));
			    i++;
		   }
		   c.close();
		  }
		  catch (SQLException e) {
		   e.printStackTrace();
		  }

		  return res;
	}
	
	public boolean isFollowing(int id1, int id2) {
	 
	 boolean result = false;
	    try{
	        String statement ="SELECT COUNT(follower) as id1 FROM follow WHERE follower =" + id1 +" AND followee = " + id2;
	        c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	        PreparedStatement stmt = c.prepareStatement(statement);
	        stmt.executeQuery();
	        
	        ResultSet rs = stmt.getResultSet();
	        rs.next();
	        if(rs!=null)
	         if(rs.getInt("id1")>0)
	              result = true;   
	            c.close();
	        }
	        
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return result;
	}
	
public AttendPost getAttendPost(int userID, int eventID){
  		
      	AttendPost post = new AttendPost();
      	String attend = "";
      	try{
      		String statement ="SELECT * FROM eventAttendance WHERE UID = " + userID + " AND EID = " + eventID ;
              c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
              PreparedStatement stmt = c.prepareStatement(statement);
              stmt.executeQuery();
              
              ResultSet rs = stmt.getResultSet();
              while(rs.next()){  
              	post.PID = rs.getInt("PID");
  	            post.eventID = rs.getInt("EID");
  	            post.UID = rs.getInt("UID");
  	            attend = rs.getString("attend");
  	            post.timestmp = (Timestamp)(rs.getObject("atime"));  	            
              } 
              if(attend.equals("GOING"))
              	post.attendance = Attend.GOING;
              else if(attend.equals("INTERESTED"))
              	post.attendance = Attend.INTERESTED;
              else
              	post.attendance = Attend.NOTGOING;
              
      	}
      	catch (Exception e) {
              e.printStackTrace();
          }
      	return post;
      }
  	
    //TODO ADDED BY CEREN - TAKE IT
  	public AttendPost getAttendPost(int pid){
  		
      	AttendPost post = new AttendPost();
      	String attend = "";
      	try{
      		String statement ="SELECT * FROM eventAttendance WHERE PID = " + pid;
              //System.out.println(statement);
              c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
              PreparedStatement stmt = c.prepareStatement(statement);
              stmt.executeQuery();
              
              ResultSet rs = stmt.getResultSet();
              post.PID = pid;
              while(rs.next()){            
  	            post.eventID = rs.getInt("EID");
  	            post.UID = rs.getInt("UID");
  	            attend = rs.getString("attend");
  	            post.timestmp = (Timestamp)(rs.getObject("atime")); 	        
              } 
              if(attend.equals("GOING"))
              	post.attendance = Attend.GOING;
              else if(attend.equals("INTERESTED"))
              	post.attendance = Attend.INTERESTED;
              else
              	post.attendance = Attend.NOTGOING;
              
      	}
      	catch (Exception e) {
              e.printStackTrace();
          }
      	return post;
      }
  	
	 
	 public boolean isFollowPost(Post p){
			
			try{	
				c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
				String statement ="SELECT COUNT(PID) as pCount FROM follow WHERE PID =" + p.PID;
				//System.out.println(statement);
				PreparedStatement stmt = c.prepareStatement(statement);
				stmt.executeQuery();
		
				ResultSet rs = stmt.getResultSet();
				int num = 0;
				while(rs.next())
					num = rs.getInt("pCount");	
				System.out.println("Num: " + num);
				if(num>0)
					return true;
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	 
	 public boolean isAttendPost(Post p){
	  		try{	
	  			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	  			String statement ="SELECT COUNT(PID) as pCount FROM eventAttendance WHERE PID =" + p.PID;
	  			PreparedStatement stmt = c.prepareStatement(statement);
	  			stmt.executeQuery();
	  	
	  			ResultSet rs = stmt.getResultSet();
	  			int num = 0;
	  			while(rs.next())
	  				num = rs.getInt("pCount");	
	  			if(num>0)
	  				return true;
	  		}
	  		
	  		catch (Exception e) {
	  			e.printStackTrace();
	  		}
	  		return false;
	  }	
	 
	 public int addToMailingList(String email)
	{
		int success = 2;
		try{
			String statement ="INSERT IGNORE INTO mailingList (email) VALUES( \'" + email + "\' )";
			System.out.println(statement);
			c = DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
			PreparedStatement stmt = c.prepareStatement(statement);
			success = stmt.executeUpdate();		
			c.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
}