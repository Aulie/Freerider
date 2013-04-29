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
package no.ntnu.idi.socialhitchhiking.map;

import java.util.ArrayList;

import no.ntnu.idi.freerider.model.Location;
/**
 * Object for storing route steps parsed from xml
 * @author Thomas Gjerde
 *
 */
public class Step 
{
	String startLatitude;
	String startLongitude;
	String endLatitude;
	String endLongitude;
	ArrayList<Location> mapPoints;
	String minutesDuration;
	String description;
	String KmDistance;
	public String getStartLatitude() {
		return startLatitude;
	}
	public void setStartLatitude(String startLatitude) {
		this.startLatitude = startLatitude;
	}
	public String getStartLongitude() {
		return startLongitude;
	}
	public void setStartLongitude(String startLongitude) {
		this.startLongitude = startLongitude;
	}
	public String getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(String endLatitude) {
		this.endLatitude = endLatitude;
	}
	public String getEndLongitude() {
		return endLongitude;
	}
	public void setEndLongitude(String endLongitude) {
		this.endLongitude = endLongitude;
	}
	public ArrayList<Location> getMapPoints() {
		return mapPoints;
	}
	public void setMapPoints(ArrayList<Location> mapPoints) {
		this.mapPoints = mapPoints;
	}
	public String getMinutesDuration() {
		return minutesDuration;
	}
	public void setMinutesDuration(String minutesDuration) {
		this.minutesDuration = minutesDuration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKmDistance() {
		return KmDistance;
	}
	public void setKmDistance(String kmDistance) {
		KmDistance = kmDistance;
	}
	public Step() {
		mapPoints = new ArrayList<Location>();
	}
}
