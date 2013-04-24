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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.freerider.model.MapLocation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * A helper class for everything that has to do with geocoding/reverse geocoding and 
 * converting between different kinds of"geo"-objects.
 */
public class GeoHelper {

	private static Geocoder fancyGeocoder = null;
	
	public static void initGeocoder(Context context){
		fancyGeocoder = new Geocoder(context);
	}

	public static List<Address> getAddressesFromLocation(double lat, double lon, int maxResults){
		JSONArray responseArray = getJSONArrayFromLocation(lat, lon, maxResults);
		List<Address> addresses = new ArrayList<Address>();
		JSONObject jsonObj;
		for (int i = 0; i < responseArray.length(); i++) {
			Address address = new Address(Locale.getDefault());
			String addressLine;
			try {
				jsonObj = responseArray.getJSONObject(i);
				addressLine = jsonObj.getString("formatted_address");
				if(addressLine.contains(",")){
					String[] lines = addressLine.split(",");
					for (int j = 0; j < lines.length; j++) {
						address.setAddressLine(j, lines[j].trim());
					}
				}
				addresses.add(address);
			} catch (JSONException e) {
				continue;
			}
		}
		return addresses;
	}
	
	/**
	 * Retrieves the address at the given {@link GeoPoint}, by calling {@link #getAddressesAtPressedPoint(GeoPoint, int, int)}
	 * and returns the first {@link String} in the {@link List} returned.
	 * 
	 * @param location The location that you want the address of
	 * @return Returns the address at the given {@link GeoPoint}, or "Could not find the address." if it is not found.
	 */
	public static String getAddressAtPointString(GeoPoint location){
		List<String> addresses = getAddressesAtPoint(location, 1, 5);
		if(addresses.size() > 0){
			return addresses.get(0);
		}
		return "Could not find the address.";
	}

	/**
	 * Retrieves a {@link List} of addresses that match the given {@link GeoPoint}. 
	 * The first element in the list has the best match (but is not guaranteed to be correct). <br><br>
	 * 
	 * This method tries to use the {@link Geocoder} to transform a (latitude, longitude) 
	 * coordinate into addresses, and if this fails (witch it most likely will under emulation), it 
	 * tries to use a method from the {@link GeoHelper}-class.
	 * 
	 * @param location The location that is transformed into a list of addresses
	 * @param maxResults The maximum number of addresses to retrieve (should be small).
	 * @param maxAddressLines The maximum number of lines in the addresses. This should be high if you want a complete address! If it is smaller than the total number of lines in the address, it cuts off the last part...) 
	 * @return Returns the {@link List} of addresses (as {@link String}s).
	 */
	public static List<String> getAddressesAtPoint(final GeoPoint location, final int maxResults, int maxAddressLines){
		List<String> addressList = new ArrayList<String>();
		List<Address> possibleAddresses = new ArrayList<Address>();
		Address address = new Address(Locale.getDefault());
		String addressString = "Could not find the address...";
		ExecutorService executor = Executors.newSingleThreadExecutor();
	    
		Callable<List<Address>> callable = new Callable<List<Address>>() {
	        @Override
	        public List<Address> call() throws IOException {
	        	return fancyGeocoder.getFromLocation(
						location.getLatitudeE6() / 1E6, 
						location.getLongitudeE6()/ 1E6, maxResults);
	        }
	    };
	    Future<List<Address>> future = executor.submit(callable);
	    try {
			possibleAddresses = future.get();
		} catch (InterruptedException e1) {
			possibleAddresses = GeoHelper.getAddressesFromLocation(
					location.getLatitudeE6() / 1E6, 
					location.getLongitudeE6()/ 1E6, maxResults);
		} catch (ExecutionException e1) {
			possibleAddresses = GeoHelper.getAddressesFromLocation(
					location.getLatitudeE6() / 1E6, 
					location.getLongitudeE6()/ 1E6, maxResults);
		}
	    executor.shutdown();
		
		if (possibleAddresses.size() > 0){
			for (int i = 0; i < possibleAddresses.size(); i++) {
				addressString = "";
				address = possibleAddresses.get(i);
				for (int j=0; j <= address.getMaxAddressLineIndex() && j <= maxAddressLines; j++){
					addressString += address.getAddressLine(j);
					addressString += "\n";
				}
				addressList.add(addressString.trim());
			}
		}
		return addressList;
	}
	
	private static JSONArray getJSONArrayFromLocation(final double lat, final double lon, int maxResults) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
	    
		Callable<JSONArray> callable = new Callable<JSONArray>() {
	        @Override
	        public JSONArray call() throws IOException {
	        	String urlStr = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=false";
	        	Log.e("URL",urlStr);
	    		String response = "";
	    		HttpClient client = new DefaultHttpClient();
	    			HttpResponse hr = client.execute(new HttpGet(urlStr));
	    			HttpEntity entity = hr.getEntity();
	    			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
	    			String buff = null;
	    			while ((buff = br.readLine()) != null)
	    				response += buff;

	    			br.close();
	    		JSONArray responseArray = null;
	    		try {
	    			JSONObject jsonObject = new JSONObject(response);
	    			responseArray = jsonObject.getJSONArray("results");
	    		} catch (JSONException e) {
	    			Log.e("Jason",e.getMessage());
	    			return null;
	    		}
	    		return responseArray;
	        }
	    };
	    Future<JSONArray> future = executor.submit(callable);
	    JSONArray ret;
			try {
				ret = future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				ret = null;
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				ret = null;
			}
		

		return ret;
	}
	
	/**
	 * Converts a {@link GeoPoint}-object to a {@link Location}-object.
	 * @param gp The {@link GeoPoint} to convert
	 * @return The returned {@link Location}
	 */
	public static Location getLocation(GeoPoint gp){
		return new MapLocation(
				(gp.getLatitudeE6() / 1E6),
				(gp.getLongitudeE6()/ 1E6), 
				getAddressAtPointString(gp));
	}
	
	/**
	 * Creates a {@link MapLocation}-object from two doubles (latitude and longitude).
	 * Retrieves the address by using the method {@link #getAddressAtPointString(GeoPoint)}.
	 * @param lat The Latitude of the location
	 * @param lon The Longitude of the location
	 * @return The returned {@link MapLocation}
	 */
	public static MapLocation getMapLocation(double lat, double lon){
		return new MapLocation(lat,lon, 
			getAddressAtPointString(new GeoPoint((int)(lat*1E6), (int)(lon*1E6))));
	}
	
	/**
	 * Converts an address ({@link String}) to a {@link MapLocation}.
	 */
	public static MapLocation getLocation(String address){
		GeoPoint gp = getGeoPoint(address);
		double lat = (gp.getLatitudeE6() / 1E6);
		double lon = (gp.getLongitudeE6()/ 1E6);
		String detailedAddress = getAddressAtPointString(gp);
		return new MapLocation(lat, lon, detailedAddress);
	}
	
	/**
	 * Converts an address ({@link String}) to a {@link GeoPoint}.
	 */
	public static GeoPoint getGeoPoint(String address){
		return GeoHelper.getLatLong(getLocationInfo(address));
	}
	
	private static GeoPoint  getLatLong(JSONObject jsonObject) {
		Double lon = new Double(0);
		Double lat = new Double(0);
		try {
			lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");
			lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
	}

	private static JSONObject getLocationInfo(final String adr) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
	    Callable<StringBuilder> callable = new Callable<StringBuilder>() {
	        @Override
	        public StringBuilder call() throws ClientProtocolException, IOException {
	        	StringBuilder stringBuilder = new StringBuilder();
	        		String address;
	    			address = adr.replaceAll(" ","%20");    
	    			address = address.replaceAll("\n", "%20");
	    			HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
	    			HttpClient client = new DefaultHttpClient();
	    			HttpResponse response;
	    			stringBuilder = new StringBuilder();

	    			response = client.execute(httppost);
	    			HttpEntity entity = response.getEntity();
	    			InputStream stream = entity.getContent();
	    			int b;
	    			while ((b = stream.read()) != -1) {
	    				stringBuilder.append((char) b);
	    			}
	    			return stringBuilder;
	        }
	    };
	    Future<StringBuilder> future = executor.submit(callable);
	    StringBuilder stringBuilder;
		try {
			stringBuilder = future.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			stringBuilder = new StringBuilder();
		} catch (ExecutionException e1) {
			stringBuilder = new StringBuilder();
		}
	    executor.shutdown();
		

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
	
	/**
	 * Converts a {@link Location} to a {@link GeoPoint}.
	 */
	public static GeoPoint getGeoPoint(Location loc){
		return new GeoPoint(
				(int)(loc.getLatitude()  * 1E6),
				(int)(loc.getLongitude() * 1E6));
	}
	
	/**
	 * Converts an array of {@link String}s to a {@link List} of {@link MapLocation}s.
	 */
	public static List<MapLocation> getLocationList(String[] addresses){
		List<MapLocation> list = new ArrayList<MapLocation>();
		for (int i = 0; i < addresses.length; i++) {
			list.add(GeoHelper.getLocation(addresses[i]));
		}
		return list;
	}
}
