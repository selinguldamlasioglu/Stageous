package gigs.engine.event.user;

import gigs.db.cloudsql.EventWrapper;
import gigs.db.cloudsql.ImageWrapper;
import gigs.db.cloudsql.UserWrapper;
import gigs.engine.auth.lastfm.*;
import gigs.engine.event.rec.XMLClipper;

import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.w3c.dom.Document;


public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6247060297304387782L;

	
	public String username;
	public String password;
	public int UID;
//	public String IID;
//	public EventImage profilePicture;
	public ArrayList<Integer> followedUsers; // Id's of the followed users are stored
	public ArrayList<Tag> tagCloud;
	
	public String twauth; // Twitter Authorization Data : username, password
	public String fbauth; // Facebook Authorization Data : username, password
	public String description;
	public GeoLocation location;
	public String email;
	
	
	// Bu IID olacak
	public String imageURL;
	
	public User(){
		//default constructor used while adding new User 
		this.UID = -1;
//		this.IID = wrapper.getImageID(userID);
		this.username = "";
		this.password = "";
		this.description = "";
		this.imageURL = "";
		this.location = new GeoLocation();
		this.email = "";
		this.fbauth = "";
		this.twauth = "";
		tagCloud = new ArrayList<Tag>();
		followedUsers = new ArrayList<Integer>();
	}
	
	
	public User(int userID, String username, String password) {
		UserWrapper wrapper = new UserWrapper();
	
		User user = wrapper.getUser(userID);
	
		if(user != null){
			this.UID = user.UID;
//			this.IID = wrapper.getImageID(userID);
			this.username = username;
			this.password = password;
			this.description = user.description;
			this.imageURL = user.imageURL;
			this.location = user.location;
			this.email = user.email;
			this.fbauth = user.fbauth;
			this.twauth = user.twauth;
			this.tagCloud = wrapper.getTagCloud(user.UID);
			this.followedUsers = wrapper.getFollowedIds(user.UID);	
		}
	}
	
	public User(String userName){
		User user;
		try {
			UserWrapper wrapper = new UserWrapper();
			
			user = wrapper.getUser(userName);
			
			if(user != null){
				this.UID = user.UID;
//				this.IID = wrapper.getImageID(UID);
				this.username = user.username;
				this.password = user.password;
				this.description = user.description;
				this.location = user.location;
				this.email = user.email;
				this.fbauth = user.fbauth;
				this.twauth = user.twauth;
				this.imageURL = user.imageURL;
				this.tagCloud = wrapper.getTagCloud(user.UID);
				this.followedUsers = wrapper.getFollowedIds(user.UID);
				// TODO
//				this.profilePicture = iwrapper.getImage(IID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public User(int UID){
		
		User newUser = null;

		try {
			UserWrapper wrapper = new UserWrapper();
		
			newUser = wrapper.getUser(UID);
			
			if( newUser != null){
				this.UID = newUser.UID;
				this.username = newUser.username;
				this.password = newUser.password;
				this.description= newUser.description;
				this.location = newUser.location;
				this.imageURL = newUser.imageURL;
				this.email = newUser.email;
				this.fbauth = newUser.email;
				this.twauth = newUser.twauth;
				this.tagCloud = wrapper.getTagCloud(UID);
				this.followedUsers = wrapper.getFollowedIds(UID);
//				this.IID = wrapper.getImageID(UID);
//				this.profilePicture = iwrapper.getImage(IID);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addUser(String username, String password, String email, GeoLocation loc) {
			
			UserWrapper wrapper = new UserWrapper();
			
			try {
				int newuid = wrapper.getNewUserid();
				this.twauth = ""; this.description = ""; this.fbauth = "";
				
				if(newuid % 2 == 0){
					//TODO: add meaningful URL
					this.imageURL = "/images/userf.png";
					
					wrapper.addUser(username, password, imageURL, "", "", "", loc, email);
				}
				else if(newuid % 2 == 1){
					//TODO: add meaningful URL
					this.imageURL = "/images/userm.png";
					
					wrapper.addUser(username, password, imageURL, "", "", "", loc, email);
				}
				this.tagCloud = new ArrayList<Tag>();
				this.followedUsers = new ArrayList<Integer>();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void addUser(String username, String password, String email, ArrayList<Tag> tags, GeoLocation loc)
	{
		  UserWrapper wrapper = new UserWrapper();
		  int s = tags.size();
		  
		  try {
			   int newuid = wrapper.getNewUserid();
			   this.twauth = "";
			   this.description = ""; 
			   this.fbauth = "";
			   
			   if(newuid % 2 == 0){
			    //TODO: add meaningful URL
				   this.imageURL = "/images/userf.png";
				   wrapper.addUser(username, password, imageURL, "", "", "", loc, email);
			   }
			   else if(newuid % 2 == 1){
			    //TODO: add meaningful URL
				   this.imageURL = "/images/userm.png";
				   wrapper.addUser(username, password, imageURL, "", "", "", loc, email);
			   }
			   
			   for(int i = 0; i<s ; i++)
			   {
				   wrapper.addTagLike(newuid, tags.get(i).name, tags.get(i).interest);
			   }
			   
			   this.tagCloud = tags;
			   this.followedUsers = new ArrayList<Integer>();
		  } catch (Exception e) {
			   e.printStackTrace();
		  }
		  
	}
		
	public FollowPost addFollowPost(int pid){
		return new UserWrapper().getFollowPost(pid);
	}
		
	public boolean followUser(int userID) {
		
		//Checks if the user is already being followed
		if(!(followedUsers.contains(userID))){
			followedUsers.add(followedUsers.size(),userID);
			new UserWrapper().addFollow(this.UID, userID);
		}
		
		if(followedUsers.contains(userID))
			return true;
		return false;
	}

	public boolean unfollowUser(int userID) {
		
		int i = 0;
		int userIndex = -1;
		userIndex = followedUsers.indexOf(userID);
		
		if(userIndex!=-1){
			followedUsers.remove(i);
			new UserWrapper().deleteFollow(this.UID, userID);
			return true;
		}
		return false;
	}	
	//TODO - CHECK
	public void addPost(Post p) {
		UserWrapper wrapper = new UserWrapper();
		//Check if the post belongs to the user
		if(p.UID != UID)
				return;
		wrapper.addPost(p);
		if(p instanceof ImagePost){
			System.out.println("Adding image post");
			wrapper.addPostImage((ImagePost)p);
		}
		else if(p instanceof VideoPost){
			System.out.println("Adding video post");
			wrapper.addVideoPost((VideoPost)p);			
		}		
	}
	
	public void addPost(int EID, String text, Timestamp t1){
		  UserWrapper wrapper = new UserWrapper();
		  int newid = wrapper.getNewPostID();
		  Post p = new Post(newid, this.UID, EID, text, t1);
		  wrapper.addPost(p);		
    }
    
	public void addPost(int EID, String text, Timestamp t1, EventImage eimage){
		UserWrapper wrapper = new UserWrapper();  
		int newid = wrapper.getNewPostID();
		ImagePost p = new ImagePost(newid, this.UID, EID, text, t1, eimage);
		wrapper.addPost(p);
		wrapper.addPostImage(p);
    }
    
	public void addPost(int EID, String text, Timestamp t1, String embedLink){
		UserWrapper wrapper = new UserWrapper();  
		int newid = wrapper.getNewPostID();
		VideoPost p = new VideoPost(newid, this.UID, EID, text, t1, embedLink);   
		wrapper.addPost(p);
		wrapper.addVideoPost(p);
	}	
		 
	boolean editSelfPost(String text, Post p) throws SQLException {		
		if(p.UID==this.UID){
			new UserWrapper().editPost(text, p.PID);
			return true;
		}
		return false;		
	}

	public void addComment(String comment)
	{
		UserWrapper wrapper = new UserWrapper();
		int newcid = wrapper.getNewCommentid();
		int newpid = wrapper.getNewPostID();
		Comment c = new Comment(this.UID, newcid, newpid, comment);
		wrapper.addComment(c);
	}
	
	public boolean deleteSelfPost(Post p){	
		
		if(p.UID==this.UID){
			new UserWrapper().deletePost(p.PID);
			return true;
		}
		return false;
	}

	public void sendMessage(ArrayList<Integer> recepientIDs, String messageData, Timestamp timeStamp){
		UserWrapper wrapper = new UserWrapper();
		int newid = wrapper.getNewMessageid();
		Message m = new Message(newid, this.UID, this.UID, recepientIDs, messageData, timeStamp);
		wrapper.addMessage(m);
		
		for(int i=0; i < recepientIDs.size(); i++){
			wrapper.addMessageToUsers(newid, recepientIDs.get(i), MessageState.UNREAD); 
		}
	}

	public boolean deleteMessage(Message message){
		
		if(message.senderID==this.UID){
			new UserWrapper().deleteMessage(message.MID);
			return true;
		}
		return false;
	}

	//Attendance = GOING, NOTGOING, INTERESTED
	public void attendEvent(int eventid, Attend state) {
		if(!state.equals(Attend.NOTGOING)){
			new UserWrapper().addEventAttendance(this.UID, eventid, state);
		}
		else{
			new UserWrapper().deleteEventAttendance(this.UID, eventid);
		}			
	}

	
	//TODO FINISH THIS!!!!!
	public void setTagInterest(){
		//wrapper.addTagLike(this.UID, str, interest);		
	}

	public boolean deleteSelfComment( Comment comment) {
		if(comment.UID==this.UID){
			new UserWrapper().deleteComment(comment.CID);
			return true;
		}
		return false;		
	}
	public void setFollowerList() {
		followedUsers = new UserWrapper().getFollowers(UID);
	}
	
	public void setTagCloud() 
	{
		tagCloud = new UserWrapper().getTagCloud(UID);
	}
	
	public ArrayList<Integer> getFollowers() {
		return new UserWrapper().getFollowers(UID);
	}
	
	public ArrayList<Integer> getFollowees() {
		return new UserWrapper().getFollowedIds(UID);
	}
	
	//Returns the users that current user follows
	public ArrayList<User> getFollowedUsers() {
		return new UserWrapper().getFollowed(UID);
	}
	
	public ArrayList<Integer> getThresholdPosts(Timestamp max, int threshold){
		
		int MAX_SEARCH = 10;
		int search = 1;
		Timestamp min = new Timestamp(max.getTime()-60*60*1000);
		UserWrapper wrapper = new UserWrapper();
		
		ArrayList<Integer> result = null;
		result = wrapper.getFollowerPostIdsByTime(UID, min, max);
		int minutes = 6*60;
		long min_t, m;
		  
		// System.out.println("Timestamp old: " + min.getTime() + "Timestamp new: " + ts1.getTime());
		while(result.size() < threshold && search<MAX_SEARCH){
			min_t = min.getTime();
		    m = minutes * 60 * 1000;
		    max = new Timestamp(min_t - 1); 
		    min = new Timestamp(min_t - m);
		   
		    result.addAll(wrapper.getFollowerPostIdsByTime(this.UID,min,max));
		    search++;
		} 
		return result;
	}
	
	 public ArrayList<Integer> getUserThresholdPosts(Timestamp max, int threshold){
		  
		 int MAX_SEARCH = 10;
		 int search = 1; 
		 Timestamp min = new Timestamp(max.getTime()-60*60*1000);
		 UserWrapper wrapper = new UserWrapper();
		 
		  ArrayList<Integer> result = null;
		   result = wrapper.getUserPostIdsByTime(this.UID, min, max);
		   int minutes = 6*60;
		   long min_t, m;
		     
		   // System.out.println("Timestamp old: " + min.getTime() + "Timestamp new: " + ts1.getTime());
		   while(result.size() < threshold && search<MAX_SEARCH){
			   min_t = min.getTime();
		       m = minutes * 60 * 1000;
		       max = new Timestamp(min_t - 1); 
		       min = new Timestamp(min_t - m);
		      
		       result.addAll(wrapper.getUserPostIdsByTime(this.UID, min, max));
		       search++;
		   } 
		  return result;
		 }
	 
	 public ArrayList<Event> searchEvent(String query, int pageNumber){ 
		 
		  //Get similar tag events
		  ArrayList<Event> relatedEvents = new ArrayList<Event>(); 
		  try{
		   LastfmGetter tagGetter = new LastfmGetter();
		   Document tagResults = tagGetter.getGeoTagEventsXML(this.location, new Tag(query));
		   XMLClipper tagClipper = new XMLClipper(tagResults);
		   int numOfTags = tagClipper.getResultCount();
		   
		   //Get similar artist events
		   LastfmGetter artistGetter = new LastfmGetter();
		   Document artistResults = artistGetter.getArtistEventsXML(query);
		   XMLClipper artistClipper = new XMLClipper(artistResults);
		   int numOfArtists = artistClipper.getResultCount();
		   
		   
		   System.out.println("numOfArtists: " + numOfArtists);
		   System.out.println("numOfTags: " + numOfTags);
		   
		   //Use artist results if they are greater in number 
		   
		   if(numOfArtists >= numOfTags){
		    if(numOfArtists!=0){
		     
		     artistResults = artistGetter.getArtistEventsXMLPage(query, pageNumber);
		     relatedEvents = new XMLClipper(artistResults).XML2Events();
		     
		    } 
		   }
		   
		   else{ //Tag results if not
		    if(numOfTags!=0){
		     tagResults = tagGetter.getGeoTagEventsXMLPage(this.location, new Tag(query), pageNumber);
		     relatedEvents = new XMLClipper(tagResults).XML2Events();    
		    } 
		   }
		  }
		  catch (Exception e) {
		   e.printStackTrace();
		  }
		  return relatedEvents;    
	}
	
//	public URL getImageURL()
//	{
//		return new ImageWrapper().getImageURL(this.imageURL);
//	}
	
	public boolean checkUserName(String username)
	{
	   return new UserWrapper().validateUsername(username);
	}
	
	public String toString(){
		return "" + this.UID + " " + this.username + " " + this.password + " " + this.description;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getUID() {
		return UID;
	}


	public void setUID(int uID) {
		UID = uID;
	}


//	public String getIID() {
//		return IID;
//	}


//	public void setIID(String iID) {
//		IID = iID;
//	}


//	public EventImage getProfilePicture() {
//		return profilePicture;
//	}
//
//
//	public void setProfilePicture(EventImage profilePicture) {
//		this.profilePicture = profilePicture;
//	}


	public ArrayList<Tag> getTagCloud() {
		return tagCloud;
	}


	public void setTagCloud(ArrayList<Tag> tagCloud) {
		this.tagCloud = tagCloud;
	}


	public String getTwauth() {
		return twauth;
	}


	public void setTwauth(String twauth) {
		this.twauth = twauth;
	}


	public String getFbauth() {
		return fbauth;
	}


	public void setFbauth(String fbauth) {
		this.fbauth = fbauth;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public GeoLocation getLocation() {
		return location;
	}


	public void setLocation(GeoLocation location) {
		this.location = location;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setFollowedUsers(ArrayList<Integer> followedUsers) {
		this.followedUsers = followedUsers;
	}


	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getImageURL() {
		return imageURL;
	}


	//Returns the users that current user is followed by
	 public ArrayList<User> getFollowingUsers() {
	  return new UserWrapper().getFollowingUsers(UID);
	 }
	
	 public Attend getEventAttendance(int EID)
	 {
		 return new UserWrapper().getEventAttendance(this.UID, EID);
	 }

	 public boolean isFollowing(int id2){
	  if(new UserWrapper().isFollowing(UID, id2))
	   return true;
	  return false;
	 }

	 public AttendPost getAttendPost(int eventid){
	  return new UserWrapper().getAttendPost(this.UID, eventid);
	 }
	 
	 public String getProfileImage()
	 {
	  if(this.imageURL.equals("/images/userf.png")  || this.imageURL.equals("/images/userm.png") || this.imageURL.contains("/images/") )
	   return this.imageURL;
	  else
	   return "";
	 }
	 
	 public ArrayList<Event> getLatestEventAttendance(int threshold)
	 {
		ArrayList<Integer> ids = new UserWrapper().getLatestEventAttendance(this.UID, threshold);
		ArrayList<Event> events =new ArrayList<Event>();
		EventWrapper ew = new EventWrapper();
		for(int i=0; i<ids.size();i++)
    	{
    		events.add(ew.getEvent(ids.get(i)));
    	}
		
		return events;
	 }
}
	