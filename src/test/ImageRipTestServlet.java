package test;

import gigs.engine.auth.lastfm.LastfmGetter;
import gigs.engine.event.rec.XMLClipper;
import gigs.engine.event.user.EventImage;
import gigs.engine.event.user.Tag;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.rdbms.AppEngineDriver;
import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("all")
public class ImageRipTestServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try{
			ServletOutputStream out = resp.getOutputStream();
			DriverManager.registerDriver(new AppEngineDriver());
			
			String statement = "SELECT EID FROM eventCache WHERE CHAR_LENGTH(IID) = 0";
	    	Connection c = (Connection) DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
	    	out.println(statement);
	    	PreparedStatement stmt = c.prepareStatement(statement);

			stmt.executeQuery();	
			ResultSet rs = stmt.getResultSet();
			
			ArrayList<Integer> ids = new ArrayList<Integer>();
			while(rs.next()){
				if(rs.getInt("EID") > 0)
					ids.add(rs.getInt("EID"));
		    }
			out.println("Size: " + ids.size());
			
			EventImage ei = null;
		    LastfmGetter lfg = new LastfmGetter();
			for(int i=0; i<ids.size(); i++)
			{
				statement = "SELECT * FROM images WHERE OID = " + ids.get(i) ;
				out.println(statement);
		    	
				c = (Connection) DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
				stmt = c.prepareStatement(statement);
				stmt.executeQuery();
				
				int size = stmt.getFetchSize();
				if(size<1)
				{
					XMLClipper xml = new XMLClipper(lfg.getEventInfoXML(ids.get(i), 0));
					ei = xml.XML2ClipEventImage();
					
					statement = "UPDATE eventCache SET IID = \'" + ei.IID + "\' WHERE EID = " + ei.ownerID;
					out.println(statement);
			    	
					c = (Connection) DriverManager.getConnection("jdbc:google:rdbms://egigdb:stageousdba/stageous");
					stmt = c.prepareStatement(statement);
					stmt.executeUpdate();	
				}
//				this.wait(100);
			}
			
			c.close();
//			resp.sendRedirect(ei.toURL().toExternalForm());
			
			return;
			
	 	} catch(Exception e) { e.printStackTrace(); }
	
	}

}
