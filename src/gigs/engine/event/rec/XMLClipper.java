package gigs.engine.event.rec;

import gigs.db.cloudsql.EventWrapper;
import gigs.engine.auth.lastfm.LastfmGetter;
import gigs.engine.event.user.*; 

import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.lang.Math;

//@SuppressWarnings("all")
public class XMLClipper {

	Document doc;
	EventWrapper ewrap;
	
	public XMLClipper (Document doc) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, InvalidPropertiesFormatException, IOException, ParserConfigurationException, SQLException
	{
		this.doc = doc;
		this.ewrap = new EventWrapper();
		  //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      //DocumentBuilder docBuilder = factory.newDocumentBuilder();
	      //File file = new File("bookStore.xml");
	      //Document doc = docBuilder.parse(file);
	}
	
	public XMLClipper (String filename) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, InvalidPropertiesFormatException, IOException, ParserConfigurationException, SAXException, SQLException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = factory.newDocumentBuilder();
	    File file = new File(filename);
	    Document doc = docBuilder.parse(file);
	    doc.getDocumentElement().normalize();
		this.ewrap = new EventWrapper();
	    this.doc = doc;
	}

//	<event>
//	  <id>640418</id>
//	  <title>Nikka Costa</title>
//	  <artists>
//	    <artist>Nikka Costa</artist>
//	    <headliner>Nikka Costa</headliner>
//	  </artists>
//	  <venue>
//	    <name>Bowery Ballroom</name>
//	    <location>
//	      <city>New York</city>
//	      <country>United States</country>
//	      <street>6 Delancey</street>
//	      <postalcode>10002</postalcode>
//	      <geo:point>
//	         <geo:lat>40.71417</geo:lat>
//	         <geo:long>-74.00639</geo:long>
//	      </geo:point>
//	      <timezone>EST</timezone>
//	    </location>
//	    <url>http://www.last.fm/venue/8779095</url>
//	  </venue>
//	  <startDate>Mon, 30 Jun 2008</startDate>
//	  <startTime>20:00</startTime>
//	  <description><![CDATA[Doors 8pm<br />
//	$20<br />
//	18+]]></description>
//	  <image size="small">...</image>
//	  <image size="medium">...</image>
//	  <image size="large">...</image>
//	  <attendance>42</attendance>
//	  <reviews>0</reviews>
//	  <tag>lastfm:event=640418</</tag>
//	  <url>http://www.last.fm/event/640418</url>
//	  <website>http://...</website>
//	  <tickets>
//	    <ticket supplier="...">http://...</ticket>
//	    ...
//	  </tickets>
//	</event>
	
	public Event XML2Event() throws TransformerConfigurationException, InvalidPropertiesFormatException, TransformerException, TransformerFactoryConfigurationError, IOException, ParserConfigurationException, Exception
	{
		NodeList events = doc.getElementsByTagName("event");
		Element eventel = (Element)events.item(0);
		
		String title = eventel.getElementsByTagName("title").item(0).getTextContent();
		String temp = eventel.getElementsByTagName("id").item(0).getTextContent();
		
		int id;
		if (temp!="")
			id = Integer.parseInt(temp);
		else
			id = 0;
		
		NodeList artistss = eventel.getElementsByTagName("artists");
		ArrayList<String> artists = new ArrayList<String>();
		ArrayList<Tag> tag = new ArrayList<Tag>();
		for(int i =0; i< artistss.getLength(); i++)
		{
			Element artistsel = (Element)artistss.item(i);
			String tempart = artistsel.getElementsByTagName("artist").item(0).getTextContent();
			artists.add(tempart.replaceAll("'", ""));
			//TODO - THREAD THIS ! USING APPENGINE THREAD FACTORY
			XMLClipper clip = new XMLClipper(new LastfmGetter().getArtistTagsXML(tempart));
			tag.addAll(clip.XML2TagList());
		}
		
		//String headliner = eventel.getElementsByTagName("headliner").item(0).getTextContent();
		
		NodeList venues = eventel.getElementsByTagName("venue");
		Element venueel = (Element) venues.item(0);
		NodeList locs = venueel.getElementsByTagName("location");
		String vid = venueel.getElementsByTagName("id").item(0).getTextContent();
		Element locel = (Element)locs.item(0);
		
		NodeList points = locel.getElementsByTagName("geo:point");
		Element pointel = (Element) points.item(0);
		String temp1 = pointel.getElementsByTagName("geo:lat").item(0).getTextContent();
		
		double lat, longe;
		
		if (temp1!="")
			lat = Double.parseDouble(temp1);
		else
			lat = 0;
		
		String temp2 = pointel.getElementsByTagName("geo:long").item(0).getTextContent();
		if (temp2!="")
			longe = Double.parseDouble(temp2);
		else
			longe = 0;
		
		Venue v = new Venue(Integer.parseInt(vid), venueel.getElementsByTagName("name").item(0).getTextContent(), venueel.getElementsByTagName("city").item(0).getTextContent(), 
				venueel.getElementsByTagName("country").item(0).getTextContent(), venueel.getElementsByTagName("street").item(0).getTextContent(),
				new GeoLocation(lat, longe));		
		
		System.out.println("Venue: " + v.toString());
		
		//NodeList venues = doc.getElementsByTagName("venue");
		//Element venueel = (Element) venues.get(0);
		
		System.out.println("id" + id);
		System.out.println("title" + title);
		Timestamp t = (new EventTime(eventel.getElementsByTagName("startDate").item(0).getTextContent()).getTimestamp());
		System.out.println("time: " + t.toString());
		String desc = eventel.getElementsByTagName("description").item(0).getTextContent();
		System.out.println("desc:---" + desc.length() + "---");
		if(desc.length() < 10)
			desc = "Not available.";
		System.out.println("desc:---" + desc + "---");
		
		// TODO - get largest image or get thumbnails etc
		// TODO - change placeholder (https://dl.dropbox.com/u/4930607/placeholder.jpg)
		String imageURL = "";
		for(int i = 0; i<eventel.getElementsByTagName("image").getLength(); i++)
			imageURL = eventel.getElementsByTagName("image").item(i).getTextContent();
		
		if(imageURL.equals("") || imageURL.equals(" ") ||  imageURL.equals("  ") ||  imageURL.equals("\n ")  ||  imageURL.equals("\n  ") )
			imageURL = "https://dl.dropbox.com/u/4930607/placeholder.jpg";
		
		System.out.println("image: " + imageURL);
		String ticketURL = eventel.getElementsByTagName("tickets").item(0).getTextContent();
		
		if(ticketURL.equals("") || ticketURL.equals(" ") ||  ticketURL.equals("  ") ||  ticketURL.equals("\n ")  ||  ticketURL.equals("\n  ") )
			ticketURL = eventel.getElementsByTagName("website").item(0).getTextContent();
		
		// TODO - Hic link yoksa, Event sayfasina yonlendir
		if(ticketURL.equals("") || ticketURL.equals(" ") ||  ticketURL.equals("  ") ||  ticketURL.equals("\n ")  ||  ticketURL.equals("\n  ") )
			ticketURL = "http://www.stageous.com";
		
//		ticketURL =  ticketURL.replaceAll("\'", "");
		System.out.println("ticket: \'" + ticketURL + "\'");
		
		Event e = new Event (id, title, t, artists, tag, desc, new EventImage(new URL(imageURL), EventImageType.EVENTFMIMAGE, id), ticketURL, v.id );
		ewrap.addVenue(v);
		ewrap.addEvent(e);
		ewrap.addEventTags(e.eventID, tag);
		ewrap.addEventArtists(e.eventID, artists);
		return e;
	}
	
	public ArrayList<Event> XML2Events() throws TransformerConfigurationException, InvalidPropertiesFormatException, TransformerException, TransformerFactoryConfigurationError, IOException, ParserConfigurationException, Exception
	{
		ArrayList<Event> result = new ArrayList<Event>();
		NodeList events = doc.getElementsByTagName("event");
		System.out.println("Events size = " + events.getLength());
		for(int j =0; j< events.getLength(); j++)
		{
			Element eventel = (Element)events.item(j);
			
			String title = eventel.getElementsByTagName("title").item(0).getTextContent();
			String temp = eventel.getElementsByTagName("id").item(0).getTextContent();
			
			int id;
			if (temp!="")
				id = Integer.parseInt(temp);
			else
				id = 0;
			
			NodeList artistss = eventel.getElementsByTagName("artists");
			ArrayList<String> artists = new ArrayList<String>();
			ArrayList<Tag> tag = new ArrayList<Tag>();
			for(int i =0; i< artistss.getLength(); i++)
			{
				Element artistsel = (Element)artistss.item(i);
				String tempart = artistsel.getElementsByTagName("artist").item(0).getTextContent();
				artists.add(tempart.replaceAll("'", ""));
				//TODO - THREAD THIS ! USING APPENGINE THREAD FACTORY
				XMLClipper clip = new XMLClipper(new LastfmGetter().getArtistTagsXML(tempart));
				tag.addAll(clip.XML2TagList());
			}
			
			//String headliner = eventel.getElementsByTagName("headliner").item(0).getTextContent();
			
			NodeList venues = eventel.getElementsByTagName("venue");
			Element venueel = (Element) venues.item(0);
			NodeList locs = venueel.getElementsByTagName("location");
			String vid = venueel.getElementsByTagName("id").item(0).getTextContent();
			Element locel = (Element)locs.item(0);
			
			NodeList points = locel.getElementsByTagName("geo:point");
			Element pointel = (Element) points.item(0);
			String temp1 = pointel.getElementsByTagName("geo:lat").item(0).getTextContent();
			
			double lat, longe;
			
			if (temp1!="")
				lat = Double.parseDouble(temp1);
			else
				lat = 0;
			
			String temp2 = pointel.getElementsByTagName("geo:long").item(0).getTextContent();
			if (temp2!="")
				longe = Double.parseDouble(temp2);
			else
				longe = 0;
			
			Venue v = new Venue(Integer.parseInt(vid), venueel.getElementsByTagName("name").item(0).getTextContent(), venueel.getElementsByTagName("city").item(0).getTextContent(), 
					venueel.getElementsByTagName("country").item(0).getTextContent(), venueel.getElementsByTagName("street").item(0).getTextContent(),
					new GeoLocation(lat, longe));		
			
			//System.out.println("Venue: " + v.toString());
			
			//NodeList venues = doc.getElementsByTagName("venue");
			//Element venueel = (Element) venues.get(0);
			
			//System.out.println("id" + id);
			//System.out.println("title" + title);
			EventTime et = new EventTime(eventel.getElementsByTagName("startDate").item(0).getTextContent());
			Timestamp t = et.getTimestamp();
			
			System.out.println("time: " + t.toString());
			String desc = eventel.getElementsByTagName("description").item(0).getTextContent();
			//System.out.println("desc:---" + desc.length() + "---");
			if(desc.length() < 10)
				desc = "Not available.";
			//System.out.println("desc:---" + desc + "---");
			
			// TODO - get largest image or get thumbnails etc
			// TODO - change placeholder (https://dl.dropbox.com/u/4930607/placeholder.jpg)
			
			String imageURL = "";
			for(int i = 0; i<eventel.getElementsByTagName("image").getLength(); i++)
				imageURL = eventel.getElementsByTagName("image").item(i).getTextContent();
			
			if(imageURL.equals("") || imageURL.equals(" ") ||  imageURL.equals("  ") ||  imageURL.equals("\n ")  ||  imageURL.equals("\n  ") )
				imageURL = "https://dl.dropbox.com/u/4930607/placeholder.jpg";
			
			//System.out.println("image: " + imageURL);
			String ticketURL = eventel.getElementsByTagName("tickets").item(0).getTextContent();
			
			if(ticketURL.equals("") || ticketURL.equals(" ") ||  ticketURL.equals("  ") ||  ticketURL.equals("\n ")  ||  ticketURL.equals("\n  ") )
				ticketURL = eventel.getElementsByTagName("website").item(0).getTextContent();
			
			// TODO - Hic link yoksa, Event sayfasina yonlendir
			if(ticketURL.equals("") || ticketURL.equals(" ") ||  ticketURL.equals("  ") ||  ticketURL.equals("\n ")  ||  ticketURL.equals("\n  ") )
				ticketURL = "http://www.stageous.com";
			
//			ticketURL =  ticketURL.replaceAll("\'", "");
			//System.out.println("ticket: \'" + ticketURL + "\'");
			Event e = new Event (id, title, t, artists, tag, desc, new EventImage(new URL(imageURL), EventImageType.EVENTFMIMAGE, id),  ticketURL, v.id ) ;
			ewrap.addVenue(v);
			ewrap.addEvent(e);
			ewrap.addEventTags(e.eventID, tag);
			ewrap.addEventArtists(e.eventID, artists);
			result.add(e);
		}
		return result;
	}
	
/*	public Event XML2Event(PrintWriter out) throws TransformerConfigurationException, InvalidPropertiesFormatException, TransformerException, TransformerFactoryConfigurationError, IOException, ParserConfigurationException, Exception
	{
		NodeList events = doc.getElementsByTagName("event");
		Element eventel = (Element)events.item(0);
		
		String title = eventel.getElementsByTagName("title").item(0).getTextContent();
		String temp = eventel.getElementsByTagName("id").item(0).getTextContent();
		
		int id;
		if (temp!="")
			id = Integer.parseInt(temp);
		else
			id = 0;
		
		NodeList artistss = eventel.getElementsByTagName("artists");
		ArrayList<String> artists = new ArrayList<String>();
		ArrayList<String> tag = new ArrayList<String>();
		for(int i =0; i< artistss.getLength(); i++)
		{
			Element artistsel = (Element)artistss.item(i);
			String tempart = artistsel.getElementsByTagName("artist").item(0).getTextContent();
			out.println("\n\n"+ tempart + "\n---------------");
			artists.add(tempart.replaceAll("'", ""));
			//THREAD THIS ! USING APPENGINE THREAD FACTORY
			XMLClipper clip = new XMLClipper(new LastfmGetter().getArtistTagsXML(tempart));
			tag = clip.XML2Tags(tag);
		}
		
		String headliner = eventel.getElementsByTagName("headliner").item(0).getTextContent();
		
		NodeList venues = eventel.getElementsByTagName("venue");
		Element venueel = (Element) venues.item(0);
		NodeList locs = eventel.getElementsByTagName("location");
		Element locel = (Element)locs.item(0);
		
		NodeList points = eventel.getElementsByTagName("geo:point");
		Element pointel = (Element) points.item(0);
		String temp1 = pointel.getElementsByTagName("geo:lat").item(0).getTextContent();
		
		double lat, longe;
		
		if (temp1!="")
			lat = Double.parseDouble(temp);
		else
			lat = 0;
		
		String temp2 = pointel.getElementsByTagName("geo:long").item(0).getTextContent();
		if (temp!="")
			longe = Double.parseDouble(temp);
		else
			longe = 0;
		
		Venue v = new Venue(venueel.getElementsByTagName("name").item(0).getTextContent(), venueel.getElementsByTagName("city").item(0).getTextContent(), 
				venueel.getElementsByTagName("country").item(0).getTextContent(), venueel.getElementsByTagName("street").item(0).getTextContent(),
				new GeoLocation(lat, longe));		
		
		//NodeList venues = doc.getElementsByTagName("venue");
		//Element venueel = (Element) venues.get(0);
		
		out.println("id" + id);
		Timestamp t = (new EventTime(eventel.getElementsByTagName("startDate").item(0).getTextContent()).getTimestamp());
		out.println("time: " + t.toString());
		String desc = eventel.getElementsByTagName("description").item(0).getTextContent();
		out.println("desc" + desc);
		//get largest image or get thumbnails etc
		String imageURL = eventel.getElementsByTagName("image").item(0).getTextContent();
		//out.println("image: " + imageURL);
		String ticketURL = eventel.getElementsByTagName("tickets").item(0).getTextContent();
		//out.println("ticket: " + ticketURL);
		
		return new Event (id, t, tag, desc, new EventImage(new URL(imageURL)), headliner, artists, ticketURL, v );
	}
*/	

	public ArrayList<String> XML2NewTagList()
	{
		ArrayList<String> taglist = new ArrayList<String>();
		NodeList tags = doc.getElementsByTagName("tag");
	    
	    for (int i = 0; i < tags.getLength(); i++) {
	         Element tagel = (Element)tags.item(i);
	         String tagname = tagel.getElementsByTagName("name").item(0).getTextContent();
	         taglist.add(tagname);
	    }
	    return taglist;
	}
	
	public ArrayList<Tag> XML2WeighedTagList()
	{
		ArrayList<Tag> tag = new ArrayList<Tag>();
		NodeList tags = doc.getElementsByTagName("tag");
	    
	    for (int i = 0; i < tags.getLength(); i++) {
	         Element tagel = (Element)tags.item(i);
	         String tagname = tagel.getElementsByTagName("name").item(0).getTextContent();
	         int tagweight = Integer.parseInt(tagel.getElementsByTagName("count").item(0).getTextContent());
	         tag.add(new Tag(tagname));
	         tag.get(i).interest = tagweight/100000;
	    }
	    
	    Collections.shuffle(tag);
	    return tag;
	}
	
	public ArrayList<Tag> XML2TagList()
	{
		ArrayList<Tag> tag = new ArrayList<Tag>();
		NodeList tags = doc.getElementsByTagName("tag");
	    
	    for (int i = 0; i < tags.getLength(); i++) {
	         Element tagel = (Element)tags.item(i);
	         String tagname = tagel.getElementsByTagName("name").item(0).getTextContent();
	         tag.add(new Tag(tagname));
	    }
	    return tag;
	}
	
	public ArrayList<Tag> XML2WeighedTagList(int max) {
		NodeList tags = doc.getElementsByTagName("tag");
	    if(max>tags.getLength()) max = tags.getLength();
	    ArrayList<Tag> tag = new ArrayList<Tag>();
	    
	    for (int i = 0; i < max; i++) {
	         Element tagel = (Element)tags.item(i);
	         String tagname = tagel.getElementsByTagName("name").item(0).getTextContent();
	         double tagweight = Double.parseDouble(tagel.getElementsByTagName("count").item(0).getTextContent());
	         tag.add(new Tag(tagname));
	         tagweight = tagweight/2000000;
	         tag.get(i).interest = (int) (10*Math.sin(tagweight)*Math.sin(tagweight));
	    }
	    Collections.shuffle(tag);
	    return tag;  
	}
	
	public int getPageCount()
	{
		NodeList nodes = doc.getElementsByTagName("events");
		Element el = (Element) nodes.item(0);
		return Integer.parseInt(el.getAttribute("totalPages"));
	}
	
	public int getResultCount()
	{
		NodeList nodes = doc.getElementsByTagName("events");
		Element el = (Element) nodes.item(0);
		if(el!=null)
			return Integer.parseInt(el.getAttribute("total"));
		return -1;
	}

	public int getResultsPerPage()
	{
		NodeList nodes = doc.getElementsByTagName("events");
		Element el = (Element) nodes.item(0);
		if(el!=null)
			return Integer.parseInt(el.getAttribute("perPage"));
		return -1;
	}
	
	public EventImage XML2ClipEventImage() throws TransformerConfigurationException, InvalidPropertiesFormatException, TransformerException, TransformerFactoryConfigurationError, IOException, ParserConfigurationException, Exception
	{
		NodeList events = doc.getElementsByTagName("event");
		Element eventel = (Element)events.item(0);
		
		String temp = eventel.getElementsByTagName("id").item(0).getTextContent();
		
		int id;
		if (temp!="")
			id = Integer.parseInt(temp);
		else
			id = 0;
		
		// TODO - get largest image or get thumbnails etc
		// TODO - change placeholder (https://dl.dropbox.com/u/4930607/placeholder.jpg)
		String imageURL = "";
		for(int i = 0; i<eventel.getElementsByTagName("image").getLength(); i++)
			imageURL = eventel.getElementsByTagName("image").item(i).getTextContent();
		if(imageURL.equals("") || imageURL.equals(" ") ||  imageURL.equals("  ") ||  imageURL.equals("\n ")  ||  imageURL.equals("\n  ") )
			imageURL = "https://dl.dropbox.com/u/4930607/placeholder.jpg";
		
		System.out.println("image: " + imageURL);
		String ticketURL = eventel.getElementsByTagName("tickets").item(0).getTextContent();
		
		if(ticketURL.equals("") || ticketURL.equals(" ") ||  ticketURL.equals("  ") ||  ticketURL.equals("\n ")  ||  ticketURL.equals("\n  ") )
			ticketURL = eventel.getElementsByTagName("website").item(0).getTextContent();
		
		// TODO - Hic link yoksa, Event sayfasina yonlendir
		if(ticketURL.equals("") || ticketURL.equals(" ") ||  ticketURL.equals("  ") ||  ticketURL.equals("\n ")  ||  ticketURL.equals("\n  ") )
			ticketURL = "http://www.stageous.com";
		
//		ticketURL =  ticketURL.replaceAll("\'", "");
		System.out.println("ticket: \'" + ticketURL + "\'");
		
//		Event e = new Event (id, title, t, artists, tag, desc, new EventImage(new URL(imageURL), EventImageType.EVENTFMIMAGE, id), ticketURL, v.id );
//		ewrap.addVenue(v);
//		ewrap.addEvent(e);
//		ewrap.addEventTags(e.eventID, tag);
//		ewrap.addEventArtists(e.eventID, artists);
		return new EventImage(new URL(imageURL), EventImageType.EVENTFMIMAGE, id);
	}
	
}
