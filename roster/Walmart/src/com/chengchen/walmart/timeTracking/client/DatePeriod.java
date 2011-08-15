package com.chengchen.walmart.timeTracking.client;



import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DatePeriod implements IsSerializable{

	private Date start;
	private Date end;
	
	public DatePeriod(){
		
	}
	
	public DatePeriod(Date first, Date second) {
		start = first;
		end = second;
	}
	
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
}
