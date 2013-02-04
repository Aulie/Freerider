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


/** A point on the Earth, with an optional address */
public class MapLocation extends Location{
	private static final long serialVersionUID = -5582644184124300618L;
	
	public MapLocation(double latitude, double longitude) {
		super(latitude, longitude);
	}
	public MapLocation(double latitude, double longitude, String address) {
		this(latitude, longitude);
		this.address = address.replace("\n", ", ");
	}
	public MapLocation(Location location, String address){
		this(location.getLatitude(), location.getLongitude());
		this.address = address.replace("\n", ", ");
	}
	
	/** 
	 * Returns the address of the {@link MapLocation} 
	 */
	@Override
	public String getAddress(){
		return address;
	}
	
	/**
	 * Returns the address of the {@link MapLocation} 
	 */
	public String getAddressWithLines(){
		return address.replace(",", "\n");
	}
	
	
	@Override
	public String toString() {
		return getAddress();
	}
}
