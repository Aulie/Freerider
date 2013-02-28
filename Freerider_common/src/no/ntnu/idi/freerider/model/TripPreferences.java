package no.ntnu.idi.freerider.model;

public class TripPreferences {
	Integer seatsAvailable;
	Boolean[] extras;
	
	public TripPreferences(){
		seatsAvailable=1;
		for(Boolean b:extras){
			b=false;
		}
	}
	public Integer getSeatsAvailable(){
		return seatsAvailable;
	}
	public void setSeatsAvailable(Integer seatsAvailable){
		this.seatsAvailable = seatsAvailable;
	}
	
	public boolean getBreaks(){
		return extras[0];
	}
	public boolean getAnimals(){
		return extras[1];
	}
}
