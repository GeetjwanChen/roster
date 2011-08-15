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


import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Hold relevant data for a time slot. This class is intended to be serialized
 * as part of RPC calls.
 */
public class TimeSlot implements IsSerializable, Comparable<TimeSlot> {

  private static final transient String[] DAYS = new String[] {
      "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};

  private static final transient String[] MONTHS = new String[] {
      "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", 
      "Aug", "Sept", "Oct", "Nov", "Dec"};

  
  private int endMinutes;

  private int startMinutes;

  private int zeroBasedDayOfWeek;
  
  private Date date;
  
  public TimeSlot() {
  }

  public TimeSlot(int startMinutes, int endMinutes, int dayOfWeek, Date cal) {
    this.startMinutes = startMinutes;
    this.endMinutes = endMinutes;
    this.date = cal;
    this.zeroBasedDayOfWeek = dayOfWeek;
  }

  public int compareTo(TimeSlot o) {
	 if (date.getYear() < o.getDate().getYear()){
		 return -1;
	 } else if (date.getYear() > o.getDate().getYear()){
		return 1;
	} else if (date.getMonth() < o.getDate().getMonth()){
		return -1;
	 } else if (date.getMonth() > o.getDate().getMonth()){
		 return 1;
	 } else if (date.getDate() < o.getDate().getDate()){
		 return -1;
	 } else if (date.getDate() > o.getDate().getDate()){
		 return 1;
	 } else {
      if (startMinutes < o.startMinutes) {
        return -1;
      } else if (startMinutes > o.startMinutes) {
        return 1;
      }
    }
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof TimeSlot)) {
      return false;
    }
    return compareTo((TimeSlot) obj) == 0;
  }
  
  public int getDayOfWeek() {
    return zeroBasedDayOfWeek;
  }

  public String getDescription() {
    return getDateToString() + " " + DAYS[zeroBasedDayOfWeek] + " " + getHrsMins(startMinutes) + "-"
        + getHrsMins(endMinutes);
  }

  private String getDateToString(){
	  return "(" + MONTHS[date.getMonth()] + " " + date.getDate() + ")";
  }
  
  public int getEndMinutes() {
    return endMinutes;
  }

  public int getStartMinutes() {
    return startMinutes;
  }

  @Override
  public int hashCode() {
    return endMinutes + 7 * startMinutes + date.hashCode();
  }

  public void setDayOfWeek(int zeroBasedDayOfWeek) {
    if (0 <= zeroBasedDayOfWeek && zeroBasedDayOfWeek < 7) {
      this.zeroBasedDayOfWeek = zeroBasedDayOfWeek;
    } else {
      throw new IllegalArgumentException("day must be in the range 0-6");
    }
  }

  public void setEndMinutes(int endMinutes) {
    this.endMinutes = endMinutes;
  }

  public void setStartMinutes(int startMinutes) {
    this.startMinutes = startMinutes;
  }

  private String getHrsMins(int mins) {
	boolean pm = false;
    int hrs = mins / 60;
    if (hrs > 12) {
      hrs -= 12;
      pm = true;
    }
    int remainder = mins % 60;
    return hrs + ":"
        + (remainder < 10 ? "0" + remainder : String.valueOf(remainder)) 
        + (pm ? "PM" : "AM");
  }

  public Date getDate() {
	  return date;
  }

  public void setDate(Date _date) {
	  if (_date != null) {
		  setDayOfWeek(_date.getDay());
		  this.date = _date;
	  }
  }
    
  public int getYear(){
	  return date.getYear();
  }
  
}
