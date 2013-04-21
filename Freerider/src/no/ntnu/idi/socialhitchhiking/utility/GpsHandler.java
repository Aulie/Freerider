package no.ntnu.idi.socialhitchhiking.utility;

import no.ntnu.idi.socialhitchhiking.findDriver.FindDriver;
import no.ntnu.idi.socialhitchhiking.map.MapActivityCreateOrEditRoute;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
/**
 * Starts a GPS listener and returns current location to FindDriver class
 * @author Thomas
 * @param <T>
 *
 */
public class GpsHandler<T> {
	private LocationManager locManager;
	private LocationListener locListener;
	Activity activity;
	public GpsHandler(Activity parent) {
		locManager = (LocationManager)parent.getSystemService(Context.LOCATION_SERVICE);
		locListener = new GpsListener();
		activity = parent;
	}
	public void findLocation() {
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
	}
	public void abortGPS() {
		locManager.removeUpdates(locListener);
	}
	public boolean gpsEnabled(){
		if (locManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			return true;
		}
		else {
			return false;
		}
	}

class GpsListener implements LocationListener {
	@Override
	public void onLocationChanged(Location location) {
		if(location != null) {
			locManager.removeUpdates(locListener);
			Log.e("Lat",Double.toString(location.getLatitude()));
			if(activity instanceof FindDriver) {
				((FindDriver)activity).gotLocation(location);
			}
			else
			{
				((MapActivityCreateOrEditRoute)activity).gotLocation(location);
			}
			
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
