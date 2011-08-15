package com.chengchen.walmart.timeTracking.server;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.chengchen.walmart.timeTracking.client.DatePeriod;
import com.chengchen.walmart.timeTracking.client.Employee;
import com.chengchen.walmart.timeTracking.client.EmployeeService;
import com.chengchen.walmart.timeTracking.client.EmployeeTimeTrackResponse;
import com.chengchen.walmart.timeTracking.client.TimeSlot;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EmployeeServiceImpl extends RemoteServiceServlet implements
		EmployeeService {

	public EmployeeTimeTrackResponse getPeople(int weekOfYear) throws IllegalArgumentException {
		Map<String, Employee> employeeMap = new HashMap<String, Employee>();
		DatePeriod period = getPeriod(weekOfYear); 
		try {
			URL feedUrl = new URL(
					"https://www.google.com/calendar/feeds/default/private/full");
			// https://www.google.com/calendar/feeds/default/owncalendars/full
			// https://www.google.com/calendar/feeds/default/allcalendars/full

			CalendarQuery myQuery = new CalendarQuery(feedUrl);
			/*
			myQuery.setMinimumStartTime(DateTime
					.parseDateTime("2011-08-05T00:00:00"));
			myQuery.setMaximumStartTime(DateTime
					.parseDateTime("2011-08-24T23:59:59"));
			*/
			myQuery.setMinimumStartTime(new DateTime(period.getStart()));
			myQuery.setMaximumStartTime(new DateTime(period.getEnd()));
			
			CalendarService myService = new CalendarService(
					"Chengchen-walmartsupercenterroster-1");
			myService.setUserCredentials("walmacdonald@gmail.com", "123321aa");

			CalendarEventFeed resultFeed = myService.query(myQuery,
					CalendarEventFeed.class);

			for (int i = 0; i < resultFeed.getEntries().size(); i++) {
				CalendarEventEntry entry = resultFeed.getEntries().get(i);
				String name = entry.getTitle().getPlainText();
				if (employeeMap.get(name) == null) {
					Employee e = new Employee();
					e.setName(name);
					employeeMap.put(name, e);
				}
				Employee e = employeeMap.get(name);
				TimeSlot slot;
				for (When when : entry.getTimes()) {
					DateTime end = when.getEndTime();
					DateTime start = when.getStartTime();
					Calendar now = Calendar.getInstance(timezone);
					now.setTimeInMillis(start.getValue());
					slot = new TimeSlot(getAbsStartMins(start),
							getAbsStartMins(end), now.get(Calendar.DAY_OF_WEEK) - 1, now.getTime());
					e.getClassSchedule().addTimeSlot(slot);
				}
				Collections.sort(e.getClassSchedule().getTimeSlots());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		Employee[] persons = new Employee[employeeMap.size()];
		int i = 0;
		for (Employee e : employeeMap.values()) {
			persons[i] = e;
			i++;
		}
		Arrays.sort(persons);
		EmployeeTimeTrackResponse resp = new EmployeeTimeTrackResponse();
		resp.setPeriod(period);
		resp.setPersons(persons);
		return resp;
	}

	private int getAbsStartMins(DateTime t) {
		Calendar now = Calendar.getInstance(timezone);
		now.setTimeInMillis(t.getValue());
		
		int hr = now.get(Calendar.HOUR_OF_DAY);
		int mins = now.get(Calendar.MINUTE);
		return (60 * hr) + mins;
	}
	
	
	private DatePeriod getPeriod(int weekOffset){
		Calendar first = Calendar.getInstance(timezone);
		first.add(Calendar.WEEK_OF_YEAR, weekOffset);
		int day = first.get(Calendar.DAY_OF_WEEK); 
		if ( day != 1) {
			first.add(Calendar.DATE,  1 - day);
		} 
		first.set(Calendar.HOUR_OF_DAY, 0);
		first.set(Calendar.MINUTE, 0);
		first.set(Calendar.SECOND, 0);
		first.set(Calendar.MILLISECOND, 0);
		
		Calendar end = (Calendar)first.clone();
		end.add(Calendar.DATE,  6);
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 0);
		
		DatePeriod result = new DatePeriod(first.getTime(), end.getTime());
		return result;	
	}
	
	private static final long WEEK_IN_MILLSEC = 7l * 24l * 60l * 60l * 1000l;
	private static final TimeZone timezone = TimeZone.getTimeZone("America/Vancouver");  
}
