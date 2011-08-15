/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.chengchen.walmart.timeTracking.client;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The implemenation of the RPC service which runs on the server.
 */
public class EmployeeCalendarService {

	
	private static final String[] NAMES = new String[] {
	      "Inman Mendez", "Omar Blum", "Eddie Edelstein", "Jimmy Epps", "Barney Crutcher", 
	      "Eddie Chase", "Jimmy Crutcher", "Cheng Chen", "Carl Chen", "David Zhang"      
	};

  private static final int CLASS_LENGTH_MINS = 50;

  private static final int MAX_SCHED_ENTRIES = 5;

  private static final int MIN_SCHED_ENTRIES = 1;

  private final List<Employee> people = new ArrayList<Employee>();

  private final Random rnd = new Random(3);

  public EmployeeCalendarService() {

  }
  
  public Person[] getPeople(int weekOfYear) {
	  Date now = new Date();
	  generatePeople();
	  List<TimeSlot> toBeRemove = new LinkedList<TimeSlot>();
	  /*
	  for (Employee p : people) {
		  Schedule s = p.getClassSchedule();
		  for (TimeSlot t : s.getTimeSlots()){
			if (t.getWeekOfYear() != weekOfYear
					|| t.getYear() !=  now.getYear()){
				toBeRemove.add(t);
			}
		  }
		  s.getTimeSlots().removeAll(toBeRemove);
 	  }
 	  */
	return people.toArray(new Person[people.size()]);
  }
  
 
  private void generatePeople() {
	  people.clear();
    for (int i = 0; i < NAMES.length; ++i) {
    	Employee person = generatePerson(i);
    	/*
    	for (int j= -2; j < 4; j ++) {
			  generateRandomSchedule(person.getClassSchedule(), j);
		  } 
		  */
      people.add(person);
    }
  }

  private Employee generatePerson(int i) {
	  Employee prof = new Employee();
	  prof.setName(NAMES[i]);
	  return prof;
  }

  private void generateRandomSchedule(Schedule sched, int week) {
    int range = MAX_SCHED_ENTRIES - MIN_SCHED_ENTRIES + 1;
    int howMany = MIN_SCHED_ENTRIES + rnd.nextInt(range);

    TimeSlot[] timeSlots = new TimeSlot[howMany];

    for (int i = 0; i < howMany; ++i) {
      int startHrs = 8 + rnd.nextInt(9); // 8 am - 5 pm
      int startMins = 15 * rnd.nextInt(4); // on the hour or some quarter
      int dayOfWeek = 0 + rnd.nextInt(6); // Mon - Fri

      int absStartMins = 60 * startHrs + startMins; // convert to minutes
      int absStopMins = absStartMins + CLASS_LENGTH_MINS;

      Date now = new Date();
      int diff = dayOfWeek - now.getDay();
      long current = now.getTime();
      current = current + ((long)diff * DAY_IN_MILSEC) + (week * 7 * DAY_IN_MILSEC) ;
      now.setTime(current);
      timeSlots[i] = new TimeSlot(absStartMins, absStopMins, now.getDay() -1, now);
    }
    Arrays.sort(timeSlots);

    for (int i = 0; i < howMany; ++i) {
      sched.addTimeSlot(timeSlots[i]);
    }
  }
  
  private static final long DAY_IN_MILSEC = 24L * 60L * 60L * 1000L;
  
}
