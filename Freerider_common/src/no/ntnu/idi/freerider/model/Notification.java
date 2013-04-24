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
/** A notification sent from one (prospective) participant in a Journey to another. */
public class Notification {
	private final String senderID;
	private final String recipientID;
	private final String senderName;
	private final String comment;
	private final int journeySerial;
	private final NotificationType type;
	private final Location startPoint, stopPoint;
	private final Calendar timeSent;
	private boolean isRead;
	
	public Notification(String senderID, String recipientID,String senderName,String comment,int journeySerial, NotificationType type){
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.journeySerial = journeySerial;
		this.type = type;
		this.startPoint = this.stopPoint = null;
		this.timeSent = null;
		this.comment = comment;
		this.senderName = senderName;
	}
	
	
	public Notification(String senderID, String recipientID,String senderName,String comment,int journeySerial, NotificationType type, Calendar timeSent){
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.journeySerial = journeySerial;
		this.type = type;
		this.startPoint = this.stopPoint = null;
		this.timeSent = timeSent;
		this.comment = comment;
		this.senderName = senderName;
	}
	
	
	public Notification(String senderID, String recipientID, String senderName, String comment, int journeySerial, NotificationType type, Location startPoint, Location stopPoint){
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.journeySerial = journeySerial;
		this.type = type;
		this.startPoint = startPoint;
		this.stopPoint = stopPoint;
		this.timeSent = null;
		this.comment = comment;
		this.senderName = senderName;
	}
	
	public Notification(String senderID, String recipientID, String senderName, String comment, int journeySerial, NotificationType type, Location startPoint, Location stopPoint, Calendar timeSent){
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.journeySerial = journeySerial;
		this.type = type;
		this.startPoint = startPoint;
		this.stopPoint = stopPoint;
		this.timeSent = timeSent;
		this.comment = comment;
		this.senderName = senderName;
	}
	
	public String getRecipientID() {
		return recipientID;
	}
	public int getJourneySerial() {
		return journeySerial;
	}
	public NotificationType getType() {
		return type;
	}
	public String getSenderID() {
		return senderID;
	}

	public Location getStartPoint() {
		return startPoint;
	}

	public Location getStopPoint() {
		return stopPoint;
	}

	public Calendar getTimeSent() {
		return timeSent;
	}


	public String getComment() {
		return comment;
	}

	public String getSenderName() {
		return senderName;
	}


	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
}
