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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Hold relevant data for Person. This class is meant to be serialized in RPC
 * calls.
 */
public abstract class Person implements IsSerializable, Comparable<Person> { 

	private String name;
	private String storeId;
	private String employeeId;

	public Person() {
	}

	public String getName() {
		return name;
	}

	public int compareTo(Person o) {
		return name.compareTo(o.name);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Person)) {
			return false;
		}
		return compareTo((Person) obj) == 0;
	}

	public abstract String getSchedule(boolean[] daysFilter);

	public void setName(String name) {
		this.name = name;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
