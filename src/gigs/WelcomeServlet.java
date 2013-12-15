package gigs;

import gigs.db.cloudsql.UserWrapper;
import gigs.engine.event.user.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class WelcomeServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
			request.getRequestDispatcher("/jsp/welcome.jsp").forward(request, response);
			return;
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		
		String n=request.getParameter("username");
		String p=request.getParameter("password");
		System.out.println("Hai.");
		System.out.println("u: "+ n + " p:" + p);
		
		if(new UserWrapper().validateUser(n, p))
		{
			System.out.println("Yes");
			User sessuser = new User(n);
			HttpSession session = request.getSession(); 
		    session.setAttribute("USER", n); 
		   
		    response.sendRedirect("home"); 
		}
		else{
			System.out.println("No");
			request.getRequestDispatcher("/jsp/loginfailed.jsp").forward(request, response); 
		}
		return;
			
	}
}
