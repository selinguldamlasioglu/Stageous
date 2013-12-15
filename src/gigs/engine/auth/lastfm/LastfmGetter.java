package gigs.engine.auth.lastfm;

//import gigs.db.bigquery.*;
import gigs.engine.event.user.GeoLocation;
import gigs.engine.event.user.Tag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

// This class is a stub.
public class LastfmGetter {
	private static String APIkey = "618956c0af51123a9b7e789b2773d066";
	private static String host = "ws.audioscrobbler.com";
	private static String projectID = "817643348209817643348209";

//	Socket sock;
//	BufferedWriter writer;
//	BufferedReader reader;
	
	// Make this object singleton in SERVER RUNTIME to avoid too many requests
	// TODO IMPLEMENT: @max: 5 requests per seconds, ~1500 requests per 5 minutes
	
	public LastfmGetter() throws ConnectException, IOException
	{
//		sock= new Socket(host,80);
//		writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
//		reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	// Find a simpler way implementing XML get stream.
	public Document getEventInfoXML(int eventid, int active) throws IOException, InterruptedException
	{
		String uri = "http://" + host + "/2.0/?method=event.getinfo&event=" + eventid + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = db.parse(xml);
		} catch (SAXException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO : OPEN THIS SHIT
		//saveInCache(eventid, doc);
		return doc;
	}
	
	public Document getWeightedTags() throws Exception{
		  String uri = "http://" + host + "/2.0/?method=tag.getTopTags&api_key=" + APIkey;
		  uri = uri.replaceAll(" ", "%20");
		  System.out.println(uri);
		  URL url = new URL(uri);
		  HttpURLConnection connection =
		      (HttpURLConnection) url.openConnection();
		  connection.setRequestMethod("GET");
		//  connection.setRequestProperty("Accept", "application/xml");

		  InputStream xml = connection.getInputStream();

		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(xml);
		  
		  return doc;
	}
	
	public Document getArtistTagsXML(String artist) throws Exception
	{
		artist = artist.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=artist.getTags&artist=" + artist + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getArtistEventsXML(String artist) throws Exception
	{
		artist = artist.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=artist.getEvents&artist=" + artist + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getArtistEventsXMLPage(String artist, int page) throws Exception
	{
		artist = artist.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=artist.getEvents&artist=" + artist + "&page=" + page + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getVenueEventsXML(int vid) throws Exception
	{
		//venue = venue.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=venue.getEvents&venue=" + vid + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoEventsXML(GeoLocation geo) throws Exception
	{
		//venue = venue.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + geo.city +"&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoEventsXMLPage(GeoLocation geo, int page) throws Exception
	{
		//venue = venue.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + geo.city + "&page=" + page + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoEventsXML(GeoLocation geo, int radius) throws Exception
	{
		//venue = venue.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + geo.city + "&distance=" + radius  +"&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		//System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoTagEventsXML(GeoLocation geo, Tag tag) throws Exception
	{
		String city = geo.city.replaceAll(" ", "+");
		String tagst = tag.name.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + city + "&tag=" + tagst  +"&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoTagEventsXMLPage(GeoLocation geo, Tag tag, int page) throws Exception
	{
		String city = geo.city.replaceAll(" ", "+");
		String tagst = tag.name.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + city + "&tag=" + tagst + "&page=" + page  +"&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoTagEventsXML(GeoLocation geo, Tag tag, int distance) throws Exception
	{
		String city = geo.city.replaceAll(" ", "+");
		String tagst = tag.name.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + city + "&tag=" + tagst  +"&distance=" + distance + "&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	public Document getGeoTagEventsXMLPage(GeoLocation geo, Tag tag, int distance, int page) throws Exception
	{
		String city = geo.city.replaceAll(" ", "+");
		String tagst = tag.name.replaceAll(" ", "+");
		String uri = "http://" + host + "/2.0/?method=geo.getEvents&location=" + city + "&tag=" + tagst +"&distance=" + distance + "&page=" + page  +"&api_key=" + APIkey;
		uri = uri.replaceAll(" ", "%20");
		System.out.println(uri);
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		
		return doc;
	}
	
	
/*	public JobReference saveInCache(int id, Document d) throws IOException, InterruptedException
	{	
		Bigquery bq = CredentialUtils.loadbigquery();
		
		if(! (this.checkInCache(bq, id)) )
		{ 
			String statement = "INSERT INTO EventCache (id, data) VALUES (" + id + ", " + d + " )";
			JobReference ref = BigQueryWrapper.startQuery(bq, projectID, statement);
			return ref;
		}
		return null;	
	}
	
	public JobReference saveInCache(int id, String s) throws IOException, InterruptedException
	{	
		Bigquery bq = CredentialUtils.loadbigquery();
		
		if(! (this.checkInCache(bq, id)) )
		{ 
			String statement = "INSERT INTO EventCache (id, data) VALUES (" + id + ", " + s + " )";
			JobReference ref = BigQueryWrapper.startQuery(bq, projectID, statement);
			return ref;
		}
		return null;	
	}
	
	public boolean checkInCache(Bigquery bq, int id) throws IOException, InterruptedException 
	{	
		String statement = "SELECT COUNT(*) AS count from EventCache WHERE id =" + id + "";
		JobReference ref = BigQueryWrapper.startQuery(bq, projectID, statement);
		Job j = bq.jobs().get(projectID, ref.getJobId()).execute();
		
		GetQueryResultsResponse queryResult = bq.jobs().getQueryResults( projectID, j.getJobReference().getJobId()).execute();
		
		queryResult.getJobComplete();
		
		List<TableRow> rows = queryResult.getRows();

	    int result = Integer.parseInt((String) rows.get(0).getF().get(0).getV());
	 
	    if(result>0)
	    	return true;
		return false;
	}
*/


}
