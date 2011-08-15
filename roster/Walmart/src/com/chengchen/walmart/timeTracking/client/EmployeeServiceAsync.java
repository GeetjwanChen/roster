package com.chengchen.walmart.timeTracking.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface EmployeeServiceAsync {
	void getPeople(int weekOfYear, AsyncCallback<EmployeeTimeTrackResponse> callback)
			throws IllegalArgumentException;
}
