package gigs.engine.event.user;

import java.io.IOException;
import java.io.Serializable;

import org.xml.sax.SAXException;

import gigs.engine.auth.GeoCoder;

public class GeoLocation implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8895798242107899478L;
	public double latitude;
	public double longitude;
	public String city;
	public String country;
	
	public GeoLocation(){
		latitude = -1;
		longitude = -1;
		this.city = "";
		this.country = "";
	}
	
	public GeoLocation(double latitude, double longtitude) throws IOException, SAXException{
		this.latitude = latitude;
		this.longitude = longtitude;
		GeoLocation rev = GeoCoder.reverseGeocode(latitude, longtitude);
		this.city = rev.city;
		this.country = rev.country;
		System.out.println(this.toString());
	}
	
	public GeoLocation(String place) throws IOException{
		GeoLocation rev = GeoCoder.geocode(place);
		this.latitude = rev.latitude;
		this.longitude = rev.longitude;
		this.city = rev.city;
		this.country = rev.country;
		System.out.println(this.toString());
	}
	
	public GeoLocation(double latitude, double longtitude, String city, String country){
		this.latitude = latitude;
		this.longitude = longtitude;
		if(city.length()<5 || country.length()<5)
		{
			GeoLocation rev = GeoCoder.reverseGeocode(latitude, longitude);
			this.city = rev.city;
			this.country = rev.country;
		}
		else
		{
			this.city = city;
			this.country = country;
		}
		System.out.println(this.toString());
	}
	
	public GeoLocation(double latitude, double longtitude, String city, String country, boolean bool){
		this.latitude = latitude;
		this.longitude = longtitude;
		
		this.city = city;
		this.country = country;
		System.out.println(this.toString());
	}
	
	public String toString()
	{
		return this.city + " " + this.country + " " + this.latitude + " " + this.longitude  + " ";
	}
}