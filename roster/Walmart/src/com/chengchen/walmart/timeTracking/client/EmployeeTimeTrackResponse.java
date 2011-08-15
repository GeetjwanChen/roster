package com.chengchen.walmart.timeTracking.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EmployeeTimeTrackResponse implements IsSerializable{

	private Person[] persons;
	private DatePeriod period;
	
	
	
	public Person[] getPersons() {
		return persons;
	}
	
	
	public EmployeeTimeTrackResponse() {
		
	}


	public void setPersons(Person[] persons) {
		this.persons = persons;
	}
	public DatePeriod getPeriod() {
		return period;
	}
	public void setPeriod(DatePeriod period) {
		this.period = period;
	}
	
	
	
}
