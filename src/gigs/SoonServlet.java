package gigs;

import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class SoonServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			request.getRequestDispatcher("/jsp/soon.jsp").forward(request, response);
			if(!response.isCommitted())
				request.getRequestDispatcher("/jsp/soon.jsp").forward(request, response);
			return;
		} catch (ServletException e) { e.printStackTrace(); }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		
		String n=request.getParameter("email");
		new UserWrapper().addToMailingList(n);
		request.setAttribute("signedup", true);
		
		request.getRequestDispatcher("/jsp/soon.jsp").forward(request, response);
//		response.sendRedirect("soon");
		
		return;
			
	}
}
