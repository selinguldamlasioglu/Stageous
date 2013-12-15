package test;

import gigs.db.cloudsql.UserWrapper;
import gigs.engine.auth.GeoCoder;
import gigs.engine.event.user.GeoLocation;
import gigs.engine.event.user.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class GeoTestServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		
		UserWrapper uw = new UserWrapper();
		int max = uw.getNewUserid();
		
		for(int i = 0; i<max; i++)
		{
			if(i==3 || i== 6 || i==7 || i==9) ;
			else{
				User u = uw.getUser(i);
				GeoLocation newgeo = GeoCoder.reverseGeocode(u.location.latitude, u.location.longitude);
				u.location = newgeo;
				uw.editUser(i, u);
				System.out.println("Geo: " + newgeo.city + " " + newgeo.country);
			}
		}
	}
}