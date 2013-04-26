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

package no.ntnu.idi.socialhitchhiking.map;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.NotificationType;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.inbox.NotificationHandler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.maps.MapView;

/**
 * This activity is where a user (the driver) can see a journey request, 
 * and choose to accept or reject it.
 */
public class MapActivityJourneyAccept extends MapActivityAbstract{

	/**
	 * The accept {@link Button}.
	 */
	private Button btnAccept;
	
	/**
	 * The reject {@link Button}.
	 */
	private Button btnReject;
	
	/**
	 * The pickup point.
	 */
	private Location pickupPoint;
	
	/**
	 * The dropoff point.
	 */
	private Location dropoffPoint;
	
	/**
	 * The selected notification
	 */
	private Notification notif;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		pickupPoint = getApp().getJourneyPickupPoint();
		dropoffPoint = getApp().getJourneyDropoffPoint();
		notif = getApp().getSelectedNotification();

		btnReject = (Button)findViewById(R.id.mapViewJourneyAcceptBtnSendReject);
		btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleNotification(NotificationType.REQUEST_REJECT);
			}
		});
		btnAccept = (Button)findViewById(R.id.mapViewJourneyAcceptBtnSendAccept);
		btnAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleNotification(NotificationType.REQUEST_ACCEPT);
			}
		});
		if(pickupPoint != null) {
			drawCross(pickupPoint, true);
		}
		if(dropoffPoint != null){
			drawCross(dropoffPoint, false);
		}
	}
	
	/**
	 * Handles the accept or reject notification, that should be sent.
	 */
	private void handleNotification(NotificationType type){
		if(NotificationHandler.handleMap(type, ((EditText)findViewById(R.id.mapViewJourneyAcceptEtComment)).getText().toString())){
			getApp().fireNotificationChanged(notif);
			makeToast("Request succesfully handled");
			finish();
		}
		else makeToast("Failed to handle request");
		
	}
	
	@Override
	protected void initContentView() {
		setContentView(R.layout.mapactivity_journey_accept);
	}
	@Override
	protected void initMapView() {
		mapView = (MapView)findViewById(R.id.mapViewJourneyAcceptMapView); 
	}
	@Override
	protected void initProgressBar() {
		setProgressBar((ProgressBar)findViewById(R.id.mapViewJourneyAcceptProgressBar));
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
