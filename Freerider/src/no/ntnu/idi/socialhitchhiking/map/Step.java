package no.ntnu.idi.socialhitchhiking.map;

import java.util.ArrayList;

import no.ntnu.idi.freerider.model.Location;

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
