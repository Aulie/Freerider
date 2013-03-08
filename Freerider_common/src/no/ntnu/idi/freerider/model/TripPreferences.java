package no.ntnu.idi.freerider.model;

import java.util.BitSet;

public class TripPreferences {
	Integer prefId;
	Integer seatsAvailable;
	Boolean music;
	Boolean animals;
	Boolean breaks;
	Boolean talking;
	Boolean smoking;
	
	public TripPreferences(){
		prefId = -1;
		seatsAvailable=1;
		music=false;
		animals=false;
		breaks=false;
		talking=false;
		smoking=false;
	}
	
	public Integer getPrefId() {
		return prefId;
	}

	public void setPrefId(Integer prefId) {
		this.prefId = prefId;
	}

	public TripPreferences(Integer seatsAvailable, Boolean music,
			Boolean animals, Boolean breaks, Boolean talking, Boolean smoking) {
		super();
		this.seatsAvailable = seatsAvailable;
		this.music = music;
		this.animals = animals;
		this.breaks = breaks;
		this.talking = talking;
		this.smoking = smoking;
	}
	public BitSet getExtras(){
		BitSet extras = new BitSet(5);
		extras.set(0, music);
		extras.set(1, animals);
		extras.set(2, breaks);
		extras.set(3, talking);
		extras.set(4, smoking);
		return extras;
	}
	public void setExtras(BitSet extras){
		this.music=extras.get(0);
		this.animals=extras.get(1);
		this.breaks=extras.get(2);
		this.talking=extras.get(3);
		this.smoking=extras.get(4);
	}
	public Integer getSeatsAvailable() {
		return seatsAvailable;
	}

	public void setSeatsAvailable(Integer seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}

	public Boolean getMusic() {
		return music;
	}

	public void setMusic(Boolean music) {
		this.music = music;
	}

	public Boolean getAnimals() {
		return animals;
	}

	public void setAnimals(Boolean animals) {
		this.animals = animals;
	}

	public Boolean getBreaks() {
		return breaks;
	}

	public void setBreaks(Boolean breaks) {
		this.breaks = breaks;
	}

	public Boolean getTalking() {
		return talking;
	}

	public void setTalking(Boolean talking) {
		this.talking = talking;
	}

	public Boolean getSmoking() {
		return smoking;
	}

	public void setSmoking(Boolean smoking) {
		this.smoking = smoking;
	}
	
}
