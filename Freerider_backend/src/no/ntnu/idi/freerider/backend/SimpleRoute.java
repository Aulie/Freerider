package no.ntnu.idi.freerider.backend;

import java.sql.Date;



public class SimpleRoute {
	private int serial;
	private Date dateModified;
	public SimpleRoute(int serial, Date dateModified) {
		this.setSerial(serial);
		this.setDateModified(dateModified);
	}
	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
}
