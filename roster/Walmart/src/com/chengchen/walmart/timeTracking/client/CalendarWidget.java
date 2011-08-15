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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

/**
 * A Composite widget that abstracts a DynaTableWidget and a data provider tied
 * to the <@link SchoolCalendarService> RPC endpoint.
 */
public class CalendarWidget extends Composite {


	
	/**
	 * A data provider that bridges the provides row level updates from the data
	 * available through a <@link SchoolCalendarService>.
	 */
	public class CalendarProvider implements DynaTableDataProvider {
		private final EmployeeServiceAsync calService = GWT.create(EmployeeService.class);	
		
		private Person[] lastPeople;
		private DatePeriod period;

		private int laststartWeek = -1;

		public CalendarProvider() {
		}

		public void updateRowData(final int startWeek,
				final RowDataAcceptor acceptor) {
			// Check the simple cache first.
			//
			if (startWeek == laststartWeek) {
				pushResults(acceptor, lastPeople, period);
				return;
			}

			// Fetch the data remotely.
			//
			calService.getPeople(startWeek, new AsyncCallback<EmployeeTimeTrackResponse>() {
				public void onFailure(Throwable caught) {
					acceptor.failed(caught);
				}

				public void onSuccess(EmployeeTimeTrackResponse result) {
					lastPeople = result.getPersons();
					laststartWeek = startWeek;
					
					pushResults(acceptor, result.getPersons(), result.getPeriod());
				}

			});
		}

		private void pushResults(RowDataAcceptor acceptor, Person[] people, DatePeriod period) {
			String[][] rows = new String[people.length][];
			for (int i = 0, n = rows.length; i < n; i++) {
				Person person = people[i];
				rows[i] = new String[2];
				rows[i][0] = person.getName();
				rows[i][1] = person.getSchedule(daysFilter);
			}
			this.period = period;
			acceptor.accept(rows, period);
		}
	}

	private final CalendarProvider calProvider = new CalendarProvider();

	private final boolean[] daysFilter = new boolean[] { true, true, true,
			true, true, true, true };

	private final DynaTableWidget dynaTable;

	private Command pendingRefresh;

	public CalendarWidget() {
		String[] columns = new String[] { "Name", "Schedule" };
		String[] styles = new String[] { "name", "sched" };
		dynaTable = new DynaTableWidget(calProvider, columns, styles);
		initWidget(dynaTable);
	}

	protected boolean getDayIncluded(int day) {
		return daysFilter[day];
	}

	@Override
	protected void onLoad() {
		dynaTable.refresh();
	}

	protected void setDayIncluded(int day, boolean included) {
		if (daysFilter[day] == included) {
			// No change.
			//
			return;
		}

		daysFilter[day] = included;
		if (pendingRefresh == null) {
			pendingRefresh = new Command() {
				public void execute() {
					pendingRefresh = null;
					dynaTable.refresh();
				}
			};
			DeferredCommand.addCommand(pendingRefresh);
		}
	}
}
