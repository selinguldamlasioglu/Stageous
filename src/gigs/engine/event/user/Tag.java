package gigs.engine.event.user;

import java.io.Serializable;

public class Tag  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3311686733943599866L;
	public String name;
	public int interest;
	
	public Tag()
	{
		name = "   ";
		interest = -1;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}

	public Tag(String name, int interest)
	{
		this.name=name;
		this.interest = interest;
	}
	
	public Tag(String name)
	{
		this.name=name;
		this.interest = 0;
	}
	
	public String toString(){
		return "" + name + " " + interest;
	}	
	
	
}
