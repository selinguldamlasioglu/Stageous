package gigs.db.bigquery;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.appengine.api.users.UserServiceFactory;

public class BigQueryWebServerAuthCallBack extends AbstractAppEngineAuthorizationCodeCallbackServlet {

	  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
	      throws ServletException, IOException {
	    resp.sendRedirect("/");
	  }

	  protected void onError(
	      HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
	      throws ServletException, IOException {
	    String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
	    resp.getWriter().print("<p>" + nickname + ", you've declined to authorize this application.</p>");
	    resp.getWriter().print("<p><a href=\"/\">Visit this page</a> to try again.</p>");
	    resp.setStatus(200);
	    resp.addHeader("Content-Type", "text/html");
	  }

	  @Override
	  protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
	    return CredentialUtils.newFlow();
	  }

	  @Override
	  protected String getRedirectUri(HttpServletRequest request) throws ServletException, IOException {
	    return CredentialUtils.getRedirectUri(request);
	  }

	}


