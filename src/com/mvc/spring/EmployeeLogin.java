package com.mvc.spring;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class EmployeeLogin {

	
	@Persistent
	public String name;

	@Persistent
	public String email;
	
	
	
	public String getUname() {
		return name;
	}

	public void setUname(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	@Persistent
	public String pic;

	public String getpic() {
		return pic;
	}

	public void setpic(String pic) {
		this.pic = pic;
	}

	
	

}
