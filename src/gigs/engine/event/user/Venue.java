package gigs.engine.event.user;

import java.io.Serializable;
import java.sql.SQLException;

public class Venue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7003909912122794216L;
	public int id;
	public String name;
	public String city;
	public String country;
	public String street;
	public GeoLocation geo;
	
	public Venue() throws SQLException
	{
		this.id = 0;
		this.name = "";
		this.city = "";
		this.country = "";
		this.street = "";
		this.geo = new GeoLocation();		
	}
	
	public Venue(int id, String name, String city, String country, String street, GeoLocation geo) throws SQLException
	{
		this.id = id;
		this.name = name;
		this.city = city;
		this.country = country;
		this.street = street;
		this.geo = geo;
	}
	
//	public String toString(){
//		return id + " " + name + " " + city + " " + country + " " + street + " " + geo;
//	}
	
	public String toString()
	{
		String address = "";
		if(name != null) address+=name + "<br />";
		if(street != null) address+=street + "<br />";
		if(city != null) address+=city + ", ";
		if(country != null) address+=country+ "<br />";
		
		return address;
	}
	
}
