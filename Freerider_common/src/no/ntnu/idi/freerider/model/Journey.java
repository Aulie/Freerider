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

import java.util.Calendar;

/** A Journey using a specific Route at a specific time, with a specific hitchhiker.*/
public class Journey {
	private Route route;
	private Calendar start;
	private User hitchhiker;
	private final int serial;
	private Visibility visibility;
	
	public Journey(int serial){
		this.serial = serial;
	}
	
	public Journey(int serial, Route route, Calendar start, User hitchhiker, Visibility visibility){
		this.serial = serial;
		this.route = route;
		this.start = start;
		this.hitchhiker = hitchhiker;
		this.visibility = visibility;
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
	
	public User getHitchhiker() {
		return hitchhiker;
	}
	
	public User getDriver(){
		if(route == null) return null;
		return route.getOwner();
	}
	
	public void setHitchhiker(User user) {
		this.hitchhiker = user;
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

}
