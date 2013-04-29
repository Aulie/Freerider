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
	private String gender;
	private int carId;
	private String about;
	private String phone;
	private int age;
	
	
	public int getCarId()
	{
		return carId;
	}
	public void setCarId(int carId)
	{
		this.carId = carId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAbout()
	{
		return about;
	}
	public void setAbout(String about)
	{
		this.about = about;
	}
	public User(String name,String id){
		this.firstName = name;
		this.ID = id.replaceAll("\\s", "");
		friends = new ArrayList<User>();
	}
	public User (){
		
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
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Method that gets a users Facebook profile picture URL.
	 * 
	 * @param id - String, containing a Facebook users id.
	 * @return {@link String} of the users profile picture.
	 */
	public String getPictureURL(){
		return "http://graph.facebook.com/"+ ID + "/picture?type=normal";
		//return "http://graph.facebook.com/710406250/picture?type=normal";
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
