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
package no.ntnu.idi.socialhitchhiking.map;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.socialhitchhiking.R;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.maps.MapView;

/**
 * This activity is where a user (the driver) can see a journey request, 
 * and choose to accept or reject it.
 */
public class MapActivityJourney extends MapActivityAbstract{

	/**
	 * The pickup point.
	 */
	private Location pickupPoint;
	
	/**
	 * The dropoff point.
	 */
	private Location dropoffPoint;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		pickupPoint = getApp().getJourneyPickupPoint();
		dropoffPoint = getApp().getJourneyDropoffPoint();

		
		if(pickupPoint != null) {
			drawCross(pickupPoint, true);
		}
		if(dropoffPoint != null){
			drawCross(dropoffPoint, false);
		}
		
		boolean acc = getIntent().getBooleanExtra("journeyAccepted", false);
		boolean rej = getIntent().getBooleanExtra("journeyRejected", false);
		
		User driver = getApp().getSelectedJourney().getDriver();
		User hiker 	= getApp().getSelectedJourney().getHitchhiker();
		
		String text = "";
		if(driver != null){
			text += "Driver: "+driver.getFullName() +"\n";
		}else{
			text += "There's no driver, call 112! (or the system admin)\n";
		}
		
		if(hiker != null){
			text += "Hitchhiker: "+hiker.getFullName() +"\n";
		}else{
			text += "No hitchhiker\n";
		}
		
		if(acc){
			text += "The request has been accepted";
		}else if(rej){
			text += "The request has been rejected";
		}else{
			text += "The request has not been accepted or rejected yet";
		}
		
		((TextView)findViewById(R.id.mapViewJourneyTextView)).setText(text);
		
	}
	

	
	@Override
	protected void initContentView() {
		setContentView(R.layout.mapactivity_journey);
	}
	@Override
	protected void initMapView() {
		mapView = (MapView)findViewById(R.id.mapViewJourneyMapView); 
	}
	@Override
	protected void initProgressBar() {
		setProgressBar((ProgressBar)findViewById(R.id.mapViewJourneyProgressBar));
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
	} 
	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
