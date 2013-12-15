package gigs;

import gigs.db.cloudsql.UserWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class LoginFailedServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
       try {
			request.getRequestDispatcher("/jsp/loginfailed.jsp").forward(request, response);
			return;
		} catch (ServletException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		
		String n=request.getParameter("username");
		String p=request.getParameter("password");
		
		if(new UserWrapper().validateUser(n, p)){
			HttpSession session = request.getSession(); 
		    session.setAttribute("USER", n); 
		    response.sendRedirect("feed"); 
		}
		else{
			request.getRequestDispatcher("/jsp/loginfailed.jsp").forward(request, response);
		}
		
		return;
	}
}
