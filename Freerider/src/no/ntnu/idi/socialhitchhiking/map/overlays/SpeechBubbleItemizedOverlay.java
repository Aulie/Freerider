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
package no.ntnu.idi.socialhitchhiking.map.overlays;

import java.util.List;

import no.ntnu.idi.socialhitchhiking.R;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public abstract class SpeechBubbleItemizedOverlay<Item extends OverlayItem> extends ItemizedOverlay<Item> {

	private MapView mapView;
	private SpeechBubbbleOverlayView<Item> speechBubbleView;
	private View clickRegion;
	private View closeRegion;
	private int viewOffset;
	final MapController mc;
	private Item currentFocusedItem;
	private int currentFocusedIndex;
	
	public SpeechBubbleItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		this.mapView = mapView;
		viewOffset = 0;
		mc = mapView.getController();
	}
	
	public void setSpeechBubbleBottomOffset(int pixels) {
		viewOffset = pixels;
	}
	public int getSpeechBubbleBottomOffset() {
		return viewOffset;
	}
	protected boolean onSpeechBubbleTap(int index, Item item) {
		return false;
	}
	protected void onSpeechBubbleOpen(int index) {		
	}
	
	@Override
	public final boolean onTap(int index) {
		
		currentFocusedIndex = index;
		currentFocusedItem = createItem(index);
		setLastFocusedIndex(index);
		
		onSpeechBubbleOpen(index);
		createAndDisplaySpeechBubbleOverlay();
		
		mc.animateTo(currentFocusedItem.getPoint());
		
		return true;
	}

	protected SpeechBubbbleOverlayView<Item> createSpeechBubbleOverlayView() {
		return new SpeechBubbbleOverlayView<Item>(getMapView().getContext(), getSpeechBubbleBottomOffset());
	}
	
	protected MapView getMapView() {
		return mapView;
	}
	
	public void hideSpeechBubble() {
		if (speechBubbleView != null) {
			speechBubbleView.setVisibility(View.GONE);
		}
		currentFocusedItem = null;
	}
	
	private void hideOtherSpeechBubbles(List<Overlay> overlays) {
		for (Overlay overlay : overlays) {
			if (overlay instanceof SpeechBubbleItemizedOverlay<?> && overlay != this) {
				((SpeechBubbleItemizedOverlay<?>) overlay).hideSpeechBubble();
			}
		}
		
	}
	
	private OnTouchListener createSpeechBubbleTouchListener() {
		return new OnTouchListener() {
			float startX;
			float startY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View l =  ((View) v.getParent()).findViewById(R.id.speech_bubble_main_layout);
				Drawable d = l.getBackground();
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int[] states = {android.R.attr.state_pressed};
					if (d.setState(states)) {
						d.invalidateSelf();
					}
					startX = event.getX();
					startY = event.getY();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					int newStates[] = {};
					if (d.setState(newStates)) {
						d.invalidateSelf();
					}
					if (Math.abs(startX - event.getX()) < 40 && 
							Math.abs(startY - event.getY()) < 40 ) {
						// call overridden method
						onSpeechBubbleTap(currentFocusedIndex, currentFocusedItem);
					}
					return true;
				} else {
					return false;
				}
				
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#getFocus()
	 */
	@Override
	public Item getFocus() {
		return currentFocusedItem;
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#setFocus(Item)
	 */
	@Override
	public void setFocus(Item item) {
		super.setFocus(item);	
		currentFocusedIndex = getLastFocusedIndex();
		currentFocusedItem = item;
		if (currentFocusedItem == null) {
			hideSpeechBubble();
		} else {
			createAndDisplaySpeechBubbleOverlay();
		}	
	}
	
	
	private boolean createAndDisplaySpeechBubbleOverlay(){
		boolean isRecycled;
		if (speechBubbleView == null) {
			speechBubbleView = createSpeechBubbleOverlayView();
			clickRegion = speechBubbleView.findViewById(R.id.speech_bubble_inner_layout);
			clickRegion.setOnTouchListener(createSpeechBubbleTouchListener());
			closeRegion = speechBubbleView.findViewById(R.id.speech_bubble_close);
			if (closeRegion != null) {
				closeRegion.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hideSpeechBubble();	
					}
				});
			}
			isRecycled = false;
		} else {
			isRecycled = true;
		}
	
		speechBubbleView.setVisibility(View.GONE);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		if (mapOverlays.size() > 1) {
			hideOtherSpeechBubbles(mapOverlays);
		}
		
		if (currentFocusedItem != null)
			speechBubbleView.setData(currentFocusedItem);
		
		GeoPoint point = currentFocusedItem.getPoint();
		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		
		speechBubbleView.setVisibility(View.VISIBLE);
		
		if (isRecycled) {
			speechBubbleView.setLayoutParams(params);
		} else {
			mapView.addView(speechBubbleView, params);
		}
		
		return isRecycled;
	}
	
}
