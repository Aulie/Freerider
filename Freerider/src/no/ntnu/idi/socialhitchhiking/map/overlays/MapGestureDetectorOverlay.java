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

import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * An overlay that detects gestures like long press, single tap and double tap.
 * Implements {@link GestureDetector.OnGestureListener} and {@link GestureDetector.OnDoubleTapListener}.
 */
public class MapGestureDetectorOverlay extends Overlay implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
	
	/**
	 * Detects various gestures and events using the supplied {@link MotionEvent}s.
	 */
	private GestureDetector gestureDetector;
	
	/**
	 * Listens for all the different gestures.
	 */
	private GestureDetector.OnGestureListener onGestureListener;
	
	/**
	 * Listens for double taps.
	 */
	private GestureDetector.OnDoubleTapListener onDoubleTapListener;
	
	/**
	 * The constructor.<br>
	 * Initializes the {@link GestureDetector} and sets the gesture listener 
	 * and double tap listener. 
	 * <br><br>
	 * For use with a (class that extends) {@link com.google.android.maps.MapActivity}, 
	 * let the MapActivity implement GestureDetector.OnGestureListener and GestureDetector.OnDoubleTapListener 
	 * and use the MapActivity-instance as an argument. 
	 * 
	 * @param onGestureListener 
	 * @param onDoubleTapListener
	 */
	public MapGestureDetectorOverlay(GestureDetector.OnGestureListener onGestureListener, GestureDetector.OnDoubleTapListener onDoubleTapListener) {
		gestureDetector = new GestureDetector(this);
		setOnGestureListener(onGestureListener);
		setOnDoubleTapListener(onDoubleTapListener);
	}
	
	/**
	 * @return Returns true if long press is enabled, false otherwise.
	 */
	public boolean isLongpressEnabled() {
		return gestureDetector.isLongpressEnabled();
	}

	/**
	 * Sets whether long press is enabled or not.
	 * @param isLongpressEnabled 
	 */
	public void setIsLongpressEnabled(boolean isLongpressEnabled) {
		gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
	}

	/**
	 * @return Returns the {@link OnGestureListener}.
	 */
	public GestureDetector.OnGestureListener getOnGestureListener() {
		return onGestureListener;
	}
	
	/**
	 * Sets the {@link OnGestureListener}.
	 * @param onGestureListener
	 */
	public void setOnGestureListener(GestureDetector.OnGestureListener onGestureListener) {
		this.onGestureListener = onGestureListener;
	}
	
	/**
	 * Sets the {@link OnDoubleTapListener}.
	 * @param onDoubleTapListener
	 */
	public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
		this.onDoubleTapListener = onDoubleTapListener;
	}
	
	/**
	 * @return Returns the {@link OnDoubleTapListener}.
	 */
	public GestureDetector.OnDoubleTapListener getOnDoubleTapListener() {
		return this.onDoubleTapListener;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.android.maps.Overlay#onTouchEvent(android.view.MotionEvent, com.google.android.maps.MapView)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		else{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		if (onGestureListener != null) {
			return onGestureListener.onDown(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (onGestureListener != null) {
			return onGestureListener.onFling(e1, e2, velocityX, velocityY);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		if (onGestureListener != null) {
			onGestureListener.onLongPress(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (onGestureListener != null) {
			onGestureListener.onScroll(e1, e2, distanceX, distanceY);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		if (onGestureListener != null) {
			onGestureListener.onShowPress(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (onGestureListener != null) {
			onGestureListener.onSingleTapUp(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnDoubleTapListener#onDoubleTap(android.view.MotionEvent)
	 */
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if(onDoubleTapListener != null){
			return onDoubleTapListener.onDoubleTap(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnDoubleTapListener#onDoubleTapEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		if(onDoubleTapListener != null){
			return onDoubleTapListener.onDoubleTapEvent(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.GestureDetector.OnDoubleTapListener#onSingleTapConfirmed(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if(onDoubleTapListener != null){
			return onDoubleTapListener.onSingleTapConfirmed(e);
		} 
		return false;
	} 
}
