package gigs.engine.event.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.StringTokenizer;

public class EventTime implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3506448773483492146L;
	Timestamp time;
	String sdate;
	
	public EventTime(Timestamp t)
	{
		this.time = t;
	}
	
//	  <startDate>Mon, 30 Jun 2008</startDate>
//	  <startTime>20:00</startTime>
// 	  <startDate>Sat, 02 Mar 2013 22:00:00</startDate>
	public EventTime(String startDate)
	{
		sdate = startDate;
		this.time = this.toTimestamp();
	}
	
	public Timestamp getTimestamp()
	{
		return time;
	}
	
	public Timestamp toTimestamp()
	{
		String day = "00";
		String month = "";
		String imonth = "00";
		String year = "0000";
		String hour = "00";
		String min = "00";
		
		
		
		if(sdate!=null)
		{
			StringTokenizer tokens = new StringTokenizer(sdate, ",:. ");
			
			if(tokens.hasMoreTokens())
			{
				tokens.nextToken();
				day = tokens.nextToken();
				while(day.length()<2) day = "0" + day;
			}
			if(tokens.hasMoreTokens())
			{
				month = tokens.nextToken();
				if(month.equals("Jan")){imonth="01";}
				if(month.equals("Feb")){imonth="02";}
				if(month.equals("Mar")){imonth="03";}
				if(month.equals("Apr")){imonth="04";}
				if(month.equals("May")){imonth="05";}
				if(month.equals("Jun")){imonth="06";}
				if(month.equals("Jul")){imonth="07";}
				if(month.equals("Aug")){imonth="08";}
				if(month.equals("Sep")){imonth="09";}
				if(month.equals("Oct")){imonth="10";}
				if(month.equals("Nov")){imonth="11";}
				if(month.equals("Dec")){imonth="12";}
			}
			if(tokens.hasMoreTokens())
			{
				year = tokens.nextToken();
			}
			if(tokens.hasMoreTokens())
			{
				hour = tokens.nextToken();
			}
			if(tokens.hasMoreTokens())
			{
				min = tokens.nextToken();
			}
		}	

		time = Timestamp.valueOf(year+"-" + imonth + "-"+ day + " " + hour + ":" + min + ":00");
		System.out.println("Timestamp : " + time.toGMTString());
		return time;
	}
	
	public String toString()
	{
		String temp;
		
		try{
			temp = time.toString().split(".")[0];
		} catch(Exception e) { temp=time.toString(); }
		
		int i = temp.lastIndexOf(":");
		if (i != -1)
		{
		    temp = temp.substring(0, i);
		}
		
		return temp;
	}
}
