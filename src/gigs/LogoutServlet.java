package gigs;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		System.out.println("0: logout");
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("/welcome");
		return;
	}
}
