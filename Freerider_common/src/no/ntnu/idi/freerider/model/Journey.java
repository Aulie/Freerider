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

package no.ntnu.idi.freerider.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/** A Journey using a specific Route at a specific time, with a specific hitchhiker.*/
public class Journey {
	private Route route;
	private Calendar start;
	private List<User> hitchhikers;
	private final int serial;
	private Visibility visibility;
	private TripPreferences tripPreferences;
	
	public Journey(int serial){
		this.serial = serial;
		this.hitchhikers = new ArrayList<User>();
	}
	
	public Journey(int serial, Route route, Calendar start, List<User> hitchhikers, Visibility visibility){
		this.serial = serial;
		this.route = route;
		this.start = start;
		this.hitchhikers = hitchhikers;
		this.visibility = visibility;
	}
	
	public TripPreferences getTripPreferences()
	{
		return tripPreferences;
	}

	public void setTripPreferences(TripPreferences tripPreferences)
	{
		this.tripPreferences = tripPreferences;
	}

	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public Calendar getStart() {
		return start;
	}
	public void setStart(Calendar start) {
		this.start = start;
	}
	
	public List<User> getHitchhikers() {
		return hitchhikers;
	}
	
	public User getDriver(){
		if(route == null) return null;
		return route.getOwner();
	}
	
	public void setHitchhikers(List<User> users) {
		this.hitchhikers = users;
	}
	public void addHitchhiker(User user){
		this.hitchhikers.add(user);
	}
	public int getSerial() {
		return serial;
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		int routesize = route.getMapPoints().size();
		sb.append(route.getRouteData().get(0).getAddress());
		sb.append("  :  "+route.getRouteData().get(routesize).getAddress());
		sb.append("\n"+route.getOwner().getFullName());
		
		return sb.toString();
	}
	public Visibility getVisibility() {
		return visibility;
	}
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	public void setSeatsAvailable(Integer seats){
		tripPreferences.setSeatsAvailable(seats);
	}
	public Integer getSeatsAvailable(){
		return tripPreferences.getSeatsAvailable();
	}
}
