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
package no.ntnu.idi.socialhitchhiking.map;

import java.io.Serializable;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.MapLocation;
import no.ntnu.idi.socialhitchhiking.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * This activity is used for searching after journeys in map mode, so the 
 * user can long press a point in the map instead of writing the address.
 */
public class MapActivitySearch extends MapActivityAbstract{

	/**
	 * The search {@link Button}.
	 */
	private Button btnSearch;
	
	/**
	 * The back {@link Button}.
	 */
	private Button btnBack;
	
	/**
	 * The {@link Location} that the user wants a ride from.
	 */
	private Location goingFrom;
	
	/**
	 * The {@link Location} that the user wants a ride to.
	 */
	private Location goingTo;
	
	/**
	 * The {@link Overlay} that should show the {@link #goingFrom} on the map.
	 */
	private Overlay overlayFrom = null;
	
	/**
	 * The {@link Overlay} that should show the {@link #goingFrom} on the map.
	 */
	private Overlay overlayTo = null;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		btnSearch = (Button)findViewById(R.id.mapViewSearchBtnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent data = new Intent();
				if(goingFrom != null){
					data.putExtra("goingFrom", (Serializable)goingFrom);
				}
				if(goingTo != null){
					data.putExtra("goingTo", (Serializable)goingTo);
				}
				setResult(7331, data);
				finish();
			}
		});
		btnBack = (Button)findViewById(R.id.mapViewSearchBtnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		makeToast("Long press to choose address");
	}
	
	@Override
	protected void initContentView() {
		setContentView(R.layout.mapactivity_search);
	}
	@Override
	protected void initMapView() {
		mapView = (MapView)findViewById(R.id.mapViewSearchMapView); 
	}
	@Override
	protected void initProgressBar() {
		setProgressBar((ProgressBar)findViewById(R.id.mapViewSearchProgressBar));
	}
	
	/**
	 * When long pressing the map, a red or green thumb should be 
	 * drawn on the map.
	 */
	@Override
	public synchronized void onLongPress(MotionEvent e) {
		GeoPoint gp = mapView.getProjection().fromPixels(
				(int) e.getX(),
				(int) e.getY());
		final MapLocation mapLocation = (MapLocation) GeoHelper.getLocation(gp);
		
		new AlertDialog.Builder(this).
		setTitle("Add address").
		setMessage(mapLocation.getAddressWithLines()).
		setPositiveButton("From", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(overlayFrom != null){
					mapView.getOverlays().remove(overlayFrom);
					goingFrom = null;
				}
				goingFrom = mapLocation;
				overlayFrom = drawThumb(mapLocation, true);
				mapView.invalidate();
			}
		}).
		setNeutralButton("To", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(overlayTo != null){
					mapView.getOverlays().remove(overlayTo);
					goingTo = null;
				}
				goingTo = mapLocation;
				overlayTo = drawThumb(mapLocation, false);
				mapView.invalidate();
			}
		}).
		setNegativeButton("Cancel", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		}).
		show();
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
