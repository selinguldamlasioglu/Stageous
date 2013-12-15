package test;

import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.*;
import gigs.engine.event.rec.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.*;

public class RecTestServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		//ArrayList<Integer> recepients= new ArrayList<Integer>();
		//Timestamp ts= new Timestamp((new Date()).getTime());
		
		User u = new UserWrapper().getUser(26);
		try {
			ArrayList<Event> e = Recommendation.recUserNewEvents(u, 20);
			
			for (int i = 0; i<e.size(); i++)
				out.println(e.get(i).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//GeoLocation loc = new GeoLocation(45.67, 90.44, "Ankara", "Turkey"); 
		//User u = new User(100, "Ali", "pass1", "http://www.google.com.tr/imgres?imgurl=http://www.vipresim.com/uploads/Kis-ve-Nehir-Manzarasi.jpg", "twauth", "description", "fbauth", loc);
	}
}
