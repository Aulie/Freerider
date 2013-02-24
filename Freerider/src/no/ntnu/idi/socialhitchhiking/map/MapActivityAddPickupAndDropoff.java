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

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import no.ntnu.idi.freerider.model.Journey;
import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.MapLocation;
import no.ntnu.idi.freerider.model.Notification;
import no.ntnu.idi.freerider.model.NotificationType;
import no.ntnu.idi.freerider.model.User;
import no.ntnu.idi.freerider.protocol.NotificationRequest;
import no.ntnu.idi.freerider.protocol.RequestType;
import no.ntnu.idi.freerider.protocol.Response;
import no.ntnu.idi.freerider.protocol.ResponseStatus;
import no.ntnu.idi.freerider.protocol.UserResponse;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.client.RequestTask;

import org.apache.http.client.ClientProtocolException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * The activity used when a user should select where he/she wants to be picked up (pickup point), 
 * and where he/she wants to be dropped off (dropoff point). 
 *
 */
public class MapActivityAddPickupAndDropoff extends MapActivityAbstract{

	/**
	 * When this field is true, the user should select a pickup point, by touching the map.
	 */
	private boolean isSelectingPickupPoint = true;
	
	/**
	 * When this field is true, the user should select a dropoff point, by touching the map.
	 */
	private boolean isSelectingDropoffPoint = false;
	
	/**
	 * The button to be pressed when the user should select a pickup point. 
	 */
	private Button btnSelectPickupPoint;
	
	/**
	 * The button to be pressed when the user should select a dropoff point.
	 */
	private Button btnSelectDropoffPoint;
	
	/**
	 * The button to press for sending a request.
	 */
	private Button btnSendRequest;
	
	/**
	 * The selected pickup point.
	 */
	private Location pickupPoint;
	
	/**
	 * The selected dropoff point.
	 */
	private Location dropoffPoint;
	
	/**
	 * The color that pickup and dropoff button should have when they have been selected. 
	 * Only one of the buttons will have this at the same time.
	 */
	private int selected = Color.argb(200, 170, 170, 250);
	
	/**
	 * The color that pickup and dropoff button should have when they are <i>not</i> selected.
	 */
	private int notSelected = Color.argb(200, 200, 200, 200);
	
	/**
	 * The {@link Overlay} that is used for drawing the pickup point.
	 * Is null when no pickup point is selected.
	 */
	private Overlay overlayPickupCross = null;
	
	/**
	 * The {@link Overlay} that is used for drawing the dropoff point.
	 * Is null when no dropoff point is selected.
	 */
	private Overlay overlayDropoffCross = null;
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Journey journey = getApp().getSelectedJourney();
		// Setting up tabs
		TabHost tabs = (TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		
		TabHost.TabSpec spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.ride_tab);
		spec.setIndicator("Ride");
		tabs.addTab(spec);
		
		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.driver_tab);
		spec.setIndicator("Driver");
		tabs.addTab(spec);
		
		try{
			((ImageView)findViewById(R.id.mapViewPickupImage)).setImageBitmap(getPicture(journey.getRoute().getOwner()));
		}catch (Exception e) {
			//Uses facebook logo
		}
		((TextView)findViewById(R.id.mapViewPickupTextViewName)).setText(journey.getRoute().getOwner().getFullName());
		Date d = journey.getStart().getTime();
		String s = d.toLocaleString();
		((TextView)findViewById(R.id.mapViewPickupTextViewDate)).setText(s);
		
		btnSendRequest = (Button)findViewById(no.ntnu.idi.socialhitchhiking.R.id.mapViewPickupBtnSendRequest);
		btnSendRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Response res;
				NotificationRequest req;
				
				if(pickupPoint == null || dropoffPoint == null){
					makeToast("You have to choose pickup point and dropoff point.");
					return;
				}
				
				inPickupMode = true;
				
				String senderID = getApp().getUser().getID();
				String recipientID = getApp().getSelectedJourney().getRoute().getOwner().getID();
				String senderName = getApp().getUser().getFullName();
				String comment = ((EditText)findViewById(R.id.mapViewPickupEtComment)).getText().toString();
				int journeyID = getApp().getSelectedJourney().getSerial();
				
				Notification n = new Notification(senderID, recipientID, senderName, comment, journeyID, NotificationType.HITCHHIKER_REQUEST, pickupPoint, dropoffPoint, Calendar.getInstance());
				req = new NotificationRequest(RequestType.SEND_NOTIFICATION, getApp().getUser(), n);
				
				try {
					res = RequestTask.sendRequest(req,getApp());
					if(res instanceof UserResponse){
						if(res.getStatus() == ResponseStatus.OK){
							makeToast("Notification sent");
							finish();
						}
						if(res.getStatus() == ResponseStatus.FAILED){
							if(res.getErrorMessage().contains("no_duplicate_notifications")){
								makeToast("You have already sent a request on this journey");
							}
							else{
								makeToast("Could not send request");
							}
						}
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		btnSelectPickupPoint = (Button)findViewById(R.id.mapViewPickupBtnPickup);
		btnSelectDropoffPoint = (Button)findViewById(R.id.mapViewPickupBtnDropoff);
		
		setSelectingPickupPoint();
		btnSelectPickupPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setSelectingPickupPoint();
			}
		});
		btnSelectDropoffPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setSelectingDropoffPoint();
			}
		});
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			Serializable ser1 = extras.getSerializable("searchFrom");
			Serializable ser2 = extras.getSerializable("searchTo");
			if(ser1 != null && ser1 instanceof Location){
				drawThumb((Location)ser1, true);
			}
			if(ser2 != null && ser2 instanceof Location){
				drawThumb((Location)ser2, false);
			}
		}
	}
	
	//These two fields are used to make sure the toast "select a pickup point", doesn't pop up too often.
	private int pickCount = 0;
	private long pickLastTime = 0;
	
	/**
	 * This method should be called when the user should select a pickup point.
	 */
	private void setSelectingPickupPoint(){
		isSelectingPickupPoint = true;
		isSelectingDropoffPoint = false;
		btnSelectPickupPoint.setBackgroundColor(selected); 
		btnSelectDropoffPoint.setBackgroundColor(notSelected);
		
		if(pickCount == 0 || (System.currentTimeMillis()-pickLastTime) > 2000){
			makeToast("Select a pickup point.");
			pickLastTime = System.currentTimeMillis();
		}
		
		if(overlayPickupCross != null){
			mapView.getOverlays().remove(overlayPickupCross);
			overlayPickupCross = null;
			pickupPoint = null;
		}
		mapView.invalidate();
		pickCount++;
	}
	
	//These two fields are used to make sure the toast "select a dropoff point", doesn't pop up too often.
	private int dropCount = 0;
	private long dropLastTime = 0;
	
	/**
	 * This method should be called when the user should select a dropoff point.
	 */
	private void setSelectingDropoffPoint(){
		isSelectingPickupPoint = false;
		isSelectingDropoffPoint = true;
		btnSelectPickupPoint.setBackgroundColor(notSelected); 
		btnSelectDropoffPoint.setBackgroundColor(selected);
		
		if(dropCount == 0 || (System.currentTimeMillis()-dropLastTime) > 2000){
			makeToast("Select a dropoff point.");
			dropLastTime = System.currentTimeMillis();
		}
		
		if(overlayDropoffCross != null){
			mapView.getOverlays().remove(overlayDropoffCross);
			overlayDropoffCross = null;
			dropoffPoint = null;
		}
		mapView.invalidate();
		dropCount++;
	}
	
	/**
	 * Should be called when both a pickup point and a dropoff point has been selected.
	 */
	private void setDoneSelecting(){
		isSelectingPickupPoint = false;
		isSelectingDropoffPoint = false;
		btnSelectPickupPoint.setBackgroundColor(notSelected); 
		btnSelectDropoffPoint.setBackgroundColor(notSelected);
	}

	@Override
	protected void initContentView() {
		setContentView(R.layout.mapactivity_pickup_and_dropoff);
	}
	@Override
	protected void initMapView() {
		mapView = (MapView)findViewById(R.id.mapViewPickupAndDropoffMapView); 
	}
	@Override
	protected void initProgressBar() {
		setProgressBar((ProgressBar)findViewById(R.id.mapViewPickupProgressBar));
	}
	
	/**
	 * Static method that retrieves a users Facebook profile picture.
	 * 
	 * @param id - String, containing a Facebook users id.
	 * @return {@link Bitmap} of the users profile picture.
	 */
	private static Bitmap getPicture(User user){
		Bitmap mIcon1 = null;
		try {
			mIcon1 = BitmapFactory.decodeStream(user.getPictureURL().openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mIcon1;
	}

	/**
	 * This method loops trough the route path, to find out which {@link Location} is 
	 * closest to the given {@link MapLocation}, and returns this.
	 */
	private Location findClosestLocationOnRoute(MapLocation ml){
		List<Location> l = getApp().getSelectedJourney().getRoute().getRouteData();
		
		Location lClosest = l.get(0);
		for (int i = 0; i < l.size(); i++) {
			lClosest = closest(ml, lClosest, l.get(i));
		}
		return lClosest;
	}

	/**
	 * Takes three parameters. Returns the one {@link Location}-parameter of the two last, that is closest 
	 * to the first given {@link Location}.
	 */
	private static Location closest(Location loc, Location a, Location b){
		android.location.Location location = new android.location.Location("");
		location.setLatitude(loc.getLatitude());
		location.setLongitude(loc.getLongitude());
		android.location.Location lA = new android.location.Location("");
		lA.setLatitude(a.getLatitude());
		lA.setLongitude(a.getLongitude());
		android.location.Location lB = new android.location.Location("");
		lB.setLatitude(b.getLatitude());
		lB.setLongitude(b.getLongitude());
				
		float distA = location.distanceTo(lA);
		float distB = location.distanceTo(lB);
		
		if(distA < distB) return a;
		return b;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		//Does nothing
	} 
	
	
	/**
	 * When someone presses the map, this method is called and draws the pickup 
	 * or dropoff point on the map depending on which of the {@link Button}s, 
	 * {@link #btnSelectPickupPoint} or {@link #btnSelectDropoffPoint} that is pressed.
	 */
	@Override
	public synchronized boolean onSingleTapUp(MotionEvent e) {
		if(!isSelectingDropoffPoint && !isSelectingPickupPoint) return false;
		GeoPoint gp = mapView.getProjection().fromPixels(
				(int) e.getX(),
				(int) e.getY());
		MapLocation mapLocation = (MapLocation) GeoHelper.getLocation(gp);
		
		if(isSelectingPickupPoint){
			Location temp = findClosestLocationOnRoute(mapLocation);
			
			if(dropoffPoint == null){
				pickupPoint = temp;
				overlayPickupCross = drawCross(pickupPoint, true);
				setSelectingDropoffPoint();
			}else{
				List<Location> l = getApp().getSelectedJourney().getRoute().getRouteData();
				if(l.indexOf(temp) < l.indexOf(dropoffPoint)){
					makeToast("The pickup point has to be before the dropoff point");
				}else{
					pickupPoint = temp;
					overlayPickupCross = drawCross(pickupPoint, true);
					setDoneSelecting();
				}
			}
		}else if(isSelectingDropoffPoint){
			Location temp = findClosestLocationOnRoute(mapLocation);
			
			if(pickupPoint == null){
				dropoffPoint = temp;
				overlayDropoffCross = drawCross(dropoffPoint, false);
				setSelectingPickupPoint();
			}else{
				List<Location> l = getApp().getSelectedJourney().getRoute().getRouteData();
				if(l.indexOf(temp) > l.indexOf(pickupPoint)){
					makeToast("The droppoff point has to be after the pickup point");
				}else{
					dropoffPoint = temp;
					overlayDropoffCross = drawCross(dropoffPoint, false);
					setDoneSelecting();
				}
			}
		}
		return true;
	}
	
	
}
