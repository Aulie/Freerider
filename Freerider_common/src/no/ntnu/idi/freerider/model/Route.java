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
package no.ntnu.idi.freerider.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.postgis.LineString;
import org.postgis.Point;

/** A route from one place to another. */
public class Route {
	protected String name;
	protected User owner;
	protected List<Location> routeData;
	protected List<MapLocation> mapPoints;
	protected int serial;

	public Route(){
		
	}
	public Route(Route r){
		name = String.valueOf(r.getName());
		owner = r.getOwner();
		serial = r.getSerial();
		
		routeData = new ArrayList<Location>(r.getRouteData());
		Collections.copy(routeData, r.getRouteData());
		
		mapPoints = new ArrayList<MapLocation>(r.getMapPoints());
		Collections.copy(mapPoints, r.getMapPoints());
	}
	public Route(User user, String name, List<Location> routeData, int serial) {
		this.owner = user;
		this.name = name;
		this.routeData = routeData;
		this.serial = serial;
	}

	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public int getSerial() {
		return serial;
	}
	
	public String getStartAddress(){
		return mapPoints.get(0).getAddress();
	}
	public String getEndAddress(){
		return mapPoints.get(mapPoints.size()-1).getAddress();
	}
	
	public void setMapPoints(List<MapLocation> mapPoints){
		this.mapPoints = mapPoints;
	}
	public void setRouteData(List<Location> routeData){
		this.routeData = routeData;
	}
	public List<MapLocation> getMapPoints(){
		return mapPoints;
	}
	public List<Location> getRouteData(){
		return routeData;
	}
	
	/** Gets the geography data of this route as a PostGIS {@link LineString}. 
	 * This is translated from the List<Location> representation every call, so cache results if you're reusing. */
	public LineString getRouteDataAsLineString() {
		Point[] list = new Point[routeData.size()];
		for (int i = 0; i < list.length; i++) {
			Location location = routeData.get(i);
			list[i] = new Point(location.latitude, location.longitude);
		}
		return new LineString(list);
	}

	/** Gets the geography data of this route's MapLocations as a {@link LineString}.
	 * This is translated from the List<Location> representation every call, so cache results if you're reusing. */
	public LineString getMapPointsAsLineString() {
		Point[] list = new Point[mapPoints.size()];
		for (int i = 0; i < list.length; i++) {
			Location location = mapPoints.get(i);
			list[i] = new Point(location.latitude, location.longitude);
		}
		return new LineString(list);
	}
	
	/** Returns the addresses (of the MapLocations) visited by this Route, as a single String. 
	 * Addresses are separated by {@link ADDRESS_STRING_DELIMITER}.*/
	public String getAddressString(){
		StringBuilder sb = new StringBuilder();
		for(MapLocation location : mapPoints){
			sb.append(location.getAddress());
			sb.append(ADDRESS_STRING_DELIMITER);
		}
		if(sb.length()>2)sb.setLength(sb.length() - 1); //Remove last delimiter char.
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/** The delimiter used by getAddressString() */
	public static final String ADDRESS_STRING_DELIMITER = "¤";
}
