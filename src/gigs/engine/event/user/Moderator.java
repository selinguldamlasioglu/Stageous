package gigs.engine.event.user;

import gigs.db.cloudsql.UserWrapper;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class Moderator extends User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5159071249149296059L;

	public Moderator(int userID, String username, String password) throws MalformedURLException, SQLException {
		super(userID, username, password);
	}

	public void deletePost(int postID) throws SQLException {
		new UserWrapper().deletePost(postID);
	}

	public void deleteComment(int commentID) throws SQLException{
		new UserWrapper().deleteComment(commentID);
	}
	
}