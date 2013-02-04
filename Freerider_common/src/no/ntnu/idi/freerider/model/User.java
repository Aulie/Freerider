/*
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

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** A user of the application. */
public class User {
	private String firstName;
	private String surName;
	private double rating;
	private String ID;
	private InetAddress ip;
	private byte[] picture;
	private List<User> friends;
	
	public User(String name,String id){
		this.firstName = name;
		this.ID = id;
		friends = new ArrayList<User>();
	}
	
	public String getFullName() {
		return firstName+" "+surName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String name) {
		this.firstName = name;
	}
	public String getSurname() {
		return surName;
	}
	public void setSurname(String surname) {
		this.surName = surname;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	/**
	 * Method that gets a users Facebook profile picture URL.
	 * 
	 * @param id - String, containing a Facebook users id.
	 * @return {@link URL} of the users profile picture.
	 */
	public URL getPictureURL(){
		URL img_value = null;
		try {
			img_value = new URL("http://graph.facebook.com/"+ID+"/picture?type=normal");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return img_value;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof User)) return false;
		User other = (User) obj;
		if(!ID.equals(other.ID)) return false; 
		return true;
	}
	
	@Override
	public int hashCode() {
		try{
			return Integer.parseInt(ID);
		}catch(NumberFormatException e){
			return super.hashCode();
		}
	}
	public User getFriend(int i){
		return friends.get(i);
	}
	public List<User> getFriends(){
		return friends;
	}

	@Override
	public String toString() {
		return ID + ":\"" + getFullName() + "\"";
	}
}
