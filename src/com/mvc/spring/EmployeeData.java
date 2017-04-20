package com.mvc.spring;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import javax.jdo.annotations.PrimaryKey;
	
@PersistenceCapable
public class EmployeeData {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	//@Persistent 
	private Long id;
	
	
	private String employeeLoginEmail;
	
	
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String firstname;
	
	@Persistent 
    private String lastname;
	
	public String getEmployeeLoginEmail() {
		return employeeLoginEmail;
	}
	public void setEmployeeLoginEmail(String employeeLoginEmail) {
		this.employeeLoginEmail = employeeLoginEmail;
	}
	@Persistent 
    private String age;
	private String profPic;
	
	public String getProfPic() {
		return profPic;
	}
	public void setProfPic(String profPic) {
		this.profPic = profPic;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	
	

	

}
