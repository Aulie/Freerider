package no.ntnu.idi.socialhitchhiking.utility;

import no.ntnu.idi.socialhitchhiking.findDriver.FindDriver;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsHandler {
	private LocationManager locManager;
	private LocationListener locListener;
	FindDriver activity;
	public GpsHandler(Activity parent) {
		locManager = (LocationManager)parent.getSystemService(Context.LOCATION_SERVICE);
		locListener = new GpsListener();
		activity = (FindDriver)parent;
	}
	public void findLocation() {
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
	}
	public void abortGPS() {
		locManager.removeUpdates(locListener);
	}

class GpsListener implements LocationListener {
	@Override
	public void onLocationChanged(Location location) {
		if(location != null) {
			locManager.removeUpdates(locListener);
			Log.e("Lat",Double.toString(location.getLatitude()));
			activity.gotLocation(location);
			try {
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
}
