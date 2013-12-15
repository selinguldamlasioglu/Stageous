package gigs.engine.auth;

import gigs.engine.event.user.GeoLocation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import google.maps.*;

public class GeoCoder {
	
	private static String host = "maps.googleapis.com";

	
	public static GeoLocation reverseGeocode(double lat, double longt)
	{
		String latLong = lat + "," + longt;
		String city = "";
		String country = "";
		
		try{
			
		URL mapsUrl = new URL( "http://maps.googleapis.com/maps/api/geocode/xml?latlng=" + latLong + "&sensor=false");
		//System.out.println(mapsUrl);
		InputStream openStream = mapsUrl.openStream();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = db.parse(openStream);
		
		//System.out.println(doc.getFirstChild().getTextContent());
		
//		String check = doc.getElementsByTagName("status").item(0).getTextContent();
//		System.out.println("Checking : " + check + "-eof");
//		boolean status = (check.equals("OK"));
//		System.out.println("Hi STATUS: " + status);
		if(doc.getElementsByTagName("status").item(0).getTextContent().equals("OK"))
		{
			NodeList address = doc.getElementsByTagName("address_component");
			for(int i =0; i< address.getLength(); i++)
			{
				Element add = (Element)address.item(i);
				
				//System.out.println("Hi: " + add.getElementsByTagName("type").item(0).getTextContent());
				
				if(add.getElementsByTagName("type").item(0).getTextContent().equals("locality"))
					city = add.getElementsByTagName("short_name").item(0).getTextContent();
				if(add.getElementsByTagName("type").item(0).getTextContent().equals("country"))
					country = add.getElementsByTagName("long_name").item(0).getTextContent();
			}
		}
		else
			return new GeoLocation(0,0,"","", false);
		} catch (Exception e) {e.printStackTrace(); }
		return new GeoLocation(lat,longt,city,country, false);
	}
	
	public static GeoLocation geocode(String address) throws IOException
	{
		address = address.replaceAll(" ", "+");
		URL mapsUrl = new URL(
                "http://maps.googleapis.com/maps/api/geocode/xml?address="
                                + address + "&sensor=false");
		
		//System.out.println(mapsUrl);
		InputStream openStream = mapsUrl.openStream();
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
			doc = db.parse(openStream);
		} catch (SAXException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		String city = "";
		String country = "";
		double lat, longi = 0;
		
		//System.out.println("Hi: " + doc.getFirstChild().getTextContent());
		
		//String check = doc.getElementsByTagName("status").item(0).getTextContent();
		//System.out.println("Checking : " + check + "-eof");
		//boolean status = (check.equals("OK"));
		//System.out.println("Hi STATUS: " + status);
		
		if(doc.getElementsByTagName("status").item(0).getTextContent().equals("OK"))
		{
			NodeList addressi = doc.getElementsByTagName("address_component");
			for(int i =0; i< addressi.getLength(); i++)
			{
				Element add = (Element)addressi.item(i);	
				
				//System.out.println(add.getElementsByTagName("type").item(0).getTextContent());
				
				if(add.getElementsByTagName("type").item(0).getTextContent().equals("locality"))
					city = add.getElementsByTagName("short_name").item(0).getTextContent();
				if(add.getElementsByTagName("type").item(0).getTextContent().equals("country"))
					country = add.getElementsByTagName("long_name").item(0).getTextContent();
			}
			
			Element loc = (Element)doc.getElementsByTagName("location").item(0);
			lat = Double.parseDouble(loc.getElementsByTagName("lat").item(0).getTextContent());
			longi = Double.parseDouble(loc.getElementsByTagName("lng").item(0).getTextContent());
		}
		else
			return new GeoLocation(0,0,"","");
		
		return new GeoLocation(lat,longi,city,country);
	}
}
