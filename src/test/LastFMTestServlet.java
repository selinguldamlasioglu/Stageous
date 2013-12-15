package test;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.URL;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import gigs.engine.auth.*;
import gigs.engine.auth.lastfm.LastfmGetter;
import gigs.engine.event.rec.XMLClipper;
import gigs.engine.event.user.Event;

import org.apache.tools.ant.taskdefs.condition.Http;
import org.w3c.dom.Document;

@SuppressWarnings("all")
public class LastFMTestServlet extends HttpServlet {
	//TODO: Use LastfmGetter.java for event calls and save into cache by using BigQueryWrapper.java
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ConnectException, IOException{
		
		PrintWriter out = resp.getWriter();
		out.println("hi-0");
		
		LastfmGetter getter;
		Document eventXML = null;
		InputStream XML;
		XMLClipper clip = null;

		getter = new LastfmGetter();
		try {
//			5 + (int)(Math.random() * ((10 - 5) + 1))
			eventXML = getter.getEventInfoXML(100000 + (int) (Math.random() * (3565222-100000) + 1), 0);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			clip = new XMLClipper(eventXML);
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(out);
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(out);
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(out);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(out);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Event e = null;
		try {
			e = clip.XML2Event();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = null;
			try {
				transformer = tf.newTransformer();
			} catch (TransformerConfigurationException e1) {
				// TODO Auto-generated catch block
				out.println("TransformerConfigurationException 2");
				e1.printStackTrace(out);
			}
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			try {
				transformer.transform(new DOMSource(eventXML), new StreamResult(writer));
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				out.println("TransformerException 3");
				e1.printStackTrace(out);
				
			}
			String output = writer.getBuffer().toString();

//			out.println(eventXML.getFirstChild().getTextContent());
				
			out.println(e.toString());
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			out.println("TransformerConfigurationException 1");
			e1.printStackTrace(out);
			
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			out.println("TransformerException 1");
			e1.printStackTrace(out);
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			out.println("TransformerFactoryConfigurationError 1");
			e1.printStackTrace(out);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			out.println("ParserConfigurationException 1");
			e1.printStackTrace(out);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			out.println("Exception 1: ");
			e1.printStackTrace(out);
		}
			
		

		
//		Document eventXML = null;
//		try {
//			eventXML = getter.getEventXML(3377287,0);
//		}
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		final String a = eventXML.toString();
//		resp.setContentType("text/html");
//		resp.getWriter().println("Hello, world - Lastfm Test");
		
		
		
	

		
	}

}
