package gigs.db.bigquery;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.ProjectList;
import com.google.api.services.bigquery.model.ProjectList.Projects;

public class BigQueryWebServerAuth extends AbstractAppEngineAuthorizationCodeServlet {

	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException {
	    response.setContentType("text/html");
	    PrintWriter writer = response.getWriter();

	    Bigquery bigquery = CredentialUtils.loadbigquery();

	    Bigquery.Projects.List projectListRequest = bigquery.projects().list();
	    ProjectList projectList = projectListRequest.execute();

	    if (projectList.getProjects() != null) {

	      List<Projects> projects = projectList.getProjects();
	      writer.println("<h3>BigQuery project list:</h3>");

	      for (ProjectList.Projects project : projects) {
	        writer.printf("%s<br />", project.getProjectReference().getProjectId());
	      }

	    }
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
