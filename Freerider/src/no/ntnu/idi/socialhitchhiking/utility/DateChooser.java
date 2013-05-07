/*******************************************************************************
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @contributor(s): Freerider Team 2 (Group 3, IT2901 Spring 2013, NTNU)
 * @version: 2.0
 * 
 * Copyright 2013 Freerider Team 2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @version: 		1.0
 *
 * Copyright (C) 2012 Freerider Team.
 *
 * Licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package no.ntnu.idi.socialhitchhiking.utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;

import no.ntnu.idi.socialhitchhiking.findDriver.FindDriver;
import no.ntnu.idi.socialhitchhiking.journey.TripOptions;
import android.app.Activity;
//import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Custom DateChooser class used to create a new date, and firing events to
 * Listeners. This class asks the user whether the given date is correct, and
 * then fires a {@link PropertyChangeEvent} to its {@link PropertyChangeListener}.
 * Requires an activity in the Constructor for the {@link DatePickerDialog} to be
 * created upon.
 * 
 * @author Christian Thurmann-Nielsen
 *
 */
public class DateChooser {
	public static final String DATE_CHANGED="date_changed_12632135675695q3ebvcdfggd";
	public static final String TIME_CHANGED="time_changed_12632135675695q3ebvcdfggd";

	private PropertyChangeSupport l;
	private Activity act;
	private DateListener d;
	private Calendar dateAndTime;
	private Calendar start;
	private TimePickerDialog td;
	private DatePickerDialog dd;
//	private AlertDialog.Builder correctbox;

	public DateChooser(Activity act,PropertyChangeListener e){
		l = new PropertyChangeSupport(this);
		l.addPropertyChangeListener(e);
		this.act = act;
		dateAndTime = Calendar.getInstance();
		start = Calendar.getInstance();
		d = new DateListener();
		initDialogs();
	}
	/**
	 * Initiates {@link DatePicker} and {@link TimePicker} dialogs with current date and time
	 */
	private void initDialogs(){
		dd = new DatePickerDialog(act, d, dateAndTime.get(Calendar.YEAR), 
				dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));	
		dd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Set", dd);
		dd.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				fireCancelEvent();
			}
		});
		td = new TimePickerDialog(act, d, dateAndTime.get(Calendar.HOUR_OF_DAY), 
				dateAndTime.get(Calendar.MINUTE), true);
		td.setButton(DatePickerDialog.BUTTON_POSITIVE, "Set", td);
		td.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				fireCancelEvent();
			}
		});
	}
	/**
	 * Sets title for dialog window
	 * @param dateMsg Title for {@link DatePicker} dialog
	 * @param timeMsg Title for {@link TimePicker} dialog
	 */
	public void setTitle(String dateMsg,String timeMsg){
		dd.setTitle(dateMsg);
		td.setTitle(timeMsg);
	}
	/**
	 * Sets title for set date dialog window
	 * @param dateMsg Title for {@link DatePicker} dialog
	 */
	public void setTitleDate(String dateMsg){
		dd.setTitle(dateMsg);
	}
	/**
	 * Sets title for set time dialog window
	 * @param timeMsg Title for {@link TimePicker} dialog
	 */
	public void setTitleTime(String timeMsg){
		td.setTitle(timeMsg);
	}
	/**
	 * Shows {@link DatePicker} dialog and then depending of the class that calls 
	 * this function it will show {@link TimePicker} or end.
	 */
	public void show(){
		showDatePicker();
	}
	/**
	 * Shows {@link DatePicker} dialog only
	 */
	public void showDatePicker(){
		dd.show();
	}
	/**
	 * Shows {@link TimePicker} dialog only
	 */
	public void showTimePicker(){
		td.show();
	}
	/**
	 * If the activity calling is FindDriver it 
	 */
	private void fireCancelEvent(){
		if(act instanceof FindDriver){
			PropertyChangeEvent e = new PropertyChangeEvent(this, DATE_CHANGED, null, null);
			l.firePropertyChange(e);			
		}
	}
	/**
	 * Notifies that date has changed
	 */
	private void fireEvent(){
		PropertyChangeEvent e = new PropertyChangeEvent(this, DATE_CHANGED, start, dateAndTime);
		l.firePropertyChange(e);
	}
	/**
	 * Notifies that time has changed
	 */
	private void fireEventTime(){
		PropertyChangeEvent e = new PropertyChangeEvent(this, TIME_CHANGED, start, dateAndTime);
		l.firePropertyChange(e);
	}

	/**
	 * Notifies changes when Date or Time are changed in {@link DatePicker} and {@link TimePicker} dialogs
	 */
	private class DateListener implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			//showTimePicker();
			fireEvent();
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			dateAndTime.set(Calendar.SECOND, 0);
			//if(act.getClass() == FindDriver.class || act.getClass() == TripOptions.class) 
			fireEventTime();
			//else confirmDate();
		}

	}

}
