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

/** An enum describing the different Notification types that a driver and hitchhiker can pass between each other concerning a journey. */
public enum NotificationType {
	/** A potential hitchhiker requests to join a Journey. */
	HITCHHIKER_REQUEST,
	/** A driver accepts an {@link HITCHHIKER_REQUEST}.*/
	REQUEST_ACCEPT,
	/** A driver rejects an {@link HITCHHIKER_REQUEST}.*/
	REQUEST_REJECT,
	/** A hitchhiker cancels his involvement in a Journey.*/
	HITCHHIKER_CANCEL,
	/** A driver cancels a planned Journey.*/
	DRIVER_CANCEL,
	/** A hitchhiker gives an acknowledgement of receiving the driver's cancellation.*/
	HITCHHIKER_ACCEPTS_DRIVER_CANCEL,
	/**Send message */
	MESSAGE,
	/** Ask the user for a rating*/
	RATING;
}
