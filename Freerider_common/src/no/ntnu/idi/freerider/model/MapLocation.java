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
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.StringTokenizer;


/** A point on the Earth, with an optional address */
public class MapLocation extends Location{
	private static final long serialVersionUID = -5582644184124300618L;
	protected String [] startAddress;
	
	public MapLocation(double latitude, double longitude) {
		super(latitude, longitude);
	}
	public MapLocation(double latitude, double longitude, String address) {
		this(latitude, longitude);
		this.address = address.replace("\n", ", ");
		this.startAddress = address.split(" ");
	}
	public MapLocation(Location location, String address){
		this(location.getLatitude(), location.getLongitude());
		this.address = address.replace("\n", ", ");
		this.startAddress = address.split(" ");
	}
	
	/** 
	 * Returns the address of the {@link MapLocation} 
	 */
	
	
	@Override
	public String getAddress(){
		return address;
	}
	/*
	 * this method takes the address and removes country and zip code from the string.
	 */
	public String getShortAddress(){
		String shortAddress = "";
		ArrayList<String> shortAddressList = new ArrayList<String>();
		
		for (int i = 0; i < this.startAddress.length; i++){
		
		 shortAddressList.add(startAddress[i]);
			
		}
		//remove country
		shortAddressList.remove(shortAddressList.size()-1);
		
		//remove zipcode
		for( int i= 0; i<shortAddressList.size(); i++){
			if(shortAddressList.get(i).length()== 4){
				try {
					Integer.parseInt(shortAddressList.get(i));
					shortAddressList.remove(i);
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}

			}
		}
		for( int i=0; i< shortAddressList.size(); i++){
			shortAddress += shortAddressList.get(i) + " ";
		}
		return shortAddress;
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
